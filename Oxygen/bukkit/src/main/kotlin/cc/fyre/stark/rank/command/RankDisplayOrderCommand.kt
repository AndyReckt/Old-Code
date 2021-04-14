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
object RankDisplayOrderCommand {

    @Command(["rank setdisplayorder"], "op")
    @JvmStatic
    fun rankDisplayOrder(sender: Player, @Param("rank") rank: Rank, @Param("displayOrder") displayOrder: Int) {
        StarkCore.instance.rankHandler.getByName(rank.displayName)!!.displayOrder = displayOrder

        StarkCore.instance.rankHandler.saveRank(rank)
        sender.sendMessage("${ChatColor.YELLOW}The rank ${ChatColor.GOLD}${rank.displayName} ${ChatColor.YELLOW}display order has been updated to ${ChatColor.GOLD}$displayOrder${ChatColor.YELLOW}.")
    }
}