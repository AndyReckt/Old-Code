/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import cc.fyre.stark.core.profile.punishment.ProfilePunishment
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.profile.grant.event.GrantCreatedEvent
import cc.fyre.stark.profile.grant.event.GrantRemovedEvent
import com.google.gson.JsonObject
import mkremins.fanciful.FancyMessage
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*

class ProfileMessageListeners : MessageListener {

    @IncomingMessageHandler("GRANT_UPDATE")
    fun onGrantUpdateMessage(json: JsonObject) {
        val uuid = UUID.fromString(json.get("uuid").asString)
        val grantId = UUID.fromString(json.get("grant").asString)
        val player = Bukkit.getPlayer(uuid)

        if (player != null) {
            val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)

            for (grant in profile.grants) {
                if (grant.uuid == grantId) {
                    if (grant.removedAt != null) {
                        Stark.instance.server.pluginManager.callEvent(GrantRemovedEvent(player, grant))
                    } else {
                        Stark.instance.server.pluginManager.callEvent(GrantCreatedEvent(player, grant))
                    }
                }
            }
        }
    }

    @IncomingMessageHandler("PUNISHMENT_UPDATE")
    fun onPunishmentUpdateMessage(json: JsonObject) {
        val uuid = UUID.fromString(json.get("uuid").asString)
        val punishmentId = UUID.fromString(json.get("punishment").asString)
        val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)

        for (punishment in profile.punishments) {
            if (punishment.uuid == punishmentId) {
                executePunishment(profile, punishment, json.get("silent").asBoolean)
            }
        }
    }

    private fun executePunishment(recipient: BukkitProfile, punishment: ProfilePunishment, silent: Boolean) {
        val recipientName = recipient.getPlayerListName()

        val issuerName: String = if (punishment.removedAt != null) {
            if (punishment.removedBy == null) {
                ChatColor.DARK_RED.toString() + "Console"
            } else {
                Stark.instance.core.getProfileHandler().loadProfile(punishment.removedBy!!).getPlayerListName()
            }
        } else {
            if (punishment.issuedBy == null) {
                ChatColor.DARK_RED.toString() + "Console"
            } else {
                Stark.instance.core.getProfileHandler().loadProfile(punishment.issuedBy!!).getPlayerListName()
            }
        }

        val silently = if (silent) ChatColor.YELLOW.toString() + "silently " else ""
        val player = recipient.getPlayer()

        if (punishment.removalReason == null) {
            val context = if (punishment.type == ProfilePunishmentType.BLACKLIST || punishment.type == ProfilePunishmentType.WARN) "" else if (punishment.expiresAt == null) "permanently " else "temporarily "

            val staffMessage = FancyMessage("$recipientName ${ChatColor.GREEN}was $silently${ChatColor.GREEN}$context${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                    .formattedTooltip(listOf(
                            FancyMessage("Issued by: ")
                                    .color(ChatColor.YELLOW)
                                    .then(issuerName),
                            FancyMessage("Reason: ")
                                    .color(ChatColor.YELLOW)
                                    .then(punishment.reason)
                                    .color(ChatColor.RED),
                            FancyMessage("Duration: ")
                                    .color(ChatColor.YELLOW)
                                    .then(if (punishment.expiresAt == null) "Permanent" else punishment.timeLeft())
                                    .color(ChatColor.RED)

                    ))

            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("stark.staff")) {
                    staffMessage.send(onlinePlayer)
                } else if (!silent) {
                    onlinePlayer.sendMessage("$recipientName ${ChatColor.GREEN}was ${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                }
            }

            // kick players
            if (punishment.type !== ProfilePunishmentType.MUTE && punishment.type != ProfilePunishmentType.WARN) {
                // kick player if they're online
                if (player != null) {
                    Stark.instance.server.scheduler.runTask(Stark.instance) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', StringUtils.join(punishment.type.kickMessages, "\n").replace("%time%", punishment.timeLeft())))
                    }
                }

                // kick players sharing ip addresses
                for (it in Stark.instance.server.onlinePlayers) {
                    if (it.uniqueId != recipient.uuid) {
                        if (recipient.ipAddresses.contains(it.address.address.hostAddress)) {
                            Stark.instance.server.scheduler.runTask(Stark.instance) {
                                it.kickPlayer(ChatColor.translateAlternateColorCodes('&', StringUtils.join(punishment.type.kickMessages, "\n").replace("%time%", punishment.timeLeft())))
                            }
                        }
                    }
                }
            }
//
            if (player != null) {
                when (punishment.type) {
                    ProfilePunishmentType.WARN -> {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have been warned for ${punishment.reason}."))
                        Button.playNeutral(player)
                    }

                    ProfilePunishmentType.MUTE -> {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have been muted for ${punishment.reason}. This will punishment will last ${if (punishment.expiresAt == null) "forever" else punishment.timeLeft()}"))
                        Button.playNeutral(player)
                    }
                }
            }

        } else {
            val staffMessage = FancyMessage("$recipientName ${ChatColor.GREEN}was $silently${ChatColor.GREEN}un${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                    .formattedTooltip(listOf(
                            FancyMessage("Removed by: ")
                                    .color(ChatColor.YELLOW)
                                    .then(issuerName),
                            FancyMessage("Reason: ")
                                    .color(ChatColor.YELLOW)
                                    .then(punishment.removalReason)
                                    .color(ChatColor.RED)
                    ))

            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("stark.staff")) {
                    staffMessage.send(onlinePlayer)
                } else if (!silent) {
                    onlinePlayer.sendMessage("$recipientName ${ChatColor.GREEN}was un${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                }
            }
        }
    }

}