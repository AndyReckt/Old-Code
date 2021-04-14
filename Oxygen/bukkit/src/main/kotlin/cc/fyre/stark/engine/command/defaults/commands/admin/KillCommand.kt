/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object KillCommand {
    @Command(["kill"], permission = "op", description = "Kill a player")
    @JvmStatic
    fun kill(sender: Player, @Param(name = "player", defaultValue = "self") player: Player) {
        player.health = 0.0
        if (player == sender) {
            sender.sendMessage("${ChatColor.GOLD}You have been killed.")
        } else {
            sender.sendMessage(player.displayName + "${ChatColor.GOLD} has been killed.")
        }
    }
}