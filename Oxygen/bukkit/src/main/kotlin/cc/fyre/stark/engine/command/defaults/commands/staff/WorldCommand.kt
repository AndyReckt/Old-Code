/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.World
import org.bukkit.entity.Player

object WorldCommand {
    @Command(["world"], permission = "stark.command.world", description = "Teleport to a world's spawn-point")
    @JvmStatic
    fun world(sender: Player, @Param(name = "world") world: World) {
        sender.teleport(world.spawnLocation.clone().add(0.5, 0.0, 0.5))
    }
}