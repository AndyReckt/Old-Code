/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.engine.protocol.PingAdapter
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object PingCommand {
    @JvmStatic
    @Command(names = ["ping"], permission = "", async = true)
    fun execute(sender: CommandSender, @Param("target", "self") target: BukkitProfile) {
        if (target.getPlayer() != null) {
            if (sender is Player) {
                if (sender == target.getPlayer()) {
                    sender.sendMessage("${ChatColor.GOLD}Your ping is ${getFormatPing(target.uuid)}${ChatColor.GOLD}ms.")
                } else {
                    sender.sendMessage("${ChatColor.GOLD}${target.getDisplayName()}${ChatColor.GOLD}'s ping is ${getFormatPing(target.uuid)}${ChatColor.GOLD}ms.")
                }
            } else {
                sender.sendMessage("${ChatColor.GOLD}${target.getDisplayName()}${ChatColor.GOLD}'s ping is ${getFormatPing(target.uuid)}${ChatColor.GOLD}ms.")
            }

        } else {
            sender.sendMessage("${ChatColor.RED}${target.getPlayerListName()}${ChatColor.RED} is not online!")
        }
    }

    private fun getFormatPing(uuid: UUID): String {
        return (if (getPing(uuid) > 150) CC.RED + getPing(uuid) else if (getPing(uuid) > 100) CC.YELLOW + getPing(uuid) else CC.GREEN + getPing(uuid)) + CC.RESET
    }

    private fun getPing(uuid: UUID): Int {
        return if (PingAdapter.ping[uuid] != null) {
            PingAdapter.ping[uuid]!!
        } else {
            0
        }
    }
}