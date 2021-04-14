/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.sign

import cc.fyre.stark.Stark
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.events.PacketListener
import org.bukkit.Bukkit
import org.bukkit.util.Vector

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/2/2020.
 */
class SignAdapter : PacketAdapter(Stark.instance, PacketType.Play.Client.CLIENT_COMMAND, PacketType.Play.Client.UPDATE_SIGN) {

    var packetListener = SignGUI().packetListener

    override fun onPacketReceiving(event: PacketEvent?) {
        val player = event!!.player
        val v: Vector = SignGUI().signLocations.remove(player.name) ?: return
        val list = event.packet.integers.values
        if ((list[0] as Int).toInt() != v.blockX) return
        if ((list[1] as Int).toInt() != v.blockY) return
        if ((list[2] as Int).toInt() != v.blockZ) return
        val lines = event.packet.stringArrays.values[0]
        val response = SignGUI().listeners.remove(event.player.name)
        if (response != null) {
            event.isCancelled = true
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin) { response.onSignDone(player, lines) }.also { packetListener as PacketListener }
        }
    }
}
