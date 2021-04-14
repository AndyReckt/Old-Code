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

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2020.
 */
object RawCommand {
    @Command(["raw"], "stark.command.broadcast")
    @JvmStatic
    fun execute(sender: CommandSender, @Param("message", wildcard = true) message: String) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message))
    }
}