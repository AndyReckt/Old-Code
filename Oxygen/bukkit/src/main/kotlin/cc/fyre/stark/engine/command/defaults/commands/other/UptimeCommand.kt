/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

object UptimeCommand {
    @JvmStatic
    fun uptimeColor(secs: Int): String {
        if (secs <= TimeUnit.HOURS.toSeconds(16L)) {
            return "§a"
        }
        return if (secs <= TimeUnit.HOURS.toSeconds(24L)) {
            "§e"
        } else "§c"
    }

    @Command(["uptime"], description = "Check how long the server has been up for")
    @JvmStatic
    fun uptime(sender: CommandSender) {
        val seconds = ((System.currentTimeMillis() - Stark.instance.enabledAt) / 1000).toInt()
        sender.sendMessage("${ChatColor.GOLD}The server has been running for " + uptimeColor(seconds) + TimeUtils.formatIntoDetailedString(seconds) + ChatColor.GOLD + ".")
    }
}