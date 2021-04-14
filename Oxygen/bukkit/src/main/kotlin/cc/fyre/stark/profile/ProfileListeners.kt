/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.core.tags.TagType
import cc.fyre.stark.modsuite.options.ModOptionsHandler
import cc.fyre.stark.util.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*

class ProfileListeners : Listener {

    @EventHandler
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        if (System.currentTimeMillis() - 1000 < Stark.instance.enabledAt) {
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            event.kickMessage = "${ChatColor.RED}This server is still loading..."
            return
        }

        // Need to check if player is still logged in when receiving another login attempt
        // This happens when a player using a custom client can access the server list while in-game (and reconnecting)
        val player = Bukkit.getPlayer(event.uniqueId)
        if (player != null && player.isOnline) {
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            event.kickMessage = "${ChatColor.RED}You tried to login too quickly after disconnecting.\nTry again in a few seconds."

            Stark.instance.server.scheduler.runTask(Stark.instance) {
                player.kickPlayer("${ChatColor.RED}Duplicate login kick")
            }

            return
        }

        // Search for an active ban
        //   - by uuid
        //   - by current ip address
        try {
            val punishment = Stark.instance.core.getProfileHandler().findActivePunishment(event.uniqueId, event.address.hostAddress)

            if (punishment != null) {
                var kickMessage = if (punishment.second.type == ProfilePunishmentType.BAN) {
                    //ProfilePunishmentType.BAN.kickMessages.toString()
                    "${ChatColor.RED}Your account has been banned from ${ChatColor.GOLD}Vyrix Network${ChatColor.RED}.\nJoin ts.vyrix.us to appeal.\nThis punishment duration is ${punishment.second.timeLeft()}"
                } else {
                    //ProfilePunishmentType.BLACKLIST.kickMessages.toString()
                    "${ChatColor.RED}Your account has been blacklisted from ${ChatColor.GOLD}Vyrix Network${ChatColor.RED}.\nThis type of punishment can't be appealed."
                }

                if (event.uniqueId != punishment.first) {
                    kickMessage += "\nThis punishment is in relation to ${ChatColor.YELLOW}${Stark.instance.core.uuidCache.name(punishment.first)}${ChatColor.RED}."
                }

                // Make it so players can join hubs while banned
                if (!Stark.instance.server.serverName.contains("punishments-hub", true)) {
                    event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
                    event.kickMessage = kickMessage
                }
            }
        } catch (e: Exception) {
            Stark.instance.logger.info("Failed to search for active punishment for " + event.name + ":")
            e.printStackTrace()
            return
        }

        try {
            val profile = Stark.instance.core.getProfileHandler().loadProfile(event.uniqueId, event.address.hostAddress)
            Stark.instance.core.getProfileHandler().profiles[profile.uuid] = profile
        } catch (e: Exception) {
            Stark.instance.logger.info("Failed to load " + event.name + "'s target:")
            e.printStackTrace()
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            event.kickMessage = ChatColor.RED.toString() + "Failed to load your target.\nTry reconnecting later!"
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncPlayerChatEventLow(event: AsyncPlayerChatEvent) {
        val player = event.player
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!

        if (!event.isCancelled && profile.getActivePunishment(ProfilePunishmentType.MUTE) != null) {
            player.sendMessage("${ChatColor.RED}You're currently muted for `${profile.getActivePunishment(ProfilePunishmentType.MUTE)!!.reason}`, this will last ${profile.getActivePunishment(ProfilePunishmentType.MUTE)!!.timeLeft()}")
            event.isCancelled = true
            return
        }

        val rank = profile.getRank()
        val prefix = rank.prefix
        val tag = profile.tag
        var tagString = ""

        if (tag != null) {
            tagString = tag.display
            when (tag.tagType) {
                TagType.BEFORE -> {
                    event.format = ChatColor.translateAlternateColorCodes('&', "$tagString $prefix%s${ChatColor.RESET}: %s")
                    return
                }
                TagType.AFTER -> {
                    event.format = ChatColor.translateAlternateColorCodes('&', "$prefix%s $tagString${ChatColor.RESET}: %s")
                    return
                }
            }
        }

        if (rank.default) {
            event.format = ChatColor.translateAlternateColorCodes('&', "$prefix%s${ChatColor.RESET}: " + CC.GRAY + "%s")
        } else {
            event.format = ChatColor.translateAlternateColorCodes('&', "$prefix%s${ChatColor.RESET}: %s")
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        event.joinMessage = null

        val player = event.player
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)

        profile!!.apply()

        if (!event.player.location.chunk.isLoaded) {
            event.player.location.chunk.load()
        }

        for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
            val profile = Stark.instance.core.getProfileHandler().getByUUID(onlinePlayer.uniqueId)
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        event.quitMessage = null

        val profile = Stark.instance.core.getProfileHandler().getByUUID(event.player.uniqueId)

        if (profile != null) {
            if (profile.attachment != null) {
                event.player.removeAttachment(profile.attachment)
            }
        }

        Stark.instance.core.getProfileHandler().profiles.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        if (!event.to.chunk.isLoaded) {
            event.to.chunk.load()
        }
    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        if (!event.message.contains("register") && isBanned(event.player) && Stark.instance.server.serverName.contains("punishments-hub", true)) {
            event.isCancelled = true
            event.player.sendMessage(CC.CHAT_BAR)
            event.player.sendMessage(CC.RED + "You are currently banned!")
            event.player.sendMessage(CC.RED + "You are only able to use /register to appeal your ban.")
            event.player.sendMessage(CC.CHAT_BAR)
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (isBanned(event.whoClicked as Player) && Stark.instance.server.serverName.contains("punishments-hub", true)) {
            event.isCancelled = true
            (event.whoClicked as Player).sendMessage(CC.CHAT_BAR)
            (event.whoClicked as Player).sendMessage(CC.RED + "You are currently banned!")
            (event.whoClicked as Player).sendMessage(CC.RED + "You are only able to use /register to appeal your ban.")
            (event.whoClicked as Player).sendMessage(CC.CHAT_BAR)
        }
    }

    fun isBanned(player: Player): Boolean {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)
        return (profile?.getActivePunishment(ProfilePunishmentType.BAN) != null && profile.getActivePunishment(ProfilePunishmentType.BAN)!!.isActive())
    }

}