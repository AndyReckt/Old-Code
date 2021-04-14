/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.staff

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import com.google.gson.JsonObject
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent

class StaffMessageListeners : MessageListener {

    private val PREFIX = "${ChatColor.BLUE}${ChatColor.BOLD}[Staff] ${ChatColor.RESET}"

    @IncomingMessageHandler("STAFF_ACTION")
    fun onStaffActionMessage(json: JsonObject) {
        val playerName = ChatColor.translateAlternateColorCodes('&', json.get("playerName").asString)

        when (StaffAction.valueOf(json.get("action").asString)) {
            StaffAction.JOIN_NETWORK -> {
                val to = json.get("to").asString

                Stark.instance.proxy.players.forEach { player ->
                    if (player.hasPermission("stark.staff")) {
                        player.sendMessage(*TextComponent.fromLegacyText("$PREFIX$playerName ${ChatColor.GREEN}joined ${ChatColor.AQUA}the network ($to)"))
                    }
                }
            }
            StaffAction.LEAVE_NETWORK -> {
                val from = json.get("from").asString

                Stark.instance.proxy.players.forEach { player ->
                    if (player.hasPermission("stark.staff")) {
                        if (player.server.info.name == from) {
                            player.sendMessage(*TextComponent.fromLegacyText("$PREFIX$playerName ${ChatColor.RED}left ${ChatColor.AQUA}the network (from your server)"))
                        } else {
                            player.sendMessage(*TextComponent.fromLegacyText("$PREFIX$playerName ${ChatColor.RED}left ${ChatColor.AQUA}the network (from $from)"))
                        }
                    }
                }
            }
            StaffAction.SWITCH_SERVER -> {
                val from = json.get("from").asString
                val to = json.get("to").asString

                Stark.instance.proxy.players.forEach { player ->
                    if (player.hasPermission("stark.staff")) {
                        if (player.server.info.name == to && player.name != ChatColor.stripColor(playerName)) {
                            player.sendMessage(*TextComponent.fromLegacyText("$PREFIX$playerName ${ChatColor.AQUA}joined your server (from $from)"))
                        } else if (player.server.info.name == from) {
                            player.sendMessage(*TextComponent.fromLegacyText("$PREFIX$playerName ${ChatColor.AQUA}left your server (to $to)"))
                        } else {
                            player.sendMessage(*TextComponent.fromLegacyText("$PREFIX$playerName ${ChatColor.AQUA}joined $to (from $from)"))
                        }
                    }
                }
            }
        }
    }

}