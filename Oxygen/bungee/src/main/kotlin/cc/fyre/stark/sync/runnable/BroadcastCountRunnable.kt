/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.sync.runnable

import cc.fyre.stark.Stark

class BroadcastCountRunnable : Runnable {

    override fun run() {
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.syncHandler.broadcastGlobalOnlineCount()
        }
    }

}