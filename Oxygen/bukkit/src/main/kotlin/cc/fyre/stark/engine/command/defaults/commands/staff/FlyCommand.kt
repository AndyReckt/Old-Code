/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object FlyCommand {
    @Command(["fly"], permission = "stark.command.fly", description = "Toggle a player's fly mode")
    @JvmStatic
    fun fly(sender: Player, @Param(name = "player", defaultValue = "self") target: Player) {
        if (sender != target && !sender.isOp) {
            sender.sendMessage("${ChatColor.RED}No permission to set other player's fly mode.")
            return
        }

        target.allowFlight = !target.allowFlight

        if (sender != target) {
            sender.sendMessage(target.displayName + "${ChatColor.GOLD}'s fly mode was set to ${ChatColor.WHITE}" + target.allowFlight + "${ChatColor.GOLD}.")
            return
        }

        target.sendMessage("${ChatColor.GOLD}Your fly mode was set to ${ChatColor.WHITE}" + target.allowFlight + "${ChatColor.GOLD}.")
    }
}