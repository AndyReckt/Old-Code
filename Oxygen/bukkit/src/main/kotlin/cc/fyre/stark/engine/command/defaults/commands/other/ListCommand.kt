/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object ListCommand {

    // This code is kinda aids...
    //todo rewrite this better and more efficient
    @Command(["who", "list"])
    @JvmStatic
    fun execute(sender: CommandSender) {
        if (sender.isOp) {
            sender.sendMessage(StringUtils.join(
                    Stark.instance.core.rankHandler.getRanks().sortedBy { it.displayOrder }.map { "${ChatColor.translateAlternateColorCodes('&', it.playerListPrefix)}${it.displayName}" },
                    "${ChatColor.GRAY}, "
            ))
        } else {
            sender.sendMessage(StringUtils.join(
                    Stark.instance.core.rankHandler.getRanks().filter { !it.hidden && !it.displayName.contains("kit", true) }.sortedBy { it.displayOrder }.map { "${ChatColor.translateAlternateColorCodes('&', it.playerListPrefix)}${it.displayName}" },
                    "${ChatColor.GRAY}, "
            ))
        }
        if (sender.hasPermission("stark.staff")) {
            sender.sendMessage("${ChatColor.GRAY}(${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}) [${ChatColor.RESET}${StringUtils.join(Bukkit.getOnlinePlayers().map { Stark.instance.core.getProfileHandler().getByUUID(it.uniqueId) }.sortedBy { it!!.getRank().displayOrder }.map { (if (it!!.getPlayer()!!.hasMetadata("hidden")) "${ChatColor.GRAY}*" else "") + ChatColor.RESET.toString() + it.getPlayerListName() }, "${ChatColor.GRAY}, ")}${ChatColor.GRAY}]")
        } else {
            sender.sendMessage("${ChatColor.GRAY}(${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}) [${ChatColor.RESET}${StringUtils.join(Bukkit.getOnlinePlayers().map { Stark.instance.core.getProfileHandler().getByUUID(it.uniqueId) }.filter { !it!!.getPlayer()!!.hasMetadata("hidden") }.sortedBy { it!!.getRank().displayOrder }.map { ChatColor.RESET.toString() + it!!.getPlayerListName() }, "${ChatColor.GRAY}, ")}${ChatColor.GRAY}]")
        }

    }
}