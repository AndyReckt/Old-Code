/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server

import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import cc.fyre.stark.engine.command.defaults.commands.admin.SetSlotsCommand
import com.google.gson.JsonObject
import org.bukkit.Bukkit

class ServerUpdateListener : MessageListener {

    @IncomingMessageHandler("UPDATE_WHITELIST")
    fun onWhitelistUpdate(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val whitelisted = data.get("whitelisted").asBoolean
        if (serverName == Bukkit.getServerName()) {
            Bukkit.setWhitelist(whitelisted)
        }
    }

    @IncomingMessageHandler("UPDATE_SLOTS")
    fun onUpdateSlots(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val slots = data.get("slots").asInt
        if (serverName == Bukkit.getServerName()) {
            SetSlotsCommand.set(slots)
        }
    }
}