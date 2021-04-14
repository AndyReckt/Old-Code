/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.visibility

import cc.fyre.stark.Stark
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatTabCompleteEvent
import org.bukkit.event.player.PlayerJoinEvent

class VisibilityListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        Stark.instance.visibilityEngine.update(event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onTabComplete(event: PlayerChatTabCompleteEvent) {
        val token = event.lastToken
        val completions = event.tabCompletions
        completions.clear()

        for (target in Bukkit.getOnlinePlayers()) {
            if (!Stark.instance.visibilityEngine.treatAsOnline(target, event.player)) {
                continue
            }

            if (!StringUtils.startsWithIgnoreCase(target.name, token)) {
                continue
            }

            completions.add(target.name)
        }
    }

}