/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.server

import java.util.*
import kotlin.collections.HashMap

class Server(val serverName: String, var serverGroup: String, val serverPort: Int) {

    /**
     * The last heartbeat time in milliseconds
     */
    var lastHeartbeat: Long = 0

    /**
     * The current uptime in milliseconds
     */
    var currentUptime: Long = 0

    /**
     * The current TPS
     */
    var currentTps: Double = 0.toDouble()

    /**
     * The current staff count
     */
    var staffCount: Int = 0

    /**
     * The current player count
     */
    var playerCount: Int = 0

    /**
     * The maximum amount of players the server can hold
     */
    var maxSlots: Int = 0

    /**
     * If the server is currently whitelisted
     */
    var whitelisted: Boolean = false

    /**
     * Loads server data from key-specific populated map
     */
    constructor(map: Map<String, String>) : this(map["serverName"]!!, map["serverGroup"]!!, map["serverPort"]!!.toInt()) {
        lastHeartbeat = map["lastHeartbeat"]!!.toLong()
        currentUptime = map["currentUptime"]!!.toLong()
        currentTps = map["currentTps"]!!.toDouble()
        playerCount = map["playerCount"]!!.toInt()
        staffCount = map["staffCount"]!!.toInt()
        maxSlots = map["maxSlots"]!!.toInt()
        whitelisted = map["whitelisted"]!!.toBoolean()
    }

    /**
     * Gets whether the server is considered online or offline.
     * If the last heartbeat time of this server exceeds 5 seconds, the server is considered offline.
     *
     * @return true if the server is considered online, otherwise false.
     */
    fun getCount(): Optional<Int> {
        return Optional.of(playerCount)
    }

    /**
     * Gets whether the server is considered online or offline.
     * If the last heartbeat time of this server exceeds 5 seconds, the server is considered offline.
     *
     * @return true if the server is considered online, otherwise false.
     */
    fun isOnline(): Boolean {
        return (System.currentTimeMillis() - lastHeartbeat) < 5000L
    }

    /**
     * Gets the current uptime in milliseconds.
     *
     * @return an [<] object where the value is this server's current
     * uptime and is present if [Server.isOnline] returns true
     */
    fun getCurrentUptime(): Optional<Long> {
        return if (isOnline()) Optional.of(currentUptime) else Optional.empty()
    }

    /**
     * Gets the current TPS.
     *
     * @return an [<] object where the value is this server's current
     * TPS and is present if [Server.isOnline] returns true
     */
    fun getCurrentTps(): Optional<Double> {
        return if (isOnline()) Optional.of(currentTps) else Optional.empty()
    }

    /**
     * Gets the current player count.
     *
     * @return an [<] object where the value is this server's current
     * player count and is present if [Server.isOnline] returns true
     */
    fun getPlayerCount(): Optional<Int> {
        return if (isOnline()) Optional.of(playerCount) else Optional.empty()
    }


    /**
     * Gets a map populated with the key-specific data provided by
     * this server.
     *
     * @return a [,][<] object
     */
    fun toMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map["serverName"] = serverName
        map["serverGroup"] = serverGroup
        map["serverPort"] = serverPort.toString()
        map["lastHeartbeat"] = lastHeartbeat.toString()
        map["currentUptime"] = currentUptime.toString()
        map["currentTps"] = currentTps.toString()
        map["playerCount"] = playerCount.toString()
        map["staffCount"] = staffCount.toString()
        map["maxSlots"] = maxSlots.toString()
        map["whitelisted"] = whitelisted.toString()
        return map
    }

}