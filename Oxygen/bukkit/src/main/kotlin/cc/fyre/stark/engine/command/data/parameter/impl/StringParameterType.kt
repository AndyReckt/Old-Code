/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StringParameterType : ParameterType<String> {

    override fun transform(sender: CommandSender, source: String): String {
        return source
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return listOf()
    }

}