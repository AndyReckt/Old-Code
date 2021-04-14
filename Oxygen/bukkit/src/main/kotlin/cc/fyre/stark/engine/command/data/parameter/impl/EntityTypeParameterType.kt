/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import cc.fyre.stark.util.EntityUtils
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*

class EntityTypeParameterType : ParameterType<EntityType?> {

    override fun transform(sender: CommandSender, source: String): EntityType? {
        val type = EntityUtils.parse(source)
        if (type == null) {
            for (possibleType in EntityType.values()) {
                if (possibleType.name.equals(source, ignoreCase = true)) {
                    return possibleType
                }

                if (possibleType.typeId.toString().equals(source, ignoreCase = true)) {
                    return possibleType
                }

                if (StringUtils.startsWithIgnoreCase(possibleType.name, source)) {
                    return possibleType
                }
            }
        }

        if (type == null) {
            sender.sendMessage("${ChatColor.RED}No entity type with the name " + source + " found.")
            return null
        }

        return type
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()
        for (mode in EntityType.values()) {
            if (StringUtils.startsWithIgnoreCase(mode.name, source)) {
                completions.add(mode.name)
            }
        }
        return completions
    }

}