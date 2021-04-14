/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object RenameCommand {
    private val customNameStarter: String = ChatColor.translateAlternateColorCodes('&', "&b&c&f")

    @Command(["rename"], permission = "stark.command.rename", description = "Rename the item you're currently holding. Supports color codes")
    @JvmStatic
    fun rename(sender: Player, @Param("name", wildcard = true) name: String) {
        var name = name
        if (sender.hasPermission("stark.command.rename.color")) {
            name = ChatColor.translateAlternateColorCodes('&', name)
        }

        val item = sender.itemInHand
        if (item == null) {
            sender.sendMessage("${ChatColor.RED}You must be holding an item.")
            return
        }

        val isCustomEnchant = item.hasItemMeta() && item.itemMeta.hasDisplayName() && item.itemMeta.displayName.startsWith(customNameStarter)
        val meta = item.itemMeta
        meta.displayName = if (isCustomEnchant && !name.startsWith(customNameStarter)) customNameStarter + name else name
        item.itemMeta = meta

        sender.updateInventory()
        sender.sendMessage("${ChatColor.GOLD}Renamed your " + ChatColor.WHITE + ItemUtils.getName(ItemStack(item.type, item.amount, item.durability)) + ChatColor.GOLD + " to " + ChatColor.WHITE + name + ChatColor.GOLD + ".")
    }
}