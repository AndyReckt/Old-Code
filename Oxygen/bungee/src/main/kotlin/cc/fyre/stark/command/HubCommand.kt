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
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Command

class HubCommand : Command("hub", null, "lobby") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender is ProxiedPlayer) {
            if (args.isEmpty()) {
                if (Stark.instance.isHubServer(sender.server.info)) {
                    sender.sendMessage(*TextComponent.fromLegacyText("${ChatColor.RED}You're already connected to a hub server."))
                    return
                }

                val hub = Stark.instance.syncHandler.findNextBestHubServer(sender)

                if (hub != null) {
                    sender.connect(hub, ServerConnectEvent.Reason.COMMAND)
                } else {
                    sender.sendMessage(*TextComponent.fromLegacyText("${ChatColor.RED}Couldn't find an open hub server for you to join."))
                }
            }
        }
    }
}