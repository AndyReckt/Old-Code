/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.messaging.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object MessageCommand {

    var PREFIX = ChatColor.translateAlternateColorCodes('&', "&9&l[Filter] &b")

    @Command(["message", "msg", "m", "whisper", "w", "tell", "t"], description = "Send a player a private message", async = true)
    @JvmStatic
    fun message(sender: Player, @Param(name = "player") target: Player, @Param(name = "message", wildcard = true) message: String) {
        if (sender.uniqueId == target.uniqueId) {
            sender.sendMessage("${ChatColor.RED}You can't message yourself.")
            return
        }

        if (!Stark.instance.messagingManager.canMessage(sender, target)) {
            return
        }

        if (Stark.instance.filter.isFiltered(message)) {
            if (sender.hasPermission("stark.staff")) {
                sender.sendMessage(CC.RED + "That message would have been filtered!")
                return
            }

            sender.sendMessage("${ChatColor.RED}Your message has been filtered!")
            for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
                if (onlinePlayer.hasPermission("stark.staff")) {
                    onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + "(" + sender.name + " -> " + target.name + ") &7") + message)
                }
            }
            return
        }

        Stark.instance.messagingManager.sendMessage(sender, target, message)
    }
}