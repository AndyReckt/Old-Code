/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.protocol

import cc.fyre.stark.Stark
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import net.minecraft.server.v1_7_R4.MinecraftServer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PingAdapter : PacketAdapter(Stark.instance, PacketType.Play.Server.KEEP_ALIVE, PacketType.Play.Client.KEEP_ALIVE), Listener {

    override fun onPacketSending(event: PacketEvent?) {
        val id = event!!.packet.integers.read(0) as Int

        callbacks[event.player.uniqueId] = object : PingCallback(id) {
            override fun call() {
                val ping = (System.currentTimeMillis() - this.sendTime).toInt()
                Companion.ping[event.player.uniqueId] = ping
                lastReply[event.player.uniqueId] = MinecraftServer.currentTick
            }
        }
    }

    override fun onPacketReceiving(event: PacketEvent?) {
        val iterator = callbacks.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.id == event!!.packet.integers.read(0) as Int) {
                entry.value.call()
                iterator.remove()
                break
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        ping.remove(event.player.uniqueId)
        lastReply.remove(event.player.uniqueId)
        callbacks.remove(event.player.uniqueId)
    }

    private abstract class PingCallback
    constructor(val id: Int) {
        val sendTime: Long = System.currentTimeMillis()

        abstract fun call()
    }

    companion object {
        private val callbacks: ConcurrentHashMap<UUID, PingCallback> = ConcurrentHashMap()
        val ping: ConcurrentHashMap<UUID, Int> = ConcurrentHashMap()
        val lastReply: ConcurrentHashMap<UUID, Int> = ConcurrentHashMap()

        fun averagePing(): Int {
            if (ping.isEmpty()) {
                return 0
            }
            var x = 0
            for (p in ping.values) {
                x += p
            }
            return x / ping.size
        }
    }
}