/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import com.google.common.base.Joiner
import org.bukkit.entity.Player

object RankInfoCommand {

    @Command(["rank info"], "op")
    @JvmStatic
    fun rankInfo(sender: Player, @Param("rank") rank: Rank) {
        sender.sendMessage(CC.CHAT_BAR)
        sender.sendMessage(CC.GOLD + rank.getColoredName() + CC.GOLD + " Rank")
        sender.sendMessage(CC.CHAT_BAR)
        sender.sendMessage(CC.GRAY + "Display Name: " + CC.WHITE + rank.displayName)
        sender.sendMessage(CC.GRAY + "Default: " + CC.WHITE + rank.default)
        sender.sendMessage(CC.GRAY + "Parents: " + CC.WHITE + Joiner.on(", ").join(rank.inherits))
        sender.sendMessage(CC.GRAY + "Weight: " + CC.WHITE + rank.displayOrder)
        sender.sendMessage(CC.GRAY + "Scope: " + CC.WHITE + rank.scope)
        sender.sendMessage(CC.GRAY + "Hidden: " + CC.WHITE + rank.hidden)
        sender.sendMessage(CC.GRAY + "Permissions: ")
        for (permission in rank.permissions) {
            sender.sendMessage(CC.GOLD + "- " + CC.WHITE + permission)
        }
        sender.sendMessage(CC.CHAT_BAR)
    }
}