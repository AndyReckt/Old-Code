/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl.filter

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.regex.Pattern

open class BaseFilter : ParameterType<String?> {

    protected val bannedPatterns = HashSet<Pattern>()

    override fun transform(sender: CommandSender, source: String): String? {
        for (bannedPattern in this.bannedPatterns) {
            if (bannedPattern.matcher(source).find()) {
                sender.sendMessage("${ChatColor.RED}Command contains inappropriate content.")
                return null
            }
        }

        return source
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return listOf()
    }

}