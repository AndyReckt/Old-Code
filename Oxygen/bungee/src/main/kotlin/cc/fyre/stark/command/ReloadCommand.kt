/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.command

import cc.fyre.stark.Stark
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command

class ReloadCommand : Command("starkreload", "stark.bungee.reload") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        Stark.instance.reloadConfig()
        sender.sendMessage(*TextComponent.fromLegacyText("${ChatColor.GOLD}Reloaded stark bungee config"))
    }
}