/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class OfflinePlayerParameterType : ParameterType<OfflinePlayer> {

    override fun transform(sender: CommandSender, source: String): OfflinePlayer {
        return if (sender is Player && (source.equals("self", ignoreCase = true) || source == "")) {
            sender
        } else Stark.instance.server.getOfflinePlayer(source)
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        for (player in Bukkit.getOnlinePlayers()) {
            if (!Stark.instance.visibilityEngine.treatAsOnline(player, sender)) {
                continue
            }

            completions.add(player.name)
        }

        return completions
    }

}