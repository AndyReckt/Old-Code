/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.stream.Collectors

class WorldParameterType : ParameterType<World?> {

    override fun transform(sender: CommandSender, source: String): World? {
        val world = Bukkit.getWorld(source)

        if (world == null) {
            sender.sendMessage("${ChatColor.RED}No world with the name $source found.")
            return null
        }

        return world
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return Bukkit.getWorlds().stream().map { world -> world.name }.collect(Collectors.toList<String>())
    }

}