/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.modsuite.options.ModOptionsHandler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object StaffChatCommand {
    @Command(["staffchat", "sc"], permission = "stark.staff", description = "Send a message through staff chat.", async = true)
    @JvmStatic
    fun execute(player: Player, @Param(name = "message", wildcard = true) message: String) {
        val options = ModOptionsHandler.get(player)

        if (!options.receivingStaffChat) {
            player.sendMessage("${ChatColor.RED}You have staff chat disabled.")
            return
        }

        val data = mapOf(
            "serverName" to Bukkit.getServerId(),
            "senderName" to player.name,
            "message" to message
        )

        Stark.instance.core.globalMessageChannel.sendMessage(Message("STAFF_CHAT", data))
    }
}