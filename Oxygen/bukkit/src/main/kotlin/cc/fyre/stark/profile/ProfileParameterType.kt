/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class ProfileParameterType : ParameterType<BukkitProfile?> {

    override fun transform(sender: CommandSender, source: String): BukkitProfile? {
        if (source == "self" && sender is Player) {
            return Stark.instance.core.getProfileHandler().getByUUID(sender.uniqueId)
        }

        val profile = Stark.instance.core.getProfileHandler().fetchProfileByUsername(source)

        if (profile == null) {
            sender.sendMessage(ChatColor.RED.toString() + "A player by the name " + source + " couldn't be found.")
            return null
        }

        return profile
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        Bukkit.getOnlinePlayers().forEach { player ->
            if (Stark.instance.visibilityEngine.treatAsOnline(player, sender)) {
                completions.add(player.name)
            }
        }

        return completions
    }

}