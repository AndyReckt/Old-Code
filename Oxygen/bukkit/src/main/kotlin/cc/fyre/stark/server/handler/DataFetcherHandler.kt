/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.handler

import cc.fyre.stark.core.StarkCore
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 6/2/2020.
 */
class DataFetcherHandler {

    var cachedLives: Int? = null

    fun refreshLives(player: Player) {
        if (Bukkit.isPrimaryThread()) {
            throw RuntimeException("Cannot get Lives on primary thread")
        }

        if (StarkCore.instance.hcfdataFetcher != null) {
            val lives = StarkCore.instance.hcfdataFetcher!!.getLives(player.uniqueId)
            cachedLives = lives
        }
    }
}