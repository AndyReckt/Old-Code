/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.sync

import cc.fyre.stark.Stark
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerKickEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class SyncListeners : Listener {

    @EventHandler
    fun onProxyPingEvent(event: ProxyPingEvent) {
        val serverPing = event.response
        serverPing.players = ServerPing.Players(2020, Stark.instance.syncHandler.getGlobalOnlineCount(), arrayOf())
    }

    @EventHandler
    fun onServerConnectEvent(event: ServerConnectEvent) {
        val player = event.player
        val server = player.server
        val target = event.target

        if ((player.server == null || !Stark.instance.isHubServer(server.info)) && Stark.instance.isHubServer(target) && event.reason != ServerConnectEvent.Reason.COMMAND && event.reason != ServerConnectEvent.Reason.PLUGIN_MESSAGE) {
            val hub = Stark.instance.syncHandler.findNextBestHubServer(player)

            if (hub != null) {
                player.sendMessage(*TextComponent.fromLegacyText("${ChatColor.GOLD}Sending you to ${hub.name}..."))
                event.target = hub
            }
        }
    }

    @EventHandler
    fun onServerKickEvent(event: ServerKickEvent) {
        val player = event.player

        if (!event.kickedFrom.name.contains("hub", ignoreCase = true)) {
            val hub = Stark.instance.syncHandler.findNextBestHubServer(player)
            if (hub != null) {
                player.sendMessage(*TextComponent.fromLegacyText("${ChatColor.GOLD}Sending you to ${hub.name}..."))
                event.isCancelled = true
                event.cancelServer = hub
            }
        }
    }

}