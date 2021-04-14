/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.scoreboard

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player

class ScoreboardTeamPacketMod(val name: String, val prefix: String, val suffix: String, val players: MutableCollection<String>?, val paramInt: Int) {

    private val packet = PacketPlayOutScoreboardTeam()

    init {
        try {
            aField.set(packet, name)
            fField.set(packet, paramInt)

            if (paramInt == 0 || paramInt == 2) {
                bField.set(packet, name)
                cField.set(packet, prefix)
                dField.set(packet, suffix)
                gField.set(packet, 3)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (paramInt == 0) {
            addAll(players)
        }
    }

    constructor(name: String, players: MutableCollection<String>, paramInt: Int) : this(name, "", "", players, paramInt) {
        try {
            gField.set(packet, 3)
            aField.set(packet, name)
            fField.set(packet, paramInt)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        addAll(players)
    }

    fun sendToPlayer(bukkitPlayer: Player) {
        (bukkitPlayer as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    private fun addAll(col: MutableCollection<String>?) {
        if (col == null) {
            return
        }

        try {
            (eField.get(packet) as MutableCollection<String>).addAll(col)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val aField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("a")
        private val bField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("b")
        private val cField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("c")
        private val dField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("d")
        private val eField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("e")
        private val fField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("f")
        private val gField = PacketPlayOutScoreboardTeam::class.java.getDeclaredField("g")

        init {
            aField.isAccessible = true
            bField.isAccessible = true
            cField.isAccessible = true
            dField.isAccessible = true
            eField.isAccessible = true
            fField.isAccessible = true
            gField.isAccessible = true
        }
    }

}