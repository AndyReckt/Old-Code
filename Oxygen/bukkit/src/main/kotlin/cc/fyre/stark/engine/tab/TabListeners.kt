/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import cc.fyre.stark.Stark
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

class TabListeners : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        object : BukkitRunnable() {
            override fun run() {
                Stark.instance.tabEngine.addPlayer(event.player)
            }
        }.runTaskLater(Stark.instance, 10L)
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        Stark.instance.tabEngine.removePlayer(event.player)
        TabLayout.remove(event.player)
    }
}