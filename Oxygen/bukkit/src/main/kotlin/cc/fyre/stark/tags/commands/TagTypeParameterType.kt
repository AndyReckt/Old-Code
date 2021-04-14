/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.tags.commands

import cc.fyre.stark.core.tags.TagType
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
class TagTypeParameterType : ParameterType<TagType?> {

    override fun transform(sender: CommandSender, source: String): TagType? {
        try {
            return TagType.valueOf(source)
        } catch (ex: Exception) {
            sender.sendMessage("${ChatColor.RED}No tag type by the name of $source! Valid Types: ${TagType.values()}")
        }
        return null
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return TagType.values().map(TagType::name).toList()
    }
}