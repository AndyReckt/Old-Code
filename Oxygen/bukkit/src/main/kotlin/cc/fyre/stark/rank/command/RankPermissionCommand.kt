/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

object RankPermissionCommand {

    @Command(["rank permission add"], "op")
    @JvmStatic
    fun addPermission(sender: CommandSender, @Param("rankName") rankName: String, @Param("permissionNode") permission: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
            return
        }

        rank.permissions.add(permission)
        Stark.instance.core.rankHandler.saveRank(rank)
        sender.sendMessage(CC.GOLD + "You have added the permission " + CC.WHITE + permission + CC.GOLD + " to rank " + CC.WHITE + rank.getColoredName() + CC.GOLD + ".")
    }

    @Command(["rank permission remove"], "op")
    @JvmStatic
    fun removePermission(sender: CommandSender, @Param("rankName") rankName: String, @Param("permissionNode") permission: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
            return
        }

        if (rank.permissions.remove(permission)) {
            Stark.instance.core.rankHandler.saveRank(rank)
            sender.sendMessage(CC.GOLD + "You have removed the permission " + CC.WHITE + permission + CC.GOLD + " from rank " + CC.WHITE + rank.getColoredName() + CC.GOLD + ".")
        } else {
            sender.sendMessage(CC.RED + rank.id + " does not have permission " + permission + ".")
        }
    }


    @Command(["rank permission list"], "op")
    @JvmStatic
    fun listPermission(sender: CommandSender, @Param("rankName") rankName: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
            return
        }

        if (rank.permissions.isEmpty()) {
            sender.sendMessage(CC.RED + rank.id + " does not have any permissions.")
        } else {
            sender.sendMessage(CC.RED + CC.BOLD + rank.id + " Permissions")
            for (permission in rank.permissions) {
                sender.sendMessage(CC.RED + "- " + permission)
            }
        }
    }

    @Command(["rank permission clear"], "op")
    @JvmStatic
    fun clearPermission(sender: CommandSender, @Param("rankName") rankName: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)
        if (sender is ConsoleCommandSender) {
            if (rank == null) {
                sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
                return
            }

            if (rank.permissions.isNotEmpty()) {
                for (permissions in rank.permissions) {
                    rank.permissions.remove(permissions)
                }
                sender.sendMessage(CC.GOLD + "You have removed ALL the permissions from rank " + CC.WHITE + rank.getColoredName() + CC.GOLD + ".")
                StarkCore.instance.rankHandler.saveRank(rank)
            } else {
                sender.sendMessage(CC.RED + rank.id + " does not have any permissions.")
            }
        } else {
            sender.sendMessage("${ChatColor.RED}You can only do this from console.")
        }
    }
}