/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.sync.runnable

import cc.fyre.stark.Stark

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/2/2020.
 */
class RankSyncRunnable : Runnable {

    override fun run() { // Force the proxy to accept rank updates
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.core.rankHandler.loadRanks()
        }
    }
}