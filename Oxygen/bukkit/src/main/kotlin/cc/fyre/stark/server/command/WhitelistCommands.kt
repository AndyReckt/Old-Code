/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.whitelist.WhitelistType
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object WhitelistCommands {

    @JvmStatic
    @Command(names = ["whitelist mode"], permission = "stark.whitelist.mode", async = true)
    fun mode(sender: CommandSender, @Param(name = "mode") mode: String) {
        val modeType = when (mode.toLowerCase()) {
            "none" -> WhitelistType.NONE
            "purchased" -> WhitelistType.PURCHASED
            "maintenance" -> WhitelistType.MAINTENANCE
            else -> {
                sender.sendMessage("${ChatColor.RED}Invalid mode.")
                return
            }
        }

        Stark.instance.core.whitelist.setMode(modeType, true)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("WHITELIST_UPDATE", hashMapOf("mode" to modeType.name)))

        sender.sendMessage("${ChatColor.GOLD}Set whitelist mode to ${ChatColor.WHITE}${modeType.displayName}")
    }

    @JvmStatic
    @Command(names = ["whitelist"], permission = "stark.whitelist", async = true)
    fun whitelist(sender: CommandSender, @Param(name = "player") profile: BukkitProfile, @Param(name = "access") access: String) {
        val modeType = when (access.toLowerCase()) {
            "none" -> WhitelistType.NONE
            "purchased" -> WhitelistType.PURCHASED
            "maintenance" -> WhitelistType.MAINTENANCE
            else -> {
                sender.sendMessage("${ChatColor.RED}Invalid mode.")
                return
            }
        }

        Stark.instance.core.whitelist.setWhitelist(profile.uuid, modeType)

        sender.sendMessage("${ChatColor.GOLD}Set ${ChatColor.RESET}${profile.getPlayerListName()}${ChatColor.GOLD}'s whitelist access to ${ChatColor.WHITE}${modeType.displayName}")
    }

    @JvmStatic
    @Command(names = ["whitelist check"], permission = "stark.whitelist", async = true)
    fun read(sender: CommandSender, @Param(name = "player") profile: BukkitProfile) {
        sender.sendMessage("${profile.getPlayerListName()}${ChatColor.GOLD}'s has whitelist access ${ChatColor.WHITE}${Stark.instance.core.whitelist.getWhitelist(profile.uuid).displayName}")
    }

    @JvmStatic
    @Command(names = ["wltoken use"], async = true)
    fun use(player: Player, @Param(name = "player") target: BukkitProfile) {
        if (Stark.instance.core.whitelist.getWhitelist(target.uuid).isAboveOrEqual(WhitelistType.PURCHASED)) {
            player.sendMessage("${ChatColor.RED}That player already has whitelist access.")
            return
        }

        val remaining = Stark.instance.core.whitelist.getWhitelistTokens(player.uniqueId)

        if (remaining <= 0) {
            player.sendMessage("${ChatColor.RED}You have no whitelist tokens to use.")
            return
        }

        Stark.instance.core.whitelist.setWhitelistTokens(player.uniqueId, remaining - 1)
        Stark.instance.core.whitelist.setWhitelist(target.uuid, WhitelistType.PURCHASED)

        player.sendMessage("${ChatColor.GOLD}You've used a whitelist token on ${ChatColor.RESET}${target.getPlayerListName()}")
    }

    @JvmStatic
    @Command(names = ["wltoken give"], permission = "op", async = true)
    fun give(sender: CommandSender, @Param(name = "player") profile: BukkitProfile, @Param(name = "amount") amount: Int) {
        val tokens = Stark.instance.core.whitelist.getWhitelistTokens(profile.uuid) + amount
        Stark.instance.core.whitelist.setWhitelistTokens(profile.uuid, tokens)
        sender.sendMessage("${ChatColor.GOLD}You've given ${ChatColor.RESET}${profile.getPlayerListName()} ${ChatColor.GOLD}$tokens tokens.")
    }

    @JvmStatic
    @Command(names = ["verifiedstatus add"], permission = "op", async = true)
    fun verifiedStatusAdd(sender: CommandSender, @Param(name = "player") profile: BukkitProfile) {
        if (Stark.instance.core.whitelist.setVerified(profile.uuid, true)) {
            sender.sendMessage("${ChatColor.GOLD}You've added verified status to ${ChatColor.RESET}${profile.getPlayerListName()}${ChatColor.GOLD}.")
        } else {
            sender.sendMessage("${profile.getPlayerListName()} ${ChatColor.RED}is already has verified status.")
        }
    }

    @JvmStatic
    @Command(names = ["verifiedstatus remove"], permission = "op", async = true)
    fun verifiedStatusRemove(sender: CommandSender, @Param(name = "player") profile: BukkitProfile) {
        if (Stark.instance.core.whitelist.setVerified(profile.uuid, false)) {
            sender.sendMessage("${ChatColor.GOLD}You've removed verified status from ${ChatColor.RESET}${profile.getPlayerListName()}${ChatColor.GOLD}.")
        } else {
            sender.sendMessage("${profile.getPlayerListName()} ${ChatColor.RED}is not verified status.")
        }
    }

}