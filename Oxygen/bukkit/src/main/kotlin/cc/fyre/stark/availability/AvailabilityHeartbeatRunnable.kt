/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.availability

import cc.fyre.stark.Stark
import org.bukkit.Bukkit

class AvailabilityHeartbeatRunnable : Runnable {

    override fun run() {
        Stark.instance.server.onlinePlayers.forEach { player ->
            Stark.instance.core.availabilityHandler.update(player.uniqueId, true, null, Bukkit.getServerId())
        }
    }

}