/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.data

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 6/2/2020.
 */
class HCFDataLivesUpdateRunnable : Runnable {
    lateinit var player: Player

    override fun run() {
        if (StarkCore.instance.hcfdataFetcher != null) {
            Stark.instance.dataFetcherHandler.refreshLives(player)
        }
    }
}