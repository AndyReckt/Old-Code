/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerParameterType : ParameterType<Player?> {

    override fun transform(sender: CommandSender, source: String): Player? {
        if (sender is Player && (source.equals("self", true) || source.isEmpty())) {
            return sender
        }

        val player = Bukkit.getServer().getPlayer(source)

        if (player == null) {
            sender.sendMessage("${ChatColor.RED}No player with the name $source found.")
            return null
        }

        if (sender is Player && !Stark.instance.visibilityEngine.treatAsOnline(player, sender)) {
            return null
        }

        return player
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        Bukkit.getOnlinePlayers().forEach { target ->
            if (Stark.instance.visibilityEngine.treatAsOnline(target, player)) {
                completions.add(target.name)
            }
        }

        return completions
    }

}