/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

object RepairCommand {
    @Command(["repair", "fix"], permission = "stark.command.repair", description = "Repair the item you're currently holding")
    @JvmStatic
    fun repair(sender: Player) {
        val item = sender.itemInHand
        if (item == null) {
            sender.sendMessage("${ChatColor.RED}You must be holding an item.")
            return
        }

        if (!Enchantment.DURABILITY.canEnchantItem(item)) {
            sender.sendMessage("${ChatColor.RED}${ItemUtils.getName(item)} cannot be repaired.")
            return
        }

        if (item.durability.toInt() == 0) {
            sender.sendMessage("${ChatColor.RED}That " + ChatColor.WHITE + ItemUtils.getName(item) + ChatColor.RED + " already has max durability.")
            return
        }

        item.durability = 0.toShort()
        sender.sendMessage("${ChatColor.RED}Your " + ChatColor.WHITE + ItemUtils.getName(item) + ChatColor.GOLD + " has been repaired.")
    }
}