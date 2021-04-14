/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.server

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object ServerMessageListeners : MessageListener {

    @IncomingMessageHandler("GLOBAL_COUNT")
    fun onGlobalCount(json: JsonObject) {
        StarkCore.instance.servers.globalCount = json["globalCount"].asInt
    }

    @IncomingMessageHandler("SERVER_GROUP_UPDATE")
    fun onServerGroupUpdate(json: JsonObject) {
        val map = GSON.fromJson<Map<String, String>>(json, TYPE) as Map<String, String>

        val optionalGroup = StarkCore.instance.servers.getServerGroupByName(map["groupName"]!!)

        if (optionalGroup.isPresent) {
            optionalGroup.get().configuration = GSON.fromJson(map["configuration"]!!, JsonObject::class.java)
        } else {
            val group = ServerGroup(map)
            StarkCore.instance.servers.groups[group.groupName] = group
        }
    }

    @IncomingMessageHandler("SERVER_UPDATE")
    fun onServerUpdate(json: JsonObject) {
        val map = GSON.fromJson<Map<String, String>>(json, TYPE) as Map<String, String>

        val optionalServer = StarkCore.instance.servers.getServerByName(map["serverName"]!!)

        if (optionalServer.isPresent) {
            val server = optionalServer.get()
            server.lastHeartbeat = map["lastHeartbeat"]!!.toLong()
            server.currentUptime = map["currentUptime"]!!.toLong()
            server.currentTps = map["currentTps"]!!.toDouble()
            server.playerCount = map["playerCount"]!!.toInt()
            server.maxSlots = map["maxSlots"]!!.toInt()
            server.whitelisted = map["whitelisted"]!!.toBoolean()
        } else {
            StarkCore.instance.servers.loadOrCreateServer(map["serverName"]!!, map["serverPort"]!!.toInt())
        }
    }

    @IncomingMessageHandler("SERVER_WHITELIST_UPDATE")
    fun onServerWhitelistUpdate(json: JsonObject) {
        val map = GSON.fromJson<Map<String, String>>(json, TYPE) as Map<String, String>

        val optionalServer = StarkCore.instance.servers.getServerByName(map.getValue("serverName"))

        if (optionalServer.isPresent) {
            val server = optionalServer.get()
            server.lastHeartbeat = map["lastHeartbeat"]!!.toLong()
            server.currentUptime = map["currentUptime"]!!.toLong()
            server.currentTps = map["currentTps"]!!.toDouble()
            server.playerCount = map["playerCount"]!!.toInt()
            server.maxSlots = map["maxSlots"]!!.toInt()
            server.whitelisted = map["whitelisted"]!!.toBoolean()
        } else {
            StarkCore.instance.servers.loadOrCreateServer(map["serverName"]!!, map["serverPort"]!!.toInt())
        }
    }

    @IncomingMessageHandler("SERVER_SLOTS_UPDATE")
    fun onServerSlotsUpdate(json: JsonObject) {
        val map = GSON.fromJson<Map<String, String>>(json, TYPE) as Map<String, String>

        val optionalServer = StarkCore.instance.servers.getServerByName(map["serverName"]!!)

        if (optionalServer.isPresent) {
            val server = optionalServer.get()
            server.maxSlots = map["maxSlots"]!!.toInt()
        } else {
            StarkCore.instance.servers.loadOrCreateServer(map["serverName"]!!, map["serverPort"]!!.toInt())
        }
    }

    private val GSON = GsonBuilder().create()
    private val TYPE = TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).rawType
}