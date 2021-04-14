/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object ClearCacheCommand {
    @Command(["clearcache"], permission = "op")
    @JvmStatic
    fun execute(sender: CommandSender) {
        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.set("UUIDCache", null)
        }

        Stark.instance.core.uuidCache.reset()

        Stark.instance.server.onlinePlayers.forEach { player ->
            Stark.instance.core.uuidCache.cache(player.uniqueId, player.name)
        }

        sender.sendMessage("${ChatColor.GREEN}Cleared UUID cache...")
    }
}