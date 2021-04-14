/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import cc.fyre.stark.Stark
import net.minecraft.util.com.mojang.authlib.GameProfile
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom

object TabUtils {
    private val cache = ConcurrentHashMap<String, GameProfile>()

    @JvmStatic
    fun is18(player: Player): Boolean {
        return (player as CraftPlayer).handle.playerConnection.networkManager.version > 20
    }

    @JvmStatic
    fun getOrCreateProfile(name: String, id: UUID): GameProfile {
        var player: GameProfile? = cache[name]
        if (player == null) {
            player = GameProfile(id, name)
            player.properties.putAll(Stark.instance.tabEngine.getDefaultPropertyMap())
            cache[name] = player
        }
        return player
    }

    @JvmStatic
    fun getOrCreateProfile(name: String): GameProfile {
        return getOrCreateProfile(name, UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()))
    }
}