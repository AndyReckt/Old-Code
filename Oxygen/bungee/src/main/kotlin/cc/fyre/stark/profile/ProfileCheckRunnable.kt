/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import java.util.*

class ProfileCheckRunnable : Runnable {

    override fun run() {
        val toRemove = arrayListOf<UUID>()

        Stark.instance.core.getProfileHandler().profiles.keys.forEach {
            if (Stark.instance.proxy.getPlayer(it) == null) {
                toRemove.add(it)
            }
        }

        toRemove.forEach {
            Stark.instance.core.getProfileHandler().profiles.remove(it)
        }

        if (toRemove.isNotEmpty()) {
            Stark.instance.logger.info("Removed " + toRemove.size + " cached profiles")
        }
    }
}