/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.availability

import cc.fyre.stark.Stark
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

class AvailabilityListeners : Listener {

    @EventHandler
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        Stark.instance.core.availabilityHandler.update(event.uniqueId, true, null, Bukkit.getServerId())
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        Stark.instance.core.availabilityHandler.update(event.player.uniqueId, false, null, Bukkit.getServerId())
    }

}