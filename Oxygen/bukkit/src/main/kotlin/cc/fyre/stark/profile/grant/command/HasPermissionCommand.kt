/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object HasPermissionCommand {
    @JvmStatic
    @Command(["hasperm"], permission = "op")
    fun execute(player: Player, @Param("target", defaultValue = "self") target: Player, @Param("permission") permission: String) {
        if (target.hasPermission(permission)) {
            player.sendMessage("${target.name} ${ChatColor.GREEN}has ${ChatColor.RESET}the permission ${ChatColor.GREEN}$permission")
        } else {
            player.sendMessage("${target.name} ${ChatColor.RED}does not have ${ChatColor.RESET}the permission ${ChatColor.GREEN}$permission")
        }
    }
}