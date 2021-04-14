/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import cc.fyre.stark.Stark
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import net.minecraft.server.v1_7_R4.EntityTrackerEntry
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo
import net.minecraft.server.v1_7_R4.WorldServer
import net.minecraft.util.com.mojang.authlib.GameProfile
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.spigotmc.SpigotConfig
import java.lang.reflect.Field
import java.util.*

class TabAdapter : PacketAdapter(Stark.instance, PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {

    override fun onPacketSending(event: PacketEvent) {
        if (Stark.instance.tabEngine.layoutProvider == null || !this.shouldForbid(event.player)) {
            return
        }

        if (event.packetType === PacketType.Play.Server.PLAYER_INFO) {
            val packetContainer = event.packet
            val name = packetContainer.strings.read(0) as String
            val isOurs = name.startsWith("$")
            val action = packetContainer.integers.read(0) as Int

            if (!isOurs && !SpigotConfig.onlyCustomTab) {
                if (action != 4 && this.shouldCancel(event.player, packetContainer)) {
                    event.isCancelled = true
                }
            } else {
                packetContainer.strings.write(0, name.replace("$", ""))
            }
        } else if (event.packetType === PacketType.Play.Server.NAMED_ENTITY_SPAWN && TabUtils.is18(event.player)) {
            val packet = event.packet.handle as PacketPlayOutNamedEntitySpawn
            val gameProfile: GameProfile
            try {
                gameProfile = namedEntitySpawnField[packet] as GameProfile
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }

            val bukkitPlayer = Bukkit.getPlayer(gameProfile.id)
            Bukkit.getScheduler().runTask(Stark.instance) {
                if (bukkitPlayer != null) {
                    (event.player as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutPlayerInfo.removePlayer((bukkitPlayer as CraftPlayer).handle))
                }
            }
        }
    }

    private fun shouldCancel(player: Player, packetContainer: PacketContainer): Boolean {
        if (!TabUtils.is18(player)) {
            return true
        }

        val playerInfoPacket = packetContainer.handle as PacketPlayOutPlayerInfo
        val recipient = (player as CraftPlayer).handle

        val tabPacketPlayer: UUID
        try {
            tabPacketPlayer = (playerField.get(playerInfoPacket) as GameProfile).id
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        val bukkitPlayer = Bukkit.getPlayer(tabPacketPlayer) ?: return true
        val trackerEntry = ((bukkitPlayer as CraftPlayer).handle.getWorld() as WorldServer).getTracker().trackedEntities.get(bukkitPlayer.entityId) as EntityTrackerEntry
        return !trackerEntry.trackedPlayers.contains(recipient)
    }

    private fun shouldForbid(player: Player): Boolean {
        val playerName = player.name
        val playerTab = Stark.instance.tabEngine.tabs[playerName]
        return playerTab != null
    }

    companion object {
        private var playerField: Field = PacketPlayOutPlayerInfo::class.java.getDeclaredField("player")
        private var namedEntitySpawnField: Field = PacketPlayOutNamedEntitySpawn::class.java.getDeclaredField("b")

        init {
            playerField.isAccessible = true
            namedEntitySpawnField.isAccessible = true
        }
    }
}