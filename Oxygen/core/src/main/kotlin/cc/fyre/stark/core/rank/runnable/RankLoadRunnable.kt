/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.rank.runnable

import cc.fyre.stark.core.StarkCore

class RankLoadRunnable : Runnable {

    override fun run() {
        StarkCore.instance.rankHandler.loadRanks()
    }

}