/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.ItemUtils
import cc.fyre.stark.util.enchantment.EnchantmentWrapper
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

object EnchantCommand {
    @Command(["enchant"], permission = "stark.command.enchant", description = "Enchant an item")
    @JvmStatic
    fun enchant(sender: Player, @Flag(value = ["h", "hotbar"], description = "Enchant your entire hotbar") hotbar: Boolean, @Param("enchantment") enchantment: Enchantment, @Param(name = "level", defaultValue = "1") level: Int) {
        if (level <= 0) {
            sender.sendMessage("${ChatColor.RED}The level must be greater than 0.")
            return
        }

        if (!hotbar) {
            val item = sender.itemInHand
            if (item == null) {
                sender.sendMessage("${ChatColor.RED}You must be holding an item.")
                return
            }

            val wrapper = EnchantmentWrapper.parse(enchantment)
            if (level > wrapper.maxLevel) {
                if (!sender.isOp) {
                    sender.sendMessage("${ChatColor.RED}The maximum enchanting level for " + wrapper.friendlyName + " is " + level + ". You provided " + level + ".")
                    return
                }
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "WARNING: " + ChatColor.YELLOW + "You added " + wrapper.friendlyName + " " + level + " to this item. The default maximum value is " + wrapper.maxLevel + ".")
            }

            wrapper.enchant(item, level)
            sender.updateInventory()
            sender.sendMessage("${ChatColor.GOLD}Enchanted your " + ChatColor.WHITE + ItemUtils.getName(item) + ChatColor.GOLD + " with " + ChatColor.WHITE + wrapper.friendlyName + ChatColor.GOLD + " level " + ChatColor.WHITE + level + ChatColor.GOLD + ".")
        } else {
            val wrapper2 = EnchantmentWrapper.parse(enchantment)
            if (level > wrapper2.maxLevel && !sender.isOp) {
                sender.sendMessage("${ChatColor.RED}The maximum enchanting level for " + wrapper2.friendlyName + " is " + level + ". You provided " + level + ".")
                return
            }

            var enchanted = 0
            for (slot in 0..8) {
                val item2 = sender.inventory.getItem(slot)
                if (item2 != null) {
                    if (wrapper2.canEnchantItem(item2)) {
                        wrapper2.enchant(item2, level)
                        ++enchanted
                    }
                }
            }

            if (enchanted == 0) {
                sender.sendMessage("${ChatColor.RED}No items in your hotbar can be enchanted with " + wrapper2.friendlyName + ".")
                return
            }

            if (level > wrapper2.maxLevel) {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "WARNING: " + ChatColor.YELLOW + "You added " + wrapper2.friendlyName + " " + level + " to these items. The default maximum value is " + wrapper2.maxLevel + ".")
            }

            sender.sendMessage("${ChatColor.GOLD}Enchanted " + ChatColor.WHITE + enchanted + ChatColor.GOLD + " items with " + ChatColor.WHITE + wrapper2.friendlyName + ChatColor.GOLD + " level " + ChatColor.WHITE + level + ChatColor.GOLD + ".")
            sender.updateInventory()
        }
    }
}