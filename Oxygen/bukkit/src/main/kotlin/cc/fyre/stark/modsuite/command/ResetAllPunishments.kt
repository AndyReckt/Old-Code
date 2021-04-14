/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import java.util.*

object ResetAllPunishments {
    @JvmStatic
    @Command(names = ["resetallpunishment"], permission = "console", async = true)
    fun execute(sender: CommandSender, @Param("remove_blacklists") boolean: Boolean) {
        if (sender is ConsoleCommandSender) {
            val documents = Stark.instance.core.mongo.client.getDatabase("stark").getCollection("profiles").find()

            var int = 0
            for (doc in documents) {
                val profile = Stark.instance.core.getProfileHandler().loadProfile(UUID.fromString(doc.getString("uuid")))

                profile.punishments.filter { it.isActive() }.filter { if (boolean) true else it.type != ProfilePunishmentType.BLACKLIST }.forEach {
                    it.removedBy = null
                    it.removalReason = "Punishment Reset."
                    it.removedAt = System.currentTimeMillis()
                    int++
                }

                Stark.instance.core.getProfileHandler().saveProfile(profile)
            }
            sender.sendMessage("$int punishments have been removed")

        } else {
            sender.sendMessage("${ChatColor.RED}Yeah no fam.")
        }
    }
}