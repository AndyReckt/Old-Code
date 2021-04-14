/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.messaging.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object SpyCommand {
    @Command(["spy"], permission = "stark.command.spy", description = "Toggle global private message spying")
    @JvmStatic
    fun spy(sender: Player) {
        val spying = Stark.instance.messagingManager.globalSpy.contains(sender.uniqueId)

        if (spying) {
            Stark.instance.messagingManager.globalSpy.remove(sender.uniqueId)
            sender.sendMessage("${ChatColor.GOLD}No longer spying on private messages...")
        } else {
            Stark.instance.messagingManager.globalSpy.add(sender.uniqueId)
            sender.sendMessage("${ChatColor.GOLD}Now spying on private messages...")
        }
    }
}