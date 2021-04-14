/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.staff

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import net.md_5.bungee.api.connection.Server
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerSwitchEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.*

class StaffListeners : Listener {


    private val previousServer: MutableMap<UUID, Server?> = hashMapOf()
    private val currentServer: MutableMap<UUID, Server?> = hashMapOf()

    @EventHandler(priority = 0)
    fun onPlayerDisconnectEvent(event: PlayerDisconnectEvent) {
        previousServer.remove(event.player.uniqueId)

        val from = currentServer.remove(event.player.uniqueId)

        if (from != null) {
            if (event.player.hasPermission("stark.staff")) {
                val profile = Stark.instance.core.getProfileHandler().profiles[event.player.uniqueId]!!

                val data = mapOf(
                    "playerName" to profile.getPlayerListName(),
                    "action" to StaffAction.LEAVE_NETWORK.name,
                    "from" to from.info.name
                )

                Stark.instance.proxyMessageChannel.sendMessage(Message("STAFF_ACTION", data))
            }
        }
    }

    @EventHandler
    fun onServerSwitchEvent(event: ServerSwitchEvent) {
        if (event.player.hasPermission("stark.staff")) {
            currentServer[event.player.uniqueId] = event.player.server

            val profile = Stark.instance.core.getProfileHandler().profiles[event.player.uniqueId]!!
            val previousServer = previousServer[event.player.uniqueId]

            if (previousServer == null) {
                val data = hashMapOf(
                    "playerName" to profile.getPlayerListName(),
                    "action" to StaffAction.JOIN_NETWORK.name,
                    "to" to event.player.server.info.name
                )

                Stark.instance.proxyMessageChannel.sendMessage(Message("STAFF_ACTION", data))
            } else {
                val data = hashMapOf(
                    "playerName" to profile.getPlayerListName(),
                    "action" to StaffAction.SWITCH_SERVER.name,
                    "from" to previousServer.info.name,
                    "to" to event.player.server.info.name
                )

                Stark.instance.proxyMessageChannel.sendMessage(Message("STAFF_ACTION", data))
            }
        }
    }

    @EventHandler
    fun onServerConnectEvent(event: ServerConnectEvent) {
        if (event.player.hasPermission("stark.staff")) {
            previousServer[event.player.uniqueId] = event.player.server
        }
    }

}