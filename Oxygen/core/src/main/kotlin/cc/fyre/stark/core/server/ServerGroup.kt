/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.server

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

class ServerGroup(groupName: String) {

    /**
     * The group name
     */
    val groupName: String = groupName

    /**
     * The list of servers
     */
    val servers: MutableList<Server> = ArrayList()

    /**
     * The global configuration
     */
    var configuration: JsonObject = JsonObject()

    constructor(map: Map<String, String>) : this(map["groupName"]!!) {
        configuration = PARSER.parse(map["configuration"]!!).asJsonObject
    }

    /**
     * Gets the total player count
     *
     * @return the sum of all the servers' player counts
     */
    fun getTotalPlayerCount(): Int {
        return servers.stream().mapToInt { server -> server.getPlayerCount().orElse(0) }.sum()
    }

    /**
     * Gets a map of the key-specific data provided by this group.
     *
     * @return a [,][<] object
     */
    fun toMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map["groupName"] = groupName
        map["configuration"] = configuration.toString()
        return map
    }

    companion object {
        private val PARSER = JsonParser()
    }

}
