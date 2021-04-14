/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.util.CC
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

object KillAllCommand {

    @Command(names = ["killall"], permission = "op")
    @JvmStatic
    fun execute(player: Player) {
        val world = player.location.world
        world.entities.stream().filter { entity: Entity? -> entity is LivingEntity && entity !is Player }.forEach { obj: Entity -> obj.remove() }
        player.sendMessage(CC.GOLD + "You have cleared all living mobs in " + CC.WHITE + world.name + CC.GOLD + ".")
    }
}