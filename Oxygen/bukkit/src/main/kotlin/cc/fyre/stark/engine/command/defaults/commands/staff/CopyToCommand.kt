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

object CopyToCommand {

    @Command(names = ["copyto", "sendinv", "cpto"], permission = "stark.command.copyto")
    @JvmStatic
    fun execute(player: Player, @Param("targetPlayer") targetPlayer: Player) {
        targetPlayer.inventory.contents = player.inventory.contents
        targetPlayer.inventory.armorContents = player.inventory.armorContents
        player.sendMessage(CC.GOLD + "You have copied your inventory to " + CC.WHITE + targetPlayer.name + CC.GOLD + ".")
    }

}