/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server

import cc.fyre.stark.Stark
import cc.fyre.stark.util.TPSUtils
import org.bukkit.Bukkit
import kotlin.streams.toList

class ServerSyncRunnable : Runnable {

    override fun run() {
        val optionalServer = Stark.instance.core.servers.getServerByName(Bukkit.getServerName())

        val server = if (optionalServer.isPresent) {
            optionalServer.get()
        } else {
            Stark.instance.core.servers.loadOrCreateServer(Bukkit.getServerName(), Bukkit.getPort())
        }

        server.lastHeartbeat = System.currentTimeMillis()
        server.currentUptime = System.currentTimeMillis() - Stark.instance.enabledAt
        server.currentTps = TPSUtils.tps
        server.playerCount = Bukkit.getOnlinePlayers().size
        server.maxSlots = Bukkit.getMaxPlayers()
        server.whitelisted = Bukkit.hasWhitelist()

        server.staffCount = Bukkit.getOnlinePlayers().stream().filter { it.hasPermission("stark.staff") }.toList().size

        Stark.instance.core.servers.saveServer(server)

    }

}