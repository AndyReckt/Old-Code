/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.rank.menu.RankManageMenu
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/20/2020.
 */
object RankManageCommand {

    @Command(["rank manage"], "op")
    @JvmStatic
    fun rankManage(player: Player) {
        RankManageMenu().openMenu(player)
    }
}