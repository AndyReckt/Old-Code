/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.punishment.command.remove

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object UnbanCommand {
    @Command(["unban"], permission = "stark.command.unban", description = "Unban a player", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Flag(value = ["s", "silentMode"], description = "Silently unban the player") silent: Boolean, @Param("target") target: BukkitProfile, @Param("reason", wildcard = true) reason: String) {
        var issuer: UUID? = null
        if (sender is Player) {
            issuer = sender.uniqueId
        }

        val activePunishment = target.getActivePunishment(ProfilePunishmentType.BAN)
        if (activePunishment == null) {
            sender.sendMessage("${target.getPlayerListName()} ${ChatColor.RED}is not banned.")
            return
        }

        activePunishment.removedBy = issuer
        activePunishment.removedAt = System.currentTimeMillis()
        activePunishment.removalReason = reason
        activePunishment.removedServer = Bukkit.getServerId()

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("PUNISHMENT_UPDATE", mapOf("uuid" to target.uuid.toString(), "punishment" to activePunishment.uuid.toString(), "silent" to silent)))
    }
}