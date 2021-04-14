/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.grant.ProfileGrant
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.grant.menu.GrantMenu
import cc.fyre.stark.util.DateUtil
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object GrantCommand {

   @Command(["grant"], "stark.command.grant", description = "Grant a player a rank", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("target") target: BukkitProfile) {
        GrantMenu(target).openMenu(player)
    }

    @Command(["ogrant"], "op", description = "Grant a player a rank", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Param("target") target: BukkitProfile, @Param("rank") rank: Rank, @Param("scope") scope: String, @Param("duration") duration: String, @Param("reason", wildcard = true) reason: String) {
        if (sender is Player) {
            sender.sendMessage("${ChatColor.RED}This command can only be executed by console.")
            return
        }
        var perm = false
        var expiresAt = 0L
        var issuer: UUID? = null

        if (!(sender.hasPermission("stark.grant" + rank.id))) {
            sender.sendMessage("${ChatColor.RED}You don't have permission to do this.")
            return
        }

        if (sender is Player) {
            issuer = sender.uniqueId
        }

        if (duration.toLowerCase() == "perm") {
            perm = true
        }

        if (!(perm)) {
            try {
                expiresAt = System.currentTimeMillis() - DateUtil.parseDateDiff(duration, false)
            } catch (exception: Exception) {
                sender.sendMessage("${ChatColor.RED}Invalid duration.")
                return
            }
        }

        val grant = ProfileGrant()
        grant.uuid = UUID.randomUUID()
        grant.rank = rank
        grant.reason = reason
        grant.issuedBy = issuer
        grant.issuedAt = System.currentTimeMillis()

        if (!perm) {
            grant.expiresAt = expiresAt + System.currentTimeMillis()
        }

        target.grants.add(grant)

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to target.uuid.toString(), "grant" to grant.uuid.toString())))

        val period = if (grant.expiresAt == null) {
            "forever"
        } else {
            TimeUtils.formatIntoDetailedString(((grant.expiresAt!! - System.currentTimeMillis()) / 1000).toInt())
        }

        sender.sendMessage("${ChatColor.GREEN}You've granted " + target.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}$period${ChatColor.GREEN}.")
    }
}