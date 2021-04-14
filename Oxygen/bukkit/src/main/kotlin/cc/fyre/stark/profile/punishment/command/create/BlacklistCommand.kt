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
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object BlacklistCommand {

    @Command(["blacklist"], permission = "stark.command.blacklist", description = "Blacklist a player", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Flag(value = ["s", "silentMode"], description = "Silently ban the player") silent: Boolean, @Param("target") target: BukkitProfile, @Param("reason", wildcard = true) reason: String) {
        var issuer: UUID? = null
        if (sender is Player) {
            val senderProfile = Stark.instance.core.getProfileHandler().getByUUID(sender.uniqueId)
            if (senderProfile == null) {
                sender.sendMessage("${ChatColor.RED}Error verifying your target. Try again later.")
                return
            }

            if (target.getRank().displayOrder <= senderProfile.getRank().displayOrder) {
                sender.sendMessage("${ChatColor.RED}You can't blacklist this player.")
                return
            }

            issuer = sender.uniqueId
        }

        if (target.getActivePunishment(ProfilePunishmentType.BLACKLIST) != null) {
            sender.sendMessage("${target.getPlayerListName()} ${ChatColor.RED}is already blacklisted.")
            return
        }

        val punishment = ProfilePunishment()
        punishment.uuid = UUID.randomUUID()
        punishment.type = ProfilePunishmentType.BLACKLIST
        punishment.reason = reason
        punishment.issuedBy = issuer
        punishment.issuedAt = System.currentTimeMillis()
        punishment.duration = "Forever"
        punishment.server = Bukkit.getServerId()

        target.punishments.add(punishment)

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("PUNISHMENT_UPDATE", mapOf("uuid" to target.uuid.toString(), "punishment" to punishment.uuid.toString(), "silent" to silent)))
    }

}