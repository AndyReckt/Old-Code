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
object RankStaffCommand {

    @Command(["rank setstaff"], "op")
    @JvmStatic
    fun rankDefault(sender: Player, @Param("rank") rank: Rank, @Param("staff") staff: Boolean) {
        StarkCore.instance.rankHandler.getByName(rank.displayName)!!.staff = staff

        StarkCore.instance.rankHandler.saveRank(rank)
        sender.sendMessage("${ChatColor.YELLOW}The rank ${ChatColor.GOLD}${rank.displayName} ${ChatColor.YELLOW}staff has been updated to ${ChatColor.GOLD}$staff${ChatColor.YELLOW}.")
    }
}