/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import cc.fyre.stark.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemStackParameterType : ParameterType<ItemStack?> {

    override fun transform(sender: CommandSender, source: String): ItemStack? {
        val item = ItemUtils[source]

        if (item == null) {
            sender.sendMessage("${ChatColor.RED}No item with the name $source found.")
            return null
        }

        return item
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return listOf()
    }

}