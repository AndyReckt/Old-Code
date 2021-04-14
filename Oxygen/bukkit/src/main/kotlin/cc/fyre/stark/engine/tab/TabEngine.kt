/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import cc.fyre.stark.Stark
import net.minecraft.util.com.google.gson.JsonElement
import net.minecraft.util.com.google.gson.JsonParser
import net.minecraft.util.com.mojang.authlib.GameProfile
import net.minecraft.util.com.mojang.authlib.HttpAuthenticationService
import net.minecraft.util.com.mojang.authlib.properties.PropertyMap
import net.minecraft.util.com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.net.Proxy
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

class TabEngine {

    private var propertyMapSerializer: AtomicReference<Any> = AtomicReference()
    private var defaultPropertyMap: AtomicReference<Any> = AtomicReference()
    var layoutProvider: LayoutProvider? = null
    val tabs: ConcurrentHashMap<String, Tab> = ConcurrentHashMap()

    fun load() {
        if (Stark.instance.config.getBoolean("disableTab", false)) {
            Stark.instance.logger.info("Tab is disabled by config")
            return
        }

        getDefaultPropertyMap()

        TabThread().start()
    }

    internal fun addPlayer(player: Player) {
        tabs[player.name] = Tab(player)
    }

    internal fun updatePlayer(player: Player) {
        if (tabs.containsKey(player.name)) {
            tabs[player.name]!!.update()
        }
    }

    internal fun removePlayer(player: Player) {
        tabs.remove(player.name)
    }

    private fun fetchSkin(): PropertyMap? {
        val propertyMap = Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.get("stark:skinPropertyMap")
        }

        if (propertyMap != null && propertyMap.isNotEmpty()) {
            Bukkit.getLogger().info("Using cached PropertyMap for skin...")
            val jsonObject = JsonParser().parse(propertyMap).asJsonArray
            return getPropertyMapSerializer().deserialize(jsonObject as JsonElement, null, null)
        }

        val profile = GameProfile(UUID.fromString("879de836-a7ad-4c27-9a73-87a7238c3358"), "bananasquad")
        val authenticationService = YggdrasilAuthenticationService(Proxy.NO_PROXY, "") as HttpAuthenticationService
        val sessionService = authenticationService.createMinecraftSessionService()
        val profile2 = sessionService.fillProfileProperties(profile, true)
        val localPropertyMap = profile2.properties

        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            Bukkit.getLogger().info("Caching PropertyMap for skin...")
            redis.setex("skinPropertyMap", 3600, getPropertyMapSerializer().serialize(localPropertyMap, null, null).toString())
        }

        return localPropertyMap
    }

    fun getPropertyMapSerializer(): PropertyMap.Serializer {
        var value = propertyMapSerializer.get()
        if (value == null) {
            synchronized(propertyMapSerializer) {
                value = propertyMapSerializer.get()
                if (value == null) {
                    val actualValue = PropertyMap.Serializer()
                    value = actualValue
                    propertyMapSerializer.set(value)
                }
            }
        }
        return (if (value === propertyMapSerializer) null else value) as PropertyMap.Serializer
    }

    fun getDefaultPropertyMap(): PropertyMap {
        var value = defaultPropertyMap.get()
        if (value == null) {
            synchronized(defaultPropertyMap) {
                value = defaultPropertyMap.get()
                if (value == null) {
                    val actualValue = fetchSkin()
                    value = actualValue ?: defaultPropertyMap
                    defaultPropertyMap.set(value)
                }
            }
        }
        return (if (value === defaultPropertyMap) null else value) as PropertyMap
    }
}