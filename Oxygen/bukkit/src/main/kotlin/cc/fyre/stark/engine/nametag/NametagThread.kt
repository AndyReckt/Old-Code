/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.nametag

import cc.fyre.stark.Stark
import java.util.concurrent.ConcurrentHashMap


internal class NametagThread : Thread("stark - Nametag Thread") {
    init {
        this.isDaemon = true
    }

    override fun run() {
        while (true) {
            val pendingUpdatesIterator = pendingUpdates.keys.iterator()
            while (pendingUpdatesIterator.hasNext()) {
                val pendingUpdate = pendingUpdatesIterator.next()
                try {
                    Stark.instance.nametagEngine.applyUpdate(pendingUpdate)
                    pendingUpdatesIterator.remove()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            try {
                sleep(Stark.instance.nametagEngine.updateInterval * 50L)
            } catch (e2: InterruptedException) {
                e2.printStackTrace()
            }
        }
    }

    companion object {
        var pendingUpdates = ConcurrentHashMap<NametagUpdate, Boolean>()
    }
}