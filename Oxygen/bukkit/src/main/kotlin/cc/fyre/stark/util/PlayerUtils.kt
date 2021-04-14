/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.protocol.InventoryAdapter
import cc.fyre.stark.engine.protocol.PingAdapter
import net.minecraft.server.v1_7_R4.*
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import java.lang.reflect.Field
import java.util.*

object PlayerUtils {

    private var STATUS_PACKET_ID_FIELD: Field = PacketPlayOutEntityStatus::class.java.getDeclaredField("a")
    private var STATUS_PACKET_STATUS_FIELD: Field = PacketPlayOutEntityStatus::class.java.getDeclaredField("b")
    private var SPAWN_PACKET_ID_FIELD: Field = PacketPlayOutNamedEntitySpawn::class.java.getDeclaredField("a")

    init {
        STATUS_PACKET_ID_FIELD.isAccessible = true
        STATUS_PACKET_STATUS_FIELD.isAccessible = true
        SPAWN_PACKET_ID_FIELD.isAccessible = true
    }

    @JvmOverloads
    @JvmStatic
    fun resetInventory(player: Player, gameMode: GameMode? = null) {
        player.health = player.maxHealth
        player.fallDistance = 0.0f
        player.foodLevel = 20
        player.saturation = 10.0f
        player.level = 0
        player.exp = 0.0f

        if (!player.hasMetadata("modmode")) {
            player.inventory.clear()
            player.inventory.armorContents = null
        }

        player.fireTicks = 0

        for (potionEffect in player.activePotionEffects) {
            player.removePotionEffect(potionEffect.type)
        }

        if (gameMode != null && player.gameMode != gameMode) {
            player.gameMode = gameMode
        }
    }

    @JvmStatic
    fun getDamageSource(damager: Entity): Player? {
        var playerDamager: Player? = null
        if (damager is Player) {
            playerDamager = damager
        } else if (damager is Projectile) {
            val projectile = damager
            if (projectile.shooter is Player) {
                playerDamager = projectile.shooter as Player
            }
        }
        return playerDamager
    }

    @JvmStatic
    fun hasOpenInventory(player: Player): Boolean {
        return hasOwnInventoryOpen(player) || hasOtherInventoryOpen(player)
    }

    @JvmStatic
    fun hasOwnInventoryOpen(player: Player): Boolean {
        return InventoryAdapter.currentlyOpen.contains(player.uniqueId)
    }

    @JvmStatic
    fun hasOtherInventoryOpen(player: Player): Boolean {
        return (player as CraftPlayer).handle.activeContainer.windowId != 0
    }

    @JvmStatic
    fun getPing(player: Player): Int {
        return (player as CraftPlayer).handle.ping
    }


    @JvmStatic
    fun isLagging(player: Player): Boolean {
        return !PingAdapter.lastReply.containsKey(player.uniqueId) || MinecraftServer.currentTick - PingAdapter.lastReply[player.uniqueId]!! > 40
    }

    @JvmStatic
    fun animateDeath(player: Player, hideAfter: Boolean) {
        val entityId = EntityUtils.fakeEntityId()
        val spawnPacket = PacketPlayOutNamedEntitySpawn((player as CraftPlayer).handle as EntityHuman)
        val statusPacket = PacketPlayOutEntityStatus()

        try {
            SPAWN_PACKET_ID_FIELD.set(spawnPacket, entityId)
            STATUS_PACKET_ID_FIELD.set(statusPacket, entityId)
            STATUS_PACKET_STATUS_FIELD.set(statusPacket, 3.toByte())

            val radius = MinecraftServer.getServer().playerList.d()
            val sentTo = HashSet<Player>()

            for (entity in player.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                if (entity !is Player) {
                    continue
                }

                if (entity.uniqueId == player.getUniqueId()) {
                    continue
                }

                (entity as CraftPlayer).handle.playerConnection.sendPacket(spawnPacket)
                entity.handle.playerConnection.sendPacket(statusPacket)
                sentTo.add(entity)
            }

            if (hideAfter) {
                Bukkit.getScheduler().runTaskLater(Stark.instance, {
                    for (target in sentTo) {
                        val playerConnection = (target as CraftPlayer).handle.playerConnection
                        val packet = PacketPlayOutEntityDestroy(player.entityId)
                        playerConnection.sendPacket(packet)
                    }
                }, 40L)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun animateDeath(player: Player, watcher: Player, hideAfter: Boolean) {
        val entityId = EntityUtils.fakeEntityId()
        val spawnPacket = PacketPlayOutNamedEntitySpawn((player as CraftPlayer).handle as EntityHuman)
        val statusPacket = PacketPlayOutEntityStatus()

        try {
            SPAWN_PACKET_ID_FIELD.set(spawnPacket, entityId)
            STATUS_PACKET_ID_FIELD.set(statusPacket, entityId)
            STATUS_PACKET_STATUS_FIELD.set(statusPacket, 3.toByte())

            (watcher as CraftPlayer).handle.playerConnection.sendPacket(spawnPacket as Packet)
            watcher.handle.playerConnection.sendPacket(statusPacket as Packet)

            if (hideAfter) {
                Bukkit.getScheduler().runTaskLater(Stark.instance, {
                    val packet = PacketPlayOutEntityDestroy(player.entityId)
                    watcher.handle.playerConnection.sendPacket(packet)
                }, 40L)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}