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

object MoreCommand {
    @Command(["more"], permission = "stark.command.more", description = "Give yourself more of the item you're holding")
    @JvmStatic
    fun more(sender: Player, @Param(name = "amount", defaultValue = "42069420") amount: Int) {
        if (sender.itemInHand == null) {
            sender.sendMessage("${ChatColor.RED}You must be holding an item.")
            return
        }

        if (amount == 42069420) {
            sender.itemInHand.amount = 64
        } else {
            sender.itemInHand.amount = Math.min(64, sender.itemInHand.amount + amount)
        }

        sender.sendMessage("${ChatColor.GOLD}The item in your hand has been duplicated.")
    }
}