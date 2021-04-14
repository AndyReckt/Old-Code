/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.listener

import cc.fyre.stark.Stark
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.*

class FrozenServerListeners : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!Stark.instance.serverHandler.frozen) {
            return
        }

        val player = event.player
        if (player.hasPermission("stark.staff")) {
            return
        }

        if (event.from.blockX == event.to.blockX && event.from.blockZ == event.to.blockZ) {
            return
        }

        val newTo = event.from.block.location.clone().add(0.5, 0.0, 0.5)
        newTo.pitch = event.to.pitch
        newTo.yaw = event.to.yaw
        event.to = newTo
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (Stark.instance.serverHandler.frozen) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (!Stark.instance.serverHandler.frozen) {
            return
        }

        if (event.damager is Player) {
            val player = event.damager as Player
            if (player.hasPermission("stark.staff")) {
                return
            }

            event.isCancelled = true
            player.sendMessage(DENY_MESSAGE)
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!Stark.instance.serverHandler.frozen) {
            return
        }

        val player = event.player
        if (player.hasPermission("stark.staff")) {
            return
        }

        event.isCancelled = true
        player.sendMessage(DENY_MESSAGE)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (Stark.instance.serverHandler.frozen) {
            event.player.sendMessage(DENY_MESSAGE)
        }
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (!Stark.instance.serverHandler.frozen) {
            return
        }

        val player = event.player
        if (player.hasPermission("stark.staff") || event.cause != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return
        }

        event.isCancelled = true
        event.to = event.from
        player.sendMessage(DENY_MESSAGE)
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if (!Stark.instance.serverHandler.frozen) {
            return
        }

        val player = event.player
        if (player.hasPermission("stark.staff")) {
            return
        }

        event.isCancelled = true
        player.updateInventory()
        player.sendMessage(DENY_MESSAGE)
    }

    companion object {
        private val DENY_MESSAGE: String = ChatColor.RED.toString() + "The server is currently frozen."
    }

}