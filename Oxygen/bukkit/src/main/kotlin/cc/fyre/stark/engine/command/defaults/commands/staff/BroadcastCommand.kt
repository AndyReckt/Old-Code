/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object BroadcastCommand {

    val PREFIX = CC.GRAY + "[" + CC.AQUA + "Broadcast" + CC.GRAY + "] " + CC.WHITE

    @Command(["bc", "broadcast"], "stark.command.broadcast")
    @JvmStatic
    fun execute(sender: CommandSender, @Flag("r") raw: Boolean, @Param("message", wildcard = true) message: String) {
        if (raw) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message.replace("-r", "")))
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + message))
        }
    }
}