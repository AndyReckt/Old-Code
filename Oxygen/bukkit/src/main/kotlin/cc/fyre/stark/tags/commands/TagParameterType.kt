/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.tags.commands

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.tags.Tag
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.streams.toList

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
class TagParameterType : ParameterType<Tag?> {

    override fun transform(sender: CommandSender, source: String): Tag? {
        val tag = StarkCore.instance.tagHandler.getByName(source)
        if (tag == null) {
            sender.sendMessage("${ChatColor.RED}No tag exists by the name of $source.")
        }
        return tag
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return StarkCore.instance.tagHandler.getTags().keys.stream().toList()
    }
}