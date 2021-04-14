/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.server.chat.ServerChatSettings
import cc.fyre.stark.server.chat.SlowedChatSession
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object SlowChatCommand {
    @Command(["slowchat"], permission = "stark.command.slowchat")
    @JvmStatic
    fun execute(sender: CommandSender, @Param("duration", defaultValue = "0") duration: Int) {
        if (duration > 30) {
            sender.sendMessage("${ChatColor.RED}You can't slow the chat for this long.")
            return
        }

        if (duration == 0) {
            val session = ServerChatSettings.slowed
            if (session == null) {
                sender.sendMessage("${ChatColor.RED}The chat isn't being slowed.")
                return
            } else {
                ServerChatSettings.slowed = null
                Bukkit.broadcastMessage("${ChatColor.LIGHT_PURPLE}Public chat has been unslowed by ${sender.name}.")
            }
            return
        }

        val session = SlowedChatSession(sender.name, duration * 1000L)
        ServerChatSettings.slowed = session
        Bukkit.broadcastMessage("${ChatColor.LIGHT_PURPLE}Public chat has been slowed by ${sender.name}.")
    }
}