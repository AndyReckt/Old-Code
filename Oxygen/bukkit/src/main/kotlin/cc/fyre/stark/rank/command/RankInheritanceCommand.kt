/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.command.CommandSender

object RankInheritanceCommand {

    @Command(["rank inherit add"], permission = "op")
    @JvmStatic
    fun addInheritance(sender: CommandSender, @Param("rank") rankName: String, @Param("parentRank") parentRankName: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)
        val parentRank = Stark.instance.core.rankHandler.getByName(parentRankName)

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
            return
        }

        if (parentRank == null) {
            sender.sendMessage(CC.RED + "Rank '" + parentRankName + "' does not exist.")
            return
        }

        rank.inherits.add(parentRank.id)
        Stark.instance.core.rankHandler.saveRank(rank)
        sender.sendMessage(CC.GOLD + "Rank " + rank.getColoredName() + CC.GOLD + " now inherits rank " + parentRank.getColoredName() + CC.GOLD + ".")
    }

    @Command(["rank inherit remove"], permission = "op")
    @JvmStatic
    fun removeInheritance(sender: CommandSender, @Param("rank") rankName: String, @Param("parentRank") parentRankName: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)
        val parentRank = Stark.instance.core.rankHandler.getByName(parentRankName)

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
            return
        }

        if (parentRank == null) {
            sender.sendMessage(CC.RED + "Rank '" + parentRankName + "' does not exist.")
            return
        }

        rank.inherits.remove(parentRank.id)
        Stark.instance.core.rankHandler.saveRank(rank)
        sender.sendMessage(CC.GOLD + "Rank " + rank.getColoredName() + CC.GOLD + " no longer inherits rank " + parentRank.getColoredName() + CC.GOLD + ".")
    }

    @Command(["rank inherit list"], permission = "op")
    @JvmStatic
    fun listInheritance(sender: CommandSender, @Param("rank") rankName: String) {
        val rank = Stark.instance.core.rankHandler.getByName(rankName)

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank '" + rankName + "' does not exist.")
            return
        }

        if (rank.inherits.isEmpty()) {
            sender.sendMessage(CC.RED + "Rank " + rank.id + " has no inheritances.")
            return
        }

        sender.sendMessage(CC.RED + rank.displayName + " Inheritances")
        for (inherit in rank.inherits) {
            sender.sendMessage(CC.RED + "- " + inherit)
        }
    }

}