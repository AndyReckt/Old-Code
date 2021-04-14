/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.messaging.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ReplyCommand {
    @Command(["reply", "r"], description = "Reply to the player you're in a conversation with", async = true)
    @JvmStatic
    fun reply(sender: Player, @Param(name = "message", defaultValue = "   ", wildcard = true) message: String) {
        val lastMessaged = Stark.instance.messagingManager.getLastMessaged(sender.uniqueId)

        if (message == "   ") {
            if (lastMessaged == null) {
                sender.sendMessage("${ChatColor.RED}You aren't in a conversation.")
            } else {
                sender.sendMessage("${ChatColor.GOLD}You are in a conversation with ${ChatColor.WHITE}${Stark.instance.core.uuidCache.name(lastMessaged)}${ChatColor.GOLD}.")
            }

            return
        }

        if (lastMessaged == null) {
            sender.sendMessage("${ChatColor.RED}You have no one to reply to.")
            return
        }

        val target = Bukkit.getPlayer(lastMessaged)

        if (target == null) {
            sender.sendMessage("${ChatColor.RED}That player has logged out.")
            return
        }

        if (!Stark.instance.messagingManager.canMessage(sender, target)) {
            return
        }

        Stark.instance.messagingManager.sendMessage(sender, target, message)
    }
}