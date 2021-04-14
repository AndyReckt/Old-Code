/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface ParameterType<T> {

    fun transform(sender: CommandSender, source: String): T

    fun tabComplete(player: Player, flags: Set<String>, source: String): List<String>

}
