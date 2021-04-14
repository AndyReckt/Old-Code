/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.listener

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.util.*

class TeleportationListeners : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val player = event.player
        val cause = event.cause

        if (cause.name.contains("PEARL") || cause.name.contains("PORTAL")) {
            return
        }

        if (player.isOp) {
            lastLocation[player.uniqueId] = event.from
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (player.isOp) {
            lastLocation[player.uniqueId] = player.location
        }
    }

    companion object {
        @JvmStatic
        val lastLocation = HashMap<UUID, Location>()
    }
}