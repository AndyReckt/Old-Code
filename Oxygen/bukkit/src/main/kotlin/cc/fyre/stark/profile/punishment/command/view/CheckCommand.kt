/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.punishment.command.view

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.punishment.menu.SelectPunishmentTypeMenu
import org.bukkit.entity.Player

object CheckCommand {
    @Command(["check", "c", "history"], description = "View a player's punishments", permission = "stark.command.check", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("target") target: BukkitProfile) {
        SelectPunishmentTypeMenu(target).openMenu(player)
    }
}