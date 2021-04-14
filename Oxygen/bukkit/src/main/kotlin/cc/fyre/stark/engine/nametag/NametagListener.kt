/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.nametag

import cc.fyre.stark.Stark
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue

internal class NametagListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.setMetadata("starkNametag-LoggedIn", FixedMetadataValue(Stark.instance, true) as MetadataValue)
        Stark.instance.nametagEngine.initiatePlayer(event.player)
        Stark.instance.nametagEngine.reloadPlayer(event.player)
        Stark.instance.nametagEngine.reloadOthersFor(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.player.removeMetadata("starkNametag-LoggedIn", Stark.instance)
        Stark.instance.nametagEngine.teamMap.remove(event.player.name)
    }
}