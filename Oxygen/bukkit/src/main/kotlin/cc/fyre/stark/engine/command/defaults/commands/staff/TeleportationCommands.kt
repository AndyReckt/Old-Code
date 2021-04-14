/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.engine.command.data.parameter.impl.offlineplayer.OfflinePlayerWrapper
import cc.fyre.stark.server.listener.TeleportationListeners
import cc.fyre.stark.util.CC
import cc.fyre.stark.util.Callback
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object TeleportationCommands {

    @Command(["teleport", "tp", "tpto", "goto"], permission = "stark.command.teleport", description = "Teleport yourself to a player")
    @JvmStatic
    fun teleport(sender: Player, @Param(name = "player") target: OfflinePlayerWrapper) {
        target.loadAsync(object : Callback<Player?> {
            override fun callback(value: Player?) {
                if (value == null) {
                    sender.sendMessage("${ChatColor.RED}No online or offline player with the name " + target.name + " found.")
                    return
                }

                sender.teleport(value as Entity)
                sender.sendMessage("${ChatColor.GOLD}Teleporting you to ${(if (value.isOnline) "" else "offline player ")}${ChatColor.WHITE}${value.displayName}${ChatColor.GOLD}.")
            }
        })
    }

    @Command(["tphere", "bring", "s"], permission = "stark.command.teleporthere", description = "Teleport a player to you")
    @JvmStatic
    fun tphere(sender: Player, @Param(name = "player") target: Player, @Flag(value = ["s", "silentMode"], description = "Silently teleport the player (staff members always get messaged)") silent: Boolean) {
        target.teleport(sender as Entity)
        sender.sendMessage("${ChatColor.GOLD}Teleporting " + ChatColor.WHITE + target.displayName + ChatColor.GOLD + " to you.")

        if (!silent || target.hasPermission("stark.staff")) {
            target.sendMessage("${ChatColor.GOLD}Teleporting you to " + ChatColor.WHITE + sender.displayName + ChatColor.GOLD + ".")
        }
    }

    @Command(["back"], permission = "stark.command.back", description = "Teleport to your last location")
    @JvmStatic
    fun back(sender: Player) {
        if (!TeleportationListeners.lastLocation.containsKey(sender.uniqueId)) {
            sender.sendMessage("${ChatColor.RED}No previous location recorded.")
            return
        }

        sender.teleport(TeleportationListeners.lastLocation[sender.uniqueId] as Location)
        sender.sendMessage("${ChatColor.GOLD}Teleporting you to your last recorded location.")
    }

    @Command(["tppos"], permission = "stark.command.tppos", description = "Teleport to coordinates")
    @JvmStatic
    fun teleport(sender: Player, @Param(name = "x") x: Double, @Param(name = "y") y: Double, @Param(name = "z") z: Double, @Param(name = "player", defaultValue = "self") target: Player) {
        var x = x
        var z = z

        if (sender != target && !sender.hasPermission("stark.command.teleport.other")) {
            sender.sendMessage("${ChatColor.RED}No permission to teleport other players.")
            return
        }

        if (isBlock(x)) {
            x += if (z >= 0.0) 0.5 else -0.5
        }

        if (isBlock(z)) {
            z += if (x >= 0.0) 0.5 else -0.5
        }

        target.teleport(Location(target.world, x, y, z))

        val location = ChatColor.translateAlternateColorCodes('&', String.format("&e[&f%s&e, &f%s&e, &f%s&e]&6", x, y, z))
        if (sender != target) {
            sender.sendMessage("${ChatColor.GOLD}Teleporting " + ChatColor.WHITE + target.displayName + ChatColor.GOLD + " to " + location + ".")
        }

        target.sendMessage("${ChatColor.GOLD}Teleporting you to " + location + ".")
    }

    @Command(["top"], permission = "stark.command.top", description = "Teleport to the highest point of your location.")
    @JvmStatic
    fun top(sender: Player) {
        val highestLocation = getHighestLocation(sender.location, null)
        if (highestLocation == null) {
            sender.sendMessage(CC.RED + "Could not find a location to teleport you to.")
        } else {
            sender.teleport(highestLocation)
            sender.sendMessage(CC.GOLD + "Teleporting to the highest point.")
        }
    }

    private fun isBlock(value: Double): Boolean {
        return value % 1.0 == 0.0
    }

    fun getHighestLocation(origin: Location, def: Location?): Location? {
        val cloned = origin.clone()
        val world = cloned.world
        val x = cloned.blockX
        var y = world.maxHeight
        val z = cloned.blockZ
        while (y > origin.blockY) {
            val block = world.getBlockAt(x, --y, z)
            if (!block.isEmpty) {
                val next = block.location
                next.pitch = origin.pitch
                next.yaw = origin.yaw
                return next
            }
        }
        return def
    }

}