/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object RankListCommand {

    @Command(["rank list"], "op")
    @JvmStatic
    fun rankList(sender: Player) {
        sender.sendMessage(CC.CHAT_BAR)
        sender.sendMessage("${ChatColor.GOLD}Rank List")
        sender.sendMessage(CC.CHAT_BAR)
        for (rank in Stark.instance.core.rankHandler.getRanks().sortedBy { rank -> rank.displayOrder }) {
            sender.sendMessage("${ChatColor.translateAlternateColorCodes('&', rank.gameColor)}${rank.displayName} ${CC.translate("&7(&e${rank.displayOrder}&7)")}")
        }
        sender.sendMessage(CC.CHAT_BAR)
    }
}