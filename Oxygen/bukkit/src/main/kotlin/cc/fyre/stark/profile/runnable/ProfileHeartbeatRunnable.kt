/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.runnable

import cc.fyre.stark.Stark

class ProfileHeartbeatRunnable : Runnable {

    override fun run() {
        // Heartbeat profiles every 30 seconds
        for (player in Stark.instance.server.onlinePlayers) {
            val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(player.uniqueId)
            Stark.instance.core.getProfileHandler().profiles[player.uniqueId] = profile
        }
    }

}