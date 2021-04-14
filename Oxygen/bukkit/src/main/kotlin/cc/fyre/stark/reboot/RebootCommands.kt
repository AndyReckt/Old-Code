/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.reboot

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

object RebootCommands {
    @Command(["reboot"], permission = "op")
    @JvmStatic
    fun execute(sender: CommandSender, @Param(name = "time") unparsedTime: String) {
        try {
            val time = TimeUtils.parseTime(unparsedTime.toLowerCase())
            Stark.instance.rebootHandler.rebootServer(time, TimeUnit.SECONDS)
            sender.sendMessage("${ChatColor.GREEN}Started auto reboot.")
        } catch (ex: Exception) {
            sender.sendMessage(ChatColor.RED.toString() + ex.message)
        }

    }

    @Command(["reboot cancel"], permission = "op")
    @JvmStatic
    fun execute(sender: CommandSender) {
        if (!Stark.instance.rebootHandler.isRebooting()) {
            sender.sendMessage("${ChatColor.RED}No reboot has been scheduled.")
        } else {
            Stark.instance.rebootHandler.cancelReboot()
            Bukkit.getServer().broadcastMessage("${ChatColor.RED}\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0")
            Bukkit.getServer().broadcastMessage("${ChatColor.RED}The server reboot has been cancelled.")
            Bukkit.getServer().broadcastMessage("${ChatColor.RED}\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0")
        }
    }
}