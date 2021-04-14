/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.reboot

import cc.fyre.stark.Stark
import cc.fyre.stark.util.event.HourEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.concurrent.TimeUnit

class RebootListener : Listener {

    @EventHandler
    fun onHour(event: HourEvent) {
        if (Stark.instance.rebootHandler.rebootTimes.contains(event.hour)) {
            Stark.instance.rebootHandler.rebootServer(5, TimeUnit.MINUTES)
        }
    }

/*    @EventHandler
    fun onServerShutdown(event: ServerShutdownEvent) {
        for (player in Bukkit.getServer().onlinePlayers) {
            player.kickPlayer("Shutting down")
        }
    }*/
}