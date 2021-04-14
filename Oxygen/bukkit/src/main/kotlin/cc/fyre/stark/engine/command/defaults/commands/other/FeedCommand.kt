/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object FeedCommand {
    @Command(["feed", "eat"], "stark.command.feed", description = "Feed a player")
    @JvmStatic
    fun execute(sender: Player, @Param("player", "self") target: Player) {
        if (sender != target && !sender.isOp) {
            sender.sendMessage("${ChatColor.RED}No permission to feed other players.")
            return
        }

        target.foodLevel = 20
        target.saturation = 10.0f

        if (sender != target) {
            sender.sendMessage(target.displayName + ChatColor.GOLD + " has been fed.")
        }

        target.sendMessage("${ChatColor.GOLD}You have been fed.")
    }
}