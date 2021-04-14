/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.messaging.command

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.mojanguser.MojangUser
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object IgnoreRemoveCommand {
    @Command(["ignore remove"], description = "Stop ignoring a player", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("player") target: MojangUser) {
        if (!Stark.instance.messagingManager.isIgnored(player.uniqueId, target.uuid)) {
            player.sendMessage("${ChatColor.RED}You aren't ignoring ${ChatColor.WHITE}${target.username}${ChatColor.RED}.")
            return
        }

        Stark.instance.messagingManager.removeFromIgnoreList(player.uniqueId, target.uuid)

        player.sendMessage("${ChatColor.YELLOW}You are no longer ignoring ${ChatColor.WHITE}${target.username}${ChatColor.YELLOW}.")
    }
}