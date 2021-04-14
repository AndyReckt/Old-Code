/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.protocol.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ServerLaggedOutEvent(private val averagePing: Int) : Event(true) {

    private val instanceHandlers: HandlerList = handlerList

    override fun getHandlers(): HandlerList {
        return instanceHandlers
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }

}
