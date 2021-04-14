/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.whitelist

import cc.fyre.stark.Stark
import cc.fyre.stark.core.whitelist.WhitelistType
import cc.fyre.stark.event.LoginProfileLoadedEvent
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class WhitelistListeners : Listener {

    @EventHandler
    fun onProxyPingEvent(event: ProxyPingEvent) {
        val serverPing = event.response

        if (Stark.instance.core.whitelist.modeType == WhitelistType.MAINTENANCE) {
            serverPing.version = ServerPing.Protocol("Maintenance", 1337)
        }
    }

    @EventHandler
    fun onLoginProfileLoadedEvent(event: LoginProfileLoadedEvent) {
        val modeType = Stark.instance.core.whitelist.modeType

        val hasWhitelist = Stark.instance.core.whitelist.getWhitelist(event.profile.uuid).isAboveOrEqual(modeType)
        val hasPermission = event.profile.getCompoundedPermissions().contains(modeType.getPermission())

        if (!hasWhitelist && !hasPermission) {
            event.isCancelled = true
            event.cancelReasons = ChatColor.GOLD.toString() + modeType.disallowMessage
        }
    }

    @EventHandler
    fun onPostLoginEvent(event: PostLoginEvent) {
        if (Stark.instance.core.whitelist.modeType == WhitelistType.MAINTENANCE) {
            event.player.sendMessage(*TextComponent.fromLegacyText("${ChatColor.GOLD}The network is in maintenance mode, but you were able to join because of your rank."))
        }
    }
}