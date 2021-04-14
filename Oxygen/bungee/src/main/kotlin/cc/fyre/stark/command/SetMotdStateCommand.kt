/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.command

import cc.fyre.stark.Stark
import cc.fyre.stark.util.ChatUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command

class SetMotdStateCommand : Command("setmotdstate", "stark.bungee.admin") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            sender.sendMessage(ChatUtil.compile(TextComponent.fromLegacyText("${ChatColor.RED}Usage: /setmotdstate <state>")))
        } else {
            val state = Stark.instance.motdHandler.states[args[0]]

            if (state == null) {
                sender.sendMessage(ChatUtil.compile(TextComponent.fromLegacyText("${ChatColor.RED}A state by that name doesn't exist.")))
            } else {
                Stark.instance.motdHandler.active = state
                sender.sendMessage(ChatUtil.compile(TextComponent.fromLegacyText("${ChatColor.GREEN}You've updated the MOTD state.")))
            }
        }
    }
}