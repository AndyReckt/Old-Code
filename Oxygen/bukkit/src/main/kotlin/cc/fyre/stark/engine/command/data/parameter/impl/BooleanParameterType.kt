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
import java.util.*

class BooleanParameterType : ParameterType<Boolean?> {

    private val map: MutableMap<String, Boolean> = mutableMapOf()

    init {
        map["true"] = true
        map["on"] = true
        map["yes"] = true
        map["false"] = false
        map["off"] = false
        map["no"] = false
    }

    override fun transform(sender: CommandSender, source: String): Boolean? {
        if (!this.map.containsKey(source.toLowerCase())) {
            sender.sendMessage("${ChatColor.RED} $source is not a valid boolean.")
            return null
        }

        return this.map[source.toLowerCase()]
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        return ArrayList(this.map.keys)
    }

}