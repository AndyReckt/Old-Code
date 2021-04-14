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

object IgnoreListClearCommand {
    @Command(["ignore clear"], description = "Clear your ignore list", async = true)
    @JvmStatic
    fun execute(player: Player) {
        Stark.instance.messagingManager.clearIgnoreList(player.uniqueId)
        player.sendMessage("${ChatColor.YELLOW}You've cleared your ignore list.")
    }
}