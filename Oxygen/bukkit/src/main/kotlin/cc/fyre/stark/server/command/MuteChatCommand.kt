/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.server.chat.ServerChatSettings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object MuteChatCommand {
    @Command(["mutechat", "chatmute", "mc"], permission = "stark.command.mutechat")
    @JvmStatic
    fun execute(sender: CommandSender) {
        ServerChatSettings.muted = !ServerChatSettings.muted
        val muted = ServerChatSettings.muted
        Bukkit.broadcastMessage("${ChatColor.LIGHT_PURPLE}Public chat has been ${if (muted) "" else "un"}muted by ${sender.name}.")
    }
}