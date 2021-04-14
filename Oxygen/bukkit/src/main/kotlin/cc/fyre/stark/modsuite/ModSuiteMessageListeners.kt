/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import cc.fyre.stark.modsuite.options.ModOptionsHandler
import cc.fyre.stark.util.CC
import com.google.gson.JsonObject
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*

class ModSuiteMessageListeners : MessageListener {

    @IncomingMessageHandler("SERVER_START")
    fun onIncomingServerStart(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val port = data.get("port").asInt
        val whitelisted = data.get("whitelisted").asBoolean

        Bukkit.getOnlinePlayers().forEach {
            if (it.isOp) {
                it.sendMessage(CC.CHAT_BAR)
                it.sendMessage(ChatColor.DARK_RED.toString() + CC.BOLD + "Server Status")
                it.sendMessage(" ")
                it.sendMessage(ChatColor.RED.toString() + serverName + ChatColor.GREEN.toString() + " has started successfully.")
                it.sendMessage(" ")
                it.sendMessage(ChatColor.RED.toString() + "Port: " + CC.WHITE + port)
                it.sendMessage(ChatColor.RED.toString() + "Whitelisted: " + CC.WHITE + if (whitelisted) CC.RED + "True" else CC.WHITE + "False")
                it.sendMessage(CC.CHAT_BAR)
            }
        }
    }

    @IncomingMessageHandler("SERVER_STOP")
    fun onIncomingServerStop(data: JsonObject) {
        val serverName = data.get("serverName").asString

        Bukkit.getOnlinePlayers().forEach {
            if (it.isOp) {
                it.sendMessage(CC.CHAT_BAR)
                it.sendMessage(ChatColor.DARK_RED.toString() + CC.BOLD + "Server Status")
                it.sendMessage(" ")
                it.sendMessage(ChatColor.RED.toString() + serverName + ChatColor.RED.toString() + " has successfully shutdown.")
                it.sendMessage(CC.CHAT_BAR)
            }
        }
    }

    @IncomingMessageHandler("STAFF_CHAT")
    fun onIncomingStaffChatMessage(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val senderName = data.get("senderName").asString
        val message = data.get("message").asString

        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("stark.staff")) {
                val options = ModOptionsHandler.get(it)
                val profile = Stark.instance.core.getProfileHandler().getByUUID(it.uniqueId)!!
                val rank = profile.getRank()
                if (options.receivingStaffChat) {
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}[SC] ${ChatColor.GRAY}[$serverName] " + ChatColor.translateAlternateColorCodes('&',rank.gameColor + senderName + "${ChatColor.GRAY}:${ChatColor.WHITE} $message"))
                }
            }
        }
    }

    @IncomingMessageHandler("REQUEST")
    fun onIncomingRequestMessage(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val senderName = data.get("senderName").asString
        val reason = data.get("reason").asString

        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("stark.staff")) {
                val options = ModOptionsHandler.get(it)
                if (options.receivingRequests) {
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}[R] ${ChatColor.GRAY}[$serverName] ${ChatColor.AQUA}$senderName ${ChatColor.GRAY}requested assistance.")
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}Reason: ${ChatColor.GRAY}$reason")
                }
            }
        }
    }
//
    @IncomingMessageHandler("REPORT")
    fun onIncomingReportMessage(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val senderName = data.get("senderName").asString
        val reportedUuid = data.get("reportedUuid").asString
        val reportedName = data.get("reportedName").asString
        val reason = data.get("reason").asString
        val reportCount = ModRequestHandler.getReportCount(UUID.fromString(reportedUuid))

        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("stark.staff")) {
                val options = ModOptionsHandler.get(it)
                if (options.receivingRequests) {
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}[R] ${ChatColor.GRAY}[$serverName] ${ChatColor.AQUA}$reportedName ${ChatColor.GRAY}($reportCount) reported by ${ChatColor.AQUA}$senderName")
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}Reason: ${ChatColor.GRAY}$reason")
                }
            }
        }
    }

}