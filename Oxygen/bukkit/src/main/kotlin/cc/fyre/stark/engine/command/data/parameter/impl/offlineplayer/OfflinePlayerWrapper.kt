/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl.offlineplayer

import cc.fyre.stark.Stark
import cc.fyre.stark.util.Callback
import net.minecraft.server.v1_7_R4.EntityPlayer
import net.minecraft.server.v1_7_R4.PlayerInteractManager
import net.minecraft.server.v1_7_R4.World
import net.minecraft.util.com.mojang.authlib.GameProfile
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_7_R4.CraftServer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class OfflinePlayerWrapper(private var source: String?) {

    var uniqueId: UUID? = null
        private set
    var name: String? = null
        private set

    fun loadAsync(callback: Callback<Player?>) {
        object : BukkitRunnable() {
            override fun run() {
                val player = this@OfflinePlayerWrapper.loadSync()
                object : BukkitRunnable() {
                    override fun run() {
                        callback.callback(player)
                    }
                }.runTask(Stark.instance)
            }
        }.runTaskAsynchronously(Stark.instance)
    }

    fun loadSync(): Player? {
        if ((this.source!![0] == '\"' || this.source!![0] == '\'') && (this.source!![this.source!!.length - 1] == '\"' || this.source!![this.source!!.length - 1] == '\'')) {
            this.source = this.source!!.replace("'", "").replace("\"", "")
            this.uniqueId = Stark.instance.core.uuidCache.uuid(this.source!!)

            if (this.uniqueId == null) {
                this.name = this.source
                return null
            }

            this.name = Stark.instance.core.uuidCache.name(this.uniqueId!!)

            if (Bukkit.getPlayer(this.uniqueId) != null) {
                return Bukkit.getPlayer(this.uniqueId)
            }

            if (!Bukkit.getOfflinePlayer(this.uniqueId).hasPlayedBefore()) {
                return null
            }

            val server = (Bukkit.getServer() as CraftServer).server
            val entity = EntityPlayer(server, server.getWorldServer(0), GameProfile(this.uniqueId, this.name), PlayerInteractManager(server.getWorldServer(0) as World))
            val player = entity.bukkitEntity as Player
            player.loadData()
            return player
        } else {
            if (Bukkit.getPlayer(this.source) != null) {
                return Bukkit.getPlayer(this.source)
            }

            this.uniqueId = Stark.instance.core.uuidCache.uuid(this.source!!)

            if (this.uniqueId == null) {
                this.name = this.source
                return null
            }

            this.name = Stark.instance.core.uuidCache.name(this.uniqueId!!)

            if (Bukkit.getPlayer(this.uniqueId) != null) {
                return Bukkit.getPlayer(this.uniqueId)
            }

            if (!Bukkit.getOfflinePlayer(this.uniqueId).hasPlayedBefore()) {
                return null
            }

            val server = (Bukkit.getServer() as CraftServer).server
            val entity = EntityPlayer(server, server.getWorldServer(0), GameProfile(this.uniqueId, this.name), PlayerInteractManager(server.getWorldServer(0) as World))
            val player = entity.bukkitEntity as Player
            player.loadData()
            return player
        }
    }

}