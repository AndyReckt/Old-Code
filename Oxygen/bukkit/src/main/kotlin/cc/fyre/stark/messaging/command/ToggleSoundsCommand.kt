/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.messaging.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ToggleSoundsCommand {
    @Command(["sounds", "togglesounds"], description = "Toggle messaging sounds", async = true)
    @JvmStatic
    fun execute(player: Player) {
        val toggle = Stark.instance.messagingManager.toggleSounds(player.uniqueId)

        if (toggle) {
            player.sendMessage(ChatColor.YELLOW.toString() + "Messaging sounds have been disabled.")
        } else {
            player.sendMessage(ChatColor.YELLOW.toString() + "Messaging sounds have been enabled.")
        }
    }
}