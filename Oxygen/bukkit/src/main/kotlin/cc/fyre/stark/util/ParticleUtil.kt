/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld

/**
 * Created by DaddyDombo daddydombo@gmail.com on 2/2/2020.
 */
object ParticleUtil {

    // name x y z offX offY offZ speed count
    fun sendsParticleToAll(vararg particleMetas: ParticleMeta) {
        val packets = arrayListOf<Pair<Location, PacketPlayOutWorldParticles>>()

        for (meta in particleMetas) {
            packets.add(meta.location to PacketPlayOutWorldParticles(
                    meta.effect.getName(),
                    meta.location.x.toFloat(),
                    meta.location.y.toFloat(),
                    meta.location.z.toFloat(),
                    meta.offsetX,
                    meta.offsetY,
                    meta.offsetZ,
                    meta.speed,
                    meta.amount))
        }

        packets.forEach { pair ->
            (pair.first.world as CraftWorld).handle.playerMap.forEachNearby(pair.first.x, pair.first.y, pair.first.z, 64.0, true) { player ->
                player.playerConnection.sendPacket(pair.second)
            }
        }
    }

}