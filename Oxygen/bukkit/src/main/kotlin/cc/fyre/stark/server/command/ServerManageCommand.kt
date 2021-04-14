/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.server.menu.ServerManagerMenu
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/9/2020.
 */
object ServerManageCommand {
    @Command(["servermanager"], "op", async = true)
    @JvmStatic
    fun serverManage(player: Player) {
        ServerManagerMenu().openMenu(player)
    }
}