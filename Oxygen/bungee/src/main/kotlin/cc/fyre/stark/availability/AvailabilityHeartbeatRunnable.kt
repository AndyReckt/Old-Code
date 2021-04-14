/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.availability

import cc.fyre.stark.Stark

class AvailabilityHeartbeatRunnable : Runnable {

    override fun run() {
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.proxy.players.forEach { player ->
                Stark.instance.core.availabilityHandler.update(player.uniqueId, true, Stark.instance.proxy.name, null)
            }
        }
    }
}