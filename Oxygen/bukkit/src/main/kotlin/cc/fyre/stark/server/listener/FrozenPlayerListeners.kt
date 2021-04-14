/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.listener

import cc.fyre.stark.Stark
import cc.fyre.stark.messaging.event.PlayerMessageEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*

class FrozenPlayerListeners : Listener {

    companion object {
        private val FROZEN_MESSAGE = "${ChatColor.RED}You cannot do this while frozen."
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerMessage(event: PlayerMessageEvent) {
        if (event.sender.hasMetadata("frozen")) {
            if (!event.target.hasPermission("stark.staff")) {
                event.isCancelled = true
                event.sender.sendMessage(FROZEN_MESSAGE)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            player.sendMessage(FROZEN_MESSAGE)
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerKick(event: PlayerKickEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            player.removeMetadata("frozen", Stark.instance)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            player.removeMetadata("frozen", Stark.instance)
            for (otherPlayer in Bukkit.getOnlinePlayers()) {
                if (otherPlayer.hasPermission("stark.staff")) {
                    otherPlayer.sendMessage("")
                    otherPlayer.sendMessage("${ChatColor.DARK_RED}${ChatColor.BOLD}${player.name} logged out while frozen!")
                    otherPlayer.sendMessage("")
                }
            }
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            val from = event.from
            val to = event.to
            if (from.x != to.x || event.from.z != event.to.z) {
                val newLocation = from.block.location.add(0.5, 0.0, 0.5)
                newLocation.pitch = to.pitch
                newLocation.yaw = to.yaw
                event.to = newLocation
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) {
            return
        }
        val player = event.entity as Player
        if (player.hasMetadata("frozen")) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) {
            return
        }
        if (event.damager.hasMetadata("frozen")) {
            event.isCancelled = true
            (event.damager as Player).sendMessage(FROZEN_MESSAGE)
        }
        if (event.entity is Player && event.entity.hasMetadata("frozen")) {
            (event.damager as Player).sendMessage((event.entity as Player).displayName + ChatColor.RED + " is currently frozen and cannot be damaged.")
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        if (player.hasMetadata("frozen")) {
            event.isCancelled = true
            player.sendMessage(FROZEN_MESSAGE)
        }
    }

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            event.isCancelled = true
            player.updateInventory()
            player.sendMessage(FROZEN_MESSAGE)
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            event.isCancelled = true
            player.updateInventory()
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            event.isCancelled = true
            player.updateInventory()
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) {
            event.isCancelled = true
            player.updateInventory()
        }
    }

}