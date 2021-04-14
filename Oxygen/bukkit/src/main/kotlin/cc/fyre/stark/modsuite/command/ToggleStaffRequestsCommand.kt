/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.modsuite.options.ModOptionsHandler
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ToggleStaffRequestsCommand {
    @Command(["tsr", "togglereports", "togglerequests"], permission = "stark.staff", description = "Toggle receiving staff requests", async = true)
    @JvmStatic
    fun execute(player: Player) {
        val options = ModOptionsHandler.get(player)
        options.receivingRequests = !options.receivingRequests

        ModOptionsHandler.update(player.uniqueId, options)

        if (options.receivingRequests) {
            player.sendMessage("${ChatColor.GREEN}You're now receiving requests and reports.")
        } else {
            player.sendMessage("${ChatColor.RED}You're no longer receiving requests and reports.")
        }
    }
}