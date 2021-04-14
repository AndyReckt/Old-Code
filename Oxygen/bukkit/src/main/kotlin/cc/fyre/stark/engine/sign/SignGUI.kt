/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.sign

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketListener
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/2/2020.
 */
class SignGUI {
    var protocolManager: ProtocolManager = ProtocolLibrary.getProtocolManager()
    var packetListener: PacketAdapter? = null
    var listeners: MutableMap<String, SignGUIListener> = ConcurrentHashMap()
    var signLocations: MutableMap<String, Vector> = ConcurrentHashMap()


    fun open(player: Player, defaultText: Array<String?>?, response: SignGUIListener) {
        val packets: MutableList<PacketContainer> = ArrayList()
        var x = 0
        val y = 0
        var z = 0
        if (defaultText != null) {
            x = player.location.blockX
            z = player.location.blockZ
            val packet53 = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE)
            packet53.integers.write(0, Integer.valueOf(x)).write(1, Integer.valueOf(y)).write(2, Integer.valueOf(z))
            packet53.blocks.write(0, Material.SIGN_POST)
            packets.add(packet53)
            val packet130 = protocolManager.createPacket(PacketType.Play.Server.UPDATE_SIGN)
            packet130.integers.write(0, Integer.valueOf(x)).write(1, Integer.valueOf(y)).write(2, Integer.valueOf(z))
            packet130.stringArrays.write(0, defaultText)
            packets.add(packet130)
        }
        val packet133 = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_ENTITY)
        packet133.integers.write(0, Integer.valueOf(x)).write(2, Integer.valueOf(z))
        packets.add(packet133)
        if (defaultText != null) {
            val packet53 = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE)
            packet53.integers.write(0, Integer.valueOf(x)).write(1, Integer.valueOf(0)).write(2, Integer.valueOf(z))
            packet53.blocks.write(0, Material.BEDROCK)
            packets.add(packet53)
        }
        try {
            for (packet in packets) protocolManager.sendServerPacket(player, packet)
            signLocations[player.name] = Vector(x, y, z)
            listeners[player.name] = response
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    fun destroy() {
        protocolManager.removePacketListener(packetListener as PacketListener?)
        listeners.clear()
        signLocations.clear()
    }
}