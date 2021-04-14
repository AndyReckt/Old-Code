/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.boss

import cc.fyre.stark.Stark
import cc.fyre.stark.util.EntityUtils
import com.google.common.base.Preconditions
import com.google.common.collect.Maps
import net.md_5.bungee.api.ChatColor
import net.minecraft.server.v1_7_R4.*
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.beans.ConstructorProperties
import java.lang.reflect.Field
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class BossBarHandler {
    private var initiated = false
    private val displaying: MutableMap<UUID?, BarData?> = Maps.newHashMap()
    private val lastUpdatedPosition: MutableMap<UUID?, Int?> = Maps.newHashMap()

    private var spawnPacketAField: Field? = null
    private var spawnPacketBField: Field? = null
    private var spawnPacketCField: Field? = null
    private var spawnPacketDField: Field? = null
    private var spawnPacketEField: Field? = null
    private var spawnPacketLField: Field? = null
    private var metadataPacketAField: Field? = null
    private var metadataPacketBField: Field? = null
    private var classToIdMap: TObjectIntHashMap<*>? = null
    fun init() {
        Preconditions.checkState(!initiated)
        initiated = true
        Bukkit.getScheduler().runTaskTimer(Stark.instance, {
            val var0: Iterator<*> = displaying.keys.iterator()
            while (var0.hasNext()) {
                val uuid = var0.next() as UUID
                val player = Bukkit.getPlayer(uuid) ?: return@runTaskTimer
                val updateTicks = if ((player as CraftPlayer).handle.playerConnection.networkManager.version != 47) 60 else 3
                if (lastUpdatedPosition.containsKey(player.getUniqueId()) && MinecraftServer.currentTick - lastUpdatedPosition[player.getUniqueId()]!! < updateTicks) {
                    return@runTaskTimer
                }
                updatePosition(player)
                lastUpdatedPosition[player.getUniqueId()] = MinecraftServer.currentTick
            }
        }, 1L, 1L)
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun onPlayerQuit(event: PlayerQuitEvent) {
                removeBossBar(event.player)
            }

            @EventHandler
            fun onPlayerTeleport(event: PlayerTeleportEvent) {
                val player = event.player
                if (displaying.containsKey(player.uniqueId)) {
                    val data = displaying[player.uniqueId]
                    val message = data!!.message
                    val health = data.health
                    removeBossBar(player)
                    setBossBar(player, message, health)
                }
            }
        }, Stark.instance)
    }

    fun setBossBar(player: Player, message: String?, health: Float) {
        var message = message
        try {
            if (message.isNullOrBlank()) {
                removeBossBar(player)
                return
            }
            Preconditions.checkArgument(health in 0.0f..1.0f, "Health must be between 0 and 1")
            if (message.length > 64) {
                message = message.substring(0, 64)
            }
            message = ChatColor.translateAlternateColorCodes('&', message)
            if (!displaying.containsKey(player.uniqueId)) {
                sendSpawnPacket(player, message, health)
            } else {
                sendUpdatePacket(player, message, health)
            }
            displaying[player.uniqueId]!!.message = message
            displaying[player.uniqueId]!!.health = health
        } catch (var4: Exception) {
            var4.printStackTrace()
        }
    }

    fun removeAllBossBar() {
        Bukkit.getOnlinePlayers().filterNotNull().forEach { removeBossBar(it) }
    }

    fun removeBossBar(player: Player) {
        if (displaying.containsKey(player.uniqueId)) {
            val entityId = displaying[player.uniqueId]!!.entityId
            (player as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutEntityDestroy(entityId))
            displaying.remove(player.getUniqueId())
            lastUpdatedPosition.remove(player.getUniqueId())
        }
    }

    fun hasBossBar(player: Player): Boolean = displaying.containsKey(player.uniqueId)

    @Throws(Exception::class)
    private fun sendSpawnPacket(bukkitPlayer: Player, message: String?, health: Float) {
        val player = (bukkitPlayer as CraftPlayer).handle
        val version = player.playerConnection.networkManager.version
        displaying[bukkitPlayer.getUniqueId()] = BarData(EntityUtils.fakeEntityId(), message, health)
        val packet = PacketPlayOutSpawnEntityLiving()
        spawnPacketAField!![packet] = displaying[bukkitPlayer.getUniqueId()]!!.entityId
        val watcher = DataWatcher(null as Entity?)
        if (version != 47) {
            spawnPacketBField!![packet] = EntityType.ENDER_DRAGON.typeId.toByte()
            watcher.a(6, health * 200.0f)
            spawnPacketCField!![packet] = (player.locX * 32.0).toInt()
            spawnPacketDField!![packet] = -6400
            spawnPacketEField!![packet] = (player.locZ * 32.0).toInt()
        } else {
            spawnPacketBField!![packet] = EntityType.WITHER.typeId.toByte()
            watcher.a(6, health * 300.0f)
            watcher.a(20, 880)
            val pitch = Math.toRadians(player.pitch.toDouble())
            val yaw = Math.toRadians(player.yaw.toDouble())
            spawnPacketCField!![packet] = ((player.locX - sin(yaw) * cos(pitch) * 32.0) * 32.0).toInt()
            spawnPacketDField!![packet] = ((player.locY - sin(pitch) * 32.0) * 32.0).toInt()
            spawnPacketEField!![packet] = ((player.locZ + sin(yaw) * cos(pitch) * 32.0) * 32.0).toInt()
        }
        watcher.a(if (version != 47) 10 else 2, message)
        spawnPacketLField!![packet] = watcher
        player.playerConnection.sendPacket(packet)
    }

    @Throws(IllegalAccessException::class)
    private fun sendUpdatePacket(bukkitPlayer: Player, message: String?, health: Float) {
        val player = (bukkitPlayer as CraftPlayer).handle
        val version = player.playerConnection.networkManager.version
        val stored = displaying[bukkitPlayer.getUniqueId()]
        val packet = PacketPlayOutEntityMetadata()
        metadataPacketAField!![packet] = stored!!.entityId
        val objects: MutableList<WatchableObject?> = ArrayList()
        if (health != stored.health) {
            if (version != 47) {
                objects.add(createWatchableObject(6, health * 200.0f))
            } else {
                objects.add(createWatchableObject(6, health * 300.0f))
            }
        }
        if (message != stored.message) {
            objects.add(createWatchableObject(if (version != 47) 10 else 2, message))
        }
        metadataPacketBField!![packet] = objects
        player.playerConnection.sendPacket(packet)
    }

    private fun createWatchableObject(id: Int, `object`: Any?): WatchableObject {
        return WatchableObject(classToIdMap!![`object`!!.javaClass], id, `object`)
    }

    private fun updatePosition(bukkitPlayer: Player) {
        if (displaying.containsKey(bukkitPlayer.uniqueId)) {
            val player = (bukkitPlayer as CraftPlayer).handle
            val version = player.playerConnection.networkManager.version
            val x: Int
            val y: Int
            val z: Int
            if (version != 47) {
                x = (player.locX * 32.0).toInt()
                y = -6400
                z = (player.locZ * 32.0).toInt()
            } else {
                val pitch = Math.toRadians(player.pitch.toDouble())
                val yaw = Math.toRadians(player.yaw.toDouble())
                x = ((player.locX - sin(yaw) * cos(pitch) * 32.0) * 32.0).toInt()
                y = ((player.locY - sin(pitch) * 32.0) * 32.0).toInt()
                z = ((player.locZ + cos(yaw) * cos(pitch) * 32.0) * 32.0).toInt()
            }
            player.playerConnection.sendPacket(PacketPlayOutEntityTeleport(displaying[bukkitPlayer.getUniqueId()]!!.entityId, x, y, z, 0.toByte(), 0.toByte()))
        }
    }

    private class BarData @ConstructorProperties("entityId", "message", "health") constructor(val entityId: Int, var message: String?, var health: Float)

    init {
        try {
            spawnPacketAField = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("a")
            spawnPacketAField!!.isAccessible = true
            spawnPacketBField = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("b")
            spawnPacketBField!!.isAccessible = true
            spawnPacketCField = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("c")
            spawnPacketCField!!.isAccessible = true
            spawnPacketDField = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("d")
            spawnPacketDField!!.isAccessible = true
            spawnPacketEField = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("e")
            spawnPacketEField!!.isAccessible = true
            spawnPacketLField = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("l")
            spawnPacketLField!!.isAccessible = true
            metadataPacketAField = PacketPlayOutEntityMetadata::class.java.getDeclaredField("a")
            metadataPacketAField!!.isAccessible = true
            metadataPacketBField = PacketPlayOutEntityMetadata::class.java.getDeclaredField("b")
            metadataPacketBField!!.isAccessible = true
            val dataWatcherClassToIdField = DataWatcher::class.java.getDeclaredField("classToId")
            dataWatcherClassToIdField.isAccessible = true
            classToIdMap = dataWatcherClassToIdField[null] as TObjectIntHashMap<*>
        } catch (var1: Exception) {
            var1.printStackTrace()
        }
    }
}