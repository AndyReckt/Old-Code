/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class IntegerParameterType : ParameterType<Int?> {

    override fun transform(sender: CommandSender, value: String): Int? {
        return try {
            Integer.parseInt(value)
        } catch (exception: NumberFormatException) {
            sender.sendMessage("${ChatColor.RED} $value is not a valid number.")
            null
        }
    }

    override fun tabComplete(sender: Player, flags: Set<String>, prefix: String): List<String> {
        return listOf()
    }

}