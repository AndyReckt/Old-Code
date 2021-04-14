/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.visibility

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object VisibilityDebugCommand {

    @JvmStatic
    @Command(["visdebug"], hidden = true, permission = "op")
    fun debugCommand(commandSender: CommandSender, @Param("target") target: Player, @Param("viewer") viewer: Player) {
        commandSender.sendMessage(Stark.instance.visibilityEngine.getDebugInfo(target, viewer).toTypedArray())
    }
}