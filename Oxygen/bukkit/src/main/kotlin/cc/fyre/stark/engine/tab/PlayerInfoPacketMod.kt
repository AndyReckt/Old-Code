/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo
import net.minecraft.util.com.mojang.authlib.GameProfile
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player

class PlayerInfoPacketMod(name: String, ping: Int, profile: GameProfile, action: Int) {

    private val packet: PacketPlayOutPlayerInfo = PacketPlayOutPlayerInfo()

    init {
        setField("username", name)
        setField("ping", Integer.valueOf(ping))
        setField("action", Integer.valueOf(action))
        setField("player", profile)
    }

    fun setField(field: String, value: Any?) {
        try {
            val fieldObject = this.packet.javaClass.getDeclaredField(field)
            fieldObject.isAccessible = true
            fieldObject.set(this.packet, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun sendToPlayer(player: Player) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

}