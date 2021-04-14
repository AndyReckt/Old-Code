/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

object SeenCommand {

    @Command(["seen"], permission = "stark.command.seen", description = "Check a player's online status.")
    @JvmStatic
    fun execute(sender: CommandSender, @Param("targetPlayer") targetPlayer: OfflinePlayer) {
        sender.sendMessage(CC.CHAT_BAR)
        if (targetPlayer.isOnline) {
            sender.sendMessage(CC.GREEN + targetPlayer.name + " is currently online!")
        } else {
            sender.sendMessage(CC.RED + targetPlayer.name + " is currently offline.")
            sender.sendMessage(CC.GRAY + "Last Seen: " + CC.WHITE + TimeUtils.formatLongIntoDetailedString(targetPlayer.lastPlayed))
        }
        sender.sendMessage(CC.CHAT_BAR)
    }

}