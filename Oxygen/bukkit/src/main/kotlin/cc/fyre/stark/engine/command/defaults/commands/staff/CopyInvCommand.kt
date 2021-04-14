/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.entity.Player

object CopyInvCommand {

    @Command(names = ["copyinv", "cpinv"], permission = "stark.command.copyinv")
    @JvmStatic
    fun execute(player: Player, @Param("targetPlayer") targetPlayer: Player) {
        player.inventory.contents = targetPlayer.inventory.contents
        player.inventory.armorContents = targetPlayer.inventory.armorContents
        player.sendMessage(CC.GOLD + "You have copied the inventory of " + CC.WHITE + targetPlayer.name + CC.GOLD + ".")
    }

}