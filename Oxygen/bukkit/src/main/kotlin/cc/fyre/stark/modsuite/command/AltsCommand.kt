/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object AltsCommand {

    @Command(["alts"], permission = "stark.command.alts", description = "Find a player's alts", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Param("player") target: BukkitProfile) {
        val alts = Stark.instance.core.getProfileHandler().findAlts(target)

        sender.sendMessage("${ChatColor.RED}Found ${ChatColor.YELLOW}${alts.size} ${ChatColor.RED}alt${if (alts.size == 1) "" else "s"} for ${ChatColor.YELLOW}${target.getPlayerListName()}")

        if (alts.isNotEmpty()) {
            val names = alts.map { uuid ->
                val availability = Stark.instance.core.availabilityHandler.fetch(uuid)
                (if (availability.isOnline()) ChatColor.GREEN else ChatColor.GRAY).toString() + Stark.instance.core.uuidCache.name(uuid)
            }.toList()

            sender.sendMessage(names.joinToString("${ChatColor.GRAY}, "))
        }
    }
}