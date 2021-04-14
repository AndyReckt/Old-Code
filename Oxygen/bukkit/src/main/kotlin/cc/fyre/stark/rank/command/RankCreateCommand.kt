/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/20/2020.
 */
object RankCreateCommand {

    @Command(["rank create"], "op")
    @JvmStatic
    fun rankCreate(player: Player, @Param("rank") rank: String) {
        if (StarkCore.instance.rankHandler.getByName(rank) != null) {
            player.sendMessage("${ChatColor.RED}A rank with this name already exists")
            return
        }

        val createdRank = Rank(rank.toLowerCase(), rank, 0, mutableListOf(), "&7", "&7", "&7", default = false, hidden = false, staff = false)
        StarkCore.instance.rankHandler.addRank(createdRank)
        player.sendMessage("${ChatColor.YELLOW}The rank ${ChatColor.GOLD}$rank ${ChatColor.YELLOW}has been created!")
    }
}