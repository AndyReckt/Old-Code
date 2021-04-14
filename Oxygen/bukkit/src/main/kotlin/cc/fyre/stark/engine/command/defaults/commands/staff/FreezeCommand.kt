/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object FreezeCommand {
    @Command(["freeze", "ss"], permission = "stark.command.freeze", description = "Freeze a player. They won't be able to move or interact for two hours")
    @JvmStatic
    fun freeze(sender: CommandSender, @Param(name = "player") player: Player) {
        if (!sender.isOp && player.hasPermission("stark.staff")) {
            sender.sendMessage("${ChatColor.RED}You can't freeze that player.")
            return
        }

        if (player.hasMetadata("frozen")) {
            sender.sendMessage("${ChatColor.RED}This player is already frozen, did you mean /unfreeze")
            return
        }

        Stark.instance.serverHandler.freeze(player)
        sender.sendMessage("${player.displayName}${ChatColor.GOLD} has been frozen.")
    }

    @Command(["unfreeze", "unss", "thereclean"], permission = "stark.command.freeze", description = "Unfreeze a player")
    @JvmStatic
    fun unfreeze(sender: CommandSender, @Param(name = "player") player: Player) {
        if (!player.hasMetadata("frozen")) {
            player.sendMessage("${ChatColor.RED}That player is not frozen, did you mean /freeze")
            return
        }

        Stark.instance.serverHandler.unfreeze(player.uniqueId)
        sender.sendMessage("${player.displayName}${ChatColor.GOLD} has been unfrozen.")
    }
}