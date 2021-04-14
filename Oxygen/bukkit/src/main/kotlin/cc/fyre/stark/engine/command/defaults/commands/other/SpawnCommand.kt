/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/15/2020.
 */
object SpawnCommand {
    @Command(["spawn"], permission = "stark.command.spawn")
    @JvmStatic
    fun spawn(sender: Player) {
        sender.teleport(sender.world.spawnLocation)
        sender.sendMessage("${ChatColor.GOLD}You have been teleported to " + ChatColor.WHITE + sender.world.name + "'s" + ChatColor.GOLD + " spawn.")
    }
}