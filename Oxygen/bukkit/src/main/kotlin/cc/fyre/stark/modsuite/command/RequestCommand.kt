/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.modsuite.ModRequestHandler
import cc.fyre.stark.modsuite.event.PlayerRequestReportEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

object RequestCommand {
    @Command(["request", "helpop"], description = "Request assistance", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("message", wildcard = true) reason: String) {
        if (ModRequestHandler.hasRequestCooldown(player)) {
            player.sendMessage("${ChatColor.RED}You can only make one staff request every 2 minutes.")
            return
        }

        val event = PlayerRequestReportEvent(player)
        Bukkit.getServer().pluginManager.callEvent(event)

        if (event.isCancelled) {
            if (event.cancelMessage != null) {
                player.sendMessage(event.cancelMessage)
            } else {
                player.sendMessage("${ChatColor.RED}Unknown cancellation.")
            }
        } else {
            ModRequestHandler.addRequestCooldown(player, 2L, TimeUnit.MINUTES)

            val data = mapOf(
                    "serverName" to Bukkit.getServerId(),
                    "senderName" to player.name,
                    "reason" to reason
            )

            Stark.instance.core.globalMessageChannel.sendMessage(Message("REQUEST", data))

            player.sendMessage("${ChatColor.GREEN}We have received your request.")
        }
    }
}