/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SudoCommands {
    @Command(["sudo"], permission = "op", description = "Force a player to perform a command")
    @JvmStatic
    fun sudo(sender: CommandSender, @Param(name = "player") target: Player, @Param(name = "command", wildcard = true) command: String) {
        target.chat("/$command")
        sender.sendMessage("${ChatColor.GOLD}Forced ${ChatColor.WHITE}${target.displayName}${ChatColor.GOLD} to run ${ChatColor.WHITE}'/$command'${ChatColor.GOLD}.")
    }

    @Command(["sudoall"], permission = "op", description = "Force all players to perform a command")
    @JvmStatic
    fun sudoAll(sender: CommandSender, @Param(name = "command", wildcard = true) command: String) {
        Bukkit.getOnlinePlayers().forEach { it.chat("/$command") }
        sender.sendMessage("${ChatColor.GOLD}Forced all players to run ${ChatColor.WHITE}'/$command'${ChatColor.GOLD}.")
    }

    @Command(["forcechat"], permission = "op", description = "Force a player to chat")
    @JvmStatic
    fun chat(sender: CommandSender, @Param(name = "player") target: Player, @Param(name = "message", wildcard = true) message: String) {
        target.chat(message)
        sender.sendMessage("${ChatColor.GOLD}Forced ${ChatColor.WHITE}${target.displayName}${ChatColor.GOLD} to chat ${ChatColor.WHITE}'$message'${ChatColor.GOLD}.")
    }

    @Command(["forcechatall"], permission = "op", description = "Force all players to chat")
    @JvmStatic
    fun chatAll(sender: CommandSender, @Param(name = "message", wildcard = true) message: String) {
        Bukkit.getOnlinePlayers().forEach { it.chat(message) }
        sender.sendMessage("${ChatColor.GOLD}Forced all players to chat ${ChatColor.WHITE}'$message'${ChatColor.GOLD}.")
    }
}