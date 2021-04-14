/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.server

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.pidgin.message.Message
import redis.clients.jedis.ScanParams
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * A class used for managing and synchronizing server information.
 *
 * Redis tree structure:
 *
 *  stark:
 *    serverSync:
 *      server:
 *        serverMeta:
 *          Hub-1:
 *            serverName: Hub-1
 *            serverGroup: Hubs
 *            serverPort: 25588
 *            ...
 *        portLookup:
 *          25588: Hub-1
 *      group:
 *        Hubs:
 *          groupName: Hub
 *          configuration: JSON string
 *
 * The load procedure goes as follows:
 *  - Load server group types into memory
 *  - Load servers and pair them with their
 *      assigned group
 *
 * If the [Servers] module is being instantiated through a bukkit
 * instance, the [ensureServerExists] and [checkNamePortMatch]
 * functions should be executed in that order on startup.
 * This ensures that the bungee data matches the bukkit data.
 *
 */
class Servers {

    var globalCount = 0
    val groups: MutableMap<String, ServerGroup> = hashMapOf()

    fun initialLoad() {
        loadServerGroups()
        loadServers()
    }

    /**
     * Streams all servers.
     *
     * @return a [<] of the list of servers
     */
    fun streamServers(): Stream<Server> {
        return groups.values
                .stream()
                .flatMap<Server> { serverGroup -> serverGroup.servers.stream() }
    }

    /**
     * Streams all servers.
     *
     * @return a [<] of the list of servers
     */
    fun streamServerGroups(): Stream<ServerGroup> {
        return groups.values.stream()
    }

    /**
     * Gets a [Server] by the given [serverName] if loaded into memory.
     *
     * @param serverName the server name
     *
     * @return an [Server] object if the cache contains a server matching the given [serverName]
     */
    fun getServerByName(serverName: String): Optional<Server> {
        return Optional.ofNullable(streamServers()
                .filter { server -> server.serverName.equals(serverName, true) }
                .collect(collectFirst<Server>()))
    }

    /**
     * Attempts to fetch server data by name and instantiate a new [Server] object if any data was found.
     *
     * @param serverName the server port
     *
     * @return an [<] object where the value is present if any data was successfully retrieved from Redis.
     */
    fun lookupServerByName(serverName: String): Optional<Server> {
        return Optional.ofNullable(StarkCore.instance.redis.runBackboneRedisCommand { client ->
            val map = client.hgetAll("stark:serverSync:server:serverMeta:$serverName")
            if (map.isEmpty()) null else Server(map)
        })
    }

    /**
     * Gets a [ServerGroup] object by the given [groupName].
     *
     * @param groupName the group name
     *
     * @return an [ServerGroup] object if the cache contains a server group matching the given [groupName]
     */
    fun getServerGroupByName(groupName: String): Optional<ServerGroup> {
        return Optional.ofNullable(streamServerGroups()
                .filter { serverGroup -> serverGroup.groupName == groupName }
                .collect(collectFirst<ServerGroup>()))
    }

    /**
     * Saves a server group to Redis.
     *
     * @param serverGroup the server group
     */
    fun saveServerGroup(serverGroup: ServerGroup) {
        StarkCore.instance.redis.runBackboneRedisCommand { jedis ->
            jedis.hmset("stark:serverSync:group:${serverGroup.groupName}", serverGroup.toMap())
        }

        StarkCore.instance.globalMessageChannel.sendMessage(Message(SERVER_GROUP_UPDATE, serverGroup.toMap()))
    }

    /**
     * Loads or creates and stores a new [ServerGroup] object.
     *
     * @param groupName the group name
     *
     * @return the existing or newly created [ServerGroup] object
     */
    fun loadOrCreateServerGroup(groupName: String): ServerGroup {
        return StarkCore.instance.redis.runBackboneRedisCommand { jedis ->
            val exists = jedis.exists("stark:serverSync:group:$groupName")

            val group = if (exists) {
                ServerGroup(jedis.hgetAll("stark:serverSync:group:$groupName"))
            } else {
                ServerGroup(groupName)
            }

            if (!exists) {
                saveServerGroup(group)

                StarkCore.instance.globalMessageChannel.sendMessage(Message(SERVER_GROUP_UPDATE, group.toMap()))
            }

            groups[groupName] = group

            return@runBackboneRedisCommand group
        }
    }

    fun deleteServerGroup(groupName: String): ServerGroup {
        return groups.remove(groupName)!!
    }

    /**
     * Saves a server to Redis.
     *
     * @param server the server
     */
    fun saveServer(server: Server) {
        StarkCore.instance.redis.runBackboneRedisCommand { jedis ->
            jedis.set("stark:serverSync:server:portLookup:${server.serverPort}", server.serverPort.toString())
            jedis.hmset("stark:serverSync:server:serverMeta:${server.serverName}", server.toMap())
        }

        StarkCore.instance.globalMessageChannel.sendMessage(Message(SERVER_UPDATE, server.toMap()))
    }

    /**
     * Loads or creates and stores a new [Server] object.
     *
     * @param serverName the server name
     * @param serverPort the server port
     *
     * @return the existing or newly created [Server] object
     */
    fun loadOrCreateServer(serverName: String, serverPort: Int): Server {
        return StarkCore.instance.redis.runBackboneRedisCommand { jedis ->
            val exists = jedis.exists("stark:serverSync:server:serverMeta:$serverName")

            val server = if (exists) {
                Server(jedis.hgetAll("stark:serverSync:server:serverMeta:$serverName"))
            } else {
                Server(serverName, "default", serverPort)
            }

            if (!exists) {
                saveServer(server)

                StarkCore.instance.globalMessageChannel.sendMessage(Message(SERVER_UPDATE, server.toMap()))
            }

            val group = getServerGroupByName(server.serverGroup)

            if (group.isPresent) {
                group.get().servers.add(server)
            } else {
                loadOrCreateServerGroup(server.serverGroup).servers.add(server)
            }

            return@runBackboneRedisCommand server
        }
    }

    /**
     * Loads the server groups stored in Redis into memory.
     */
    private fun loadServerGroups() {
        StarkCore.instance.redis.runBackboneRedisCommand { jedis ->
            jedis.scan("0", ScanParams().match("stark:serverSync:group:*")).result.forEach { key ->
                groups[key.split(":").last()] = ServerGroup(key.split(":").last())
            }
        }
    }

    /**
     * Loads the servers stored in Redis into memory.
     */
    private fun loadServers() {
        StarkCore.instance.redis.runBackboneRedisCommand { jedis ->
            jedis.scan("0", ScanParams().match("stark:serverSync:server:serverMeta:*")).result.forEach { key ->
                lookupServerByName(key.split(":").last()).ifPresent { server ->
                    val group = getServerGroupByName(server.serverGroup)

                    if (group.isPresent) {
                        group.get().servers.add(server)
                    } else {
                        loadOrCreateServerGroup(server.serverGroup).servers.add(server)
                    }
                }
            }
        }
    }

    /**
     * Checks if a [Server] by the [serverName] exists and if the
     * [serverPort] matches.
     *
     * If the name port is a mismatch, that means the bukkit
     * and bungee configurations are mismatched.
     */
    fun checkNamePortMatch(serverName: String, serverPort: Int): Boolean {
        val server = lookupServerByName(serverName)

        if (server.isPresent) {
            if (server.get().serverPort == serverPort) {
                return true
            }
        }

        return false
    }

    private fun <T> collectFirst(): Collector<T, *, T> {
        return Collectors.collectingAndThen(Collectors.toList()) { list -> if (list.isEmpty()) null else list[0] }
    }

    companion object {
        private const val SERVER_GROUP_UPDATE = "SERVER_GROUP_UPDATE"
        private const val SERVER_UPDATE = "SERVER_UPDATE"
    }

}
