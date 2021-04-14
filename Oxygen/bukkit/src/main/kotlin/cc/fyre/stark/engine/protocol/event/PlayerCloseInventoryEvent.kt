/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.protocol.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class PlayerCloseInventoryEvent(player: Player) : PlayerEvent(player) {

    private val instanceHandlers: HandlerList = ServerLaggedOutEvent.handlerList

    override fun getHandlers(): HandlerList {
        return instanceHandlers
    }

    companion object {
        var handlerList: HandlerList = HandlerList()
    }

}