/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.punishment.command.create

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.punishment.ProfilePunishment
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object TempMuteCommand {
    @Command(["tempmute"], permission = "stark.command.tempmute", description = "Mute a player", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Flag(value = ["s", "silentMode"], description = "Silently mute the player") silent: Boolean, @Param("target") target: BukkitProfile, @Param("time") time: String, @Param("reason", wildcard = true) reason: String) {
        var issuer: UUID? = null
        if (sender is Player) {
            val senderProfile = Stark.instance.core.getProfileHandler().getByUUID(sender.uniqueId)

            if (senderProfile == null) {
                sender.sendMessage("${ChatColor.RED}Error verifying your target. Try again later.")
                return
            }

            if (target.getRank().displayOrder <= senderProfile.getRank().displayOrder) {
                sender.sendMessage("${ChatColor.RED}You can't mute this player.")
                return
            }

            if (target.hasPermission("stark.punishment.protected")) {
                sender.sendMessage("${ChatColor.RED}User is protected from punishments.")
                return
            }

            issuer = sender.uniqueId
        }

        if (target.getActivePunishment(ProfilePunishmentType.MUTE) != null) {
            sender.sendMessage("${ChatColor.RED}${target.getPlayerListName()} is already muted.")
            return
        }

        val expiresAt: Long?

        try {
            val seconds = if (time.equals("perm", ignoreCase = true) || time.equals("permanent", ignoreCase = true)) -1 else TimeUtils.parseTime(time)

            if (sender is Player && !sender.hasPermission("stark.command.tempmute.unlimited")) {
                if (seconds == -1 || seconds > 7_862_400) {
                    sender.sendMessage("${ChatColor.RED}You don't have permission to create a mute this long. Maximum time allowed: 90 days.")
                }
            }

            expiresAt = if (seconds == -1) null else System.currentTimeMillis() + (seconds * 1_000L)
        } catch (e: Exception) {
            sender.sendMessage("${ChatColor.RED}Time provided is invalid.")
            return
        }

        val punishment = ProfilePunishment()
        punishment.uuid = UUID.randomUUID()
        punishment.type = ProfilePunishmentType.MUTE
        punishment.reason = reason
        punishment.issuedBy = issuer
        punishment.issuedAt = System.currentTimeMillis()
        punishment.expiresAt = expiresAt
        punishment.duration = time
        punishment.server = Bukkit.getServerId()


        target.punishments.add(punishment)

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("PUNISHMENT_UPDATE", mapOf("uuid" to target.uuid.toString(), "punishment" to punishment.uuid.toString(), "silent" to silent)))
    }
}