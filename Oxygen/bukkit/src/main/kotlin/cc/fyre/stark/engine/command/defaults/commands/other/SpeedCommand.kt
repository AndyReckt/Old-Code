/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object SpeedCommand {
    @Command(["speed"], permission = "stark.command.speed", description = "Change your walk or fly speed")
    @JvmStatic
    fun speed(sender: Player, @Param(name = "speed") speed: Int) {
        if (speed < 0 || speed > 10) {
            sender.sendMessage(ChatColor.RED.toString() + "Speed must be between 0 and 10.")
            return
        }

        val fly = sender.isFlying
        if (fly) {
            sender.flySpeed = getSpeed(speed, true)
        } else {
            sender.walkSpeed = getSpeed(speed, false)
        }

        sender.sendMessage(ChatColor.GOLD.toString() + (if (fly) "Fly" else "Walk") + " set to " + ChatColor.WHITE + speed + ChatColor.GOLD + ".")
    }

    private fun getSpeed(speed: Int, isFly: Boolean): Float {
        val defaultSpeed = if (isFly) 0.1f else 0.2f
        val maxSpeed = 1.0f
        if (speed < 1.0f) {
            return defaultSpeed * speed
        }
        val ratio = (speed - 1.0f) / 9.0f * (maxSpeed - defaultSpeed)
        return ratio + defaultSpeed
    }
}