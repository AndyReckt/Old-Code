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

object IgnoreListCommand {
    @Command(["ignore list"], description = "See a list of people you're currently ignoring", async = true)
    @JvmStatic
    fun execute(player: Player) {
        val ignoreList = Stark.instance.messagingManager.getIgnoreList(player.uniqueId)

        if (ignoreList.isEmpty()) {
            player.sendMessage("${ChatColor.RED}You aren't ignoring anyone.")
            return
        }

        val names = arrayListOf<String>()
        for (uuid in ignoreList) {
            names.add(Stark.instance.core.uuidCache.name(uuid))
        }

        player.sendMessage("${ChatColor.YELLOW}You are currently ignoring ${ChatColor.RED}${names.size} ${ChatColor.YELLOW}player${if (names.size == 1) "" else "s"}:")
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', names.joinToString("&e, ", "&c")))
    }
}