/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GiveCommand {

    @Command(["give", "i"], permission = "stark.command.give")
    @JvmStatic
    fun execute(sender: CommandSender, @Param("item") item: ItemStack, @Param("amount") amount: Int, @Param("targetPlayer", defaultValue = "self") player: Player) {
        if (player.inventory.firstEmpty() == -1) {
            sender.sendMessage(CC.RED + player.name + "'s inventory is full.")
            return
        }

        item.amount = amount
        player.inventory.addItem(item)
        sender.sendMessage(ChatColor.GOLD.toString() + "Gave " + ChatColor.WHITE + player.name + " " + amount + ChatColor.GOLD + " of " + ChatColor.WHITE + item.type.name.toLowerCase().replace("_", " ") + ChatColor.GOLD + ".")
    }

}