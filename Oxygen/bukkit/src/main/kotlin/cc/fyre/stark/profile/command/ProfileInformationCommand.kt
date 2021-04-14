/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 6/10/2020.
 */
object ProfileInformationCommand {

    @JvmStatic
    @Command(["profile info"], permission = "op")
    fun execute(player: Player, @Param(name = "target") targetPlayer: Player) {
        val target = Stark.instance.core.getProfileHandler().getByUUID(targetPlayer.uniqueId)
        if (target != null) {
            player.sendMessage("${CC.AQUA}${target.getPlayerListName()}'s Profile")
            player.sendMessage(CC.CHAT_BAR)
            player.sendMessage("${CC.PINK}Rank: ${CC.WHITE}${target.getRank().displayName}")
            player.sendMessage("${CC.PINK}Permissions: ${CC.WHITE}${target.playerPerms.joinToString()}")
            player.sendMessage("${CC.PINK}Date Joined: ${CC.WHITE}${target.dateJoined}")
            player.sendMessage("${CC.PINK}Tag: ${CC.WHITE}${target.tag}")
            player.sendMessage(CC.CHAT_BAR)
            return
        }
        player.sendMessage(CC.RED + "Target player could not be found.")
    }
}