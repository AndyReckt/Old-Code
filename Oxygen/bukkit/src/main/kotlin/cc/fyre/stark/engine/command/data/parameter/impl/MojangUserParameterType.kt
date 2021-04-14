/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.mojanguser.MojangUser
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MojangUserParameterType : ParameterType<MojangUser?> {

    override fun transform(sender: CommandSender, source: String): MojangUser? {
        val mojangUser = Stark.instance.core.getProfileHandler().fetchMojangUser(source)

        if (mojangUser == null) {
            sender.sendMessage("${ChatColor.RED}A player by that name doesn't exist.")
        }

        return mojangUser
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        Bukkit.getOnlinePlayers().forEach { target ->
            if (Stark.instance.visibilityEngine.treatAsOnline(target, sender)) {
                completions.add(target.name)
            }
        }

        return completions
    }

}