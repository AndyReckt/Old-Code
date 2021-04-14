/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import cc.fyre.stark.Stark
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player

object BungeeUtil {

    fun sendToServer(player: Player, server: String) {
        try {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("Connect")
            out.writeUTF(server)

            player.sendPluginMessage(Stark.instance, "BungeeCord", out.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}