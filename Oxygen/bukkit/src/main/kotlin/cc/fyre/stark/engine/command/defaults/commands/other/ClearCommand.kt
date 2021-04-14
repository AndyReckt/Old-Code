/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ClearCommand {
    @Command(["clear", "ci", "clearinv"], permission = "stark.command.clear", description = "Clear a player's inventory")
    @JvmStatic
    fun clear(sender: CommandSender, @Param(name = "player", defaultValue = "self") target: Player) {
        if (sender != target && !sender.hasPermission("stark.command.clear.others")) {
            sender.sendMessage("${ChatColor.RED}No permission to clear other player's inventories.")
            return
        }

        target.inventory.clear()
        target.inventory.armorContents = null

        if (sender != target) {
            sender.sendMessage(target.displayName + "${ChatColor.GOLD}'s inventory has been cleared.")
        } else {
            sender.sendMessage("${ChatColor.GOLD}Your inventory has been cleared.")
        }
    }
}