/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl.offlineplayer

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class OfflinePlayerWrapperParameterType : ParameterType<OfflinePlayerWrapper> {

    override fun transform(sender: CommandSender, source: String): OfflinePlayerWrapper {
        return OfflinePlayerWrapper(source)
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