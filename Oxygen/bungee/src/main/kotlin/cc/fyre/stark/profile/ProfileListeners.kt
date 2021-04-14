/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.event.LoginProfileLoadedEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ProfileListeners : Listener {

    @EventHandler(priority = 99)
    fun onLoginEvent(event: LoginEvent) {
        if (!event.isCancelled) {
            val profile = Stark.instance.core.getProfileHandler().loadProfile(event.connection.uniqueId)
            Stark.instance.core.getProfileHandler().profiles[profile.uuid] = profile

            val profileEvent = LoginProfileLoadedEvent(profile)
            Stark.instance.proxy.pluginManager.callEvent(profileEvent)

            if (profileEvent.isCancelled) {
                event.isCancelled = true
                event.setCancelReason(*TextComponent.fromLegacyText(profileEvent.cancelReasons))
            }
        }
    }

    @EventHandler(priority = 0)
    fun onPostLoginEvent(event: PostLoginEvent) {
        Stark.instance.core.getProfileHandler().profiles[event.player.uniqueId]!!.apply()
    }

    @EventHandler(priority = 99)
    fun onPlayerDisconnectEvent(event: PlayerDisconnectEvent) {
        Stark.instance.core.getProfileHandler().profiles.remove(event.player.uniqueId)
    }
}