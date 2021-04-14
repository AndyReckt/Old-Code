/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.command

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.grant.menu.GrantsMenu
import org.bukkit.entity.Player

object GrantsCommand {
    @Command(["grants"], description = "View a player's grants", permission = "stark.command.grants", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("target") target: BukkitProfile) {
        GrantsMenu(target).openMenu(player)
    }
}