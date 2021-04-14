/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import cc.fyre.stark.Stark
import org.bukkit.Bukkit

class TabThread : Thread("stark - Tab Thread") {

    private val protocolLib = Bukkit.getServer().pluginManager.getPlugin("ProtocolLib")

    init {
        this.isDaemon = true
    }

    override fun run() {
        while (Stark.instance.isEnabled && protocolLib != null && protocolLib.isEnabled) {
            for (online in Stark.instance.server.onlinePlayers) {
                try {
                    Stark.instance.tabEngine.updatePlayer(online)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            try {
                sleep(250L)
            } catch (e2: InterruptedException) {
                e2.printStackTrace()
            }

        }
    }

}