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

class FloatParameterType : ParameterType<Float?> {

    override fun transform(sender: CommandSender, value: String): Float? {
        if (value.toLowerCase().contains("e")) {
            sender.sendMessage(ChatColor.RED.toString() + value + " is not a valid number.")
            return null
        }

        try {
            val parsed = java.lang.Float.parseFloat(value)

            if (java.lang.Float.isNaN(parsed) || !java.lang.Float.isFinite(parsed)) {
                sender.sendMessage(ChatColor.RED.toString() + value + " is not a valid number.")
                return null
            }

            return parsed
        } catch (exception: NumberFormatException) {
            sender.sendMessage(ChatColor.RED.toString() + value + " is not a valid number.")
            return null
        }
    }

    override fun tabComplete(sender: Player, flags: Set<String>, prefix: String): List<String> {
        return listOf()
    }

}