/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/1/2020.
 */
object ResetStaffPunishments {
    @JvmStatic
    @Command(names = ["resetstaffpunishments"], permission = "console", async = true)
    fun execute(sender: CommandSender, @Param("target") target: Player) {
        if (sender is ConsoleCommandSender) {
            val documents = Stark.instance.core.mongo.client.getDatabase("stark").getCollection("profiles").find()

            var int = 0
            for (doc in documents) {
                val profile = Stark.instance.core.getProfileHandler().loadProfile(UUID.fromString(doc.getString("uuid")))

                profile.punishments.filter { it.isActive() }.filter { it.issuedBy == target.uniqueId }.forEach {
                    it.removedBy = null
                    it.removalReason = "Rollbacked Staff Punishments #$int"
                    it.removedAt = System.currentTimeMillis()
                    int++
                }

                Stark.instance.core.getProfileHandler().saveProfile(profile)
            }
            sender.sendMessage("$int punishments issued by ${target.displayName} have been removed.")
        } else {
            sender.sendMessage("${ChatColor.RED}This is a console command.")
        }
    }
}