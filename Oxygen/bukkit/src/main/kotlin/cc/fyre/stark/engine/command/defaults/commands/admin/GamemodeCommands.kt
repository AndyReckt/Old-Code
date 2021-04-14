/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GamemodeCommands {
    @Command(["gamemode", "gm"], permission = "stark.command.gamemode", description = "Set a player's gamemode")
    @JvmStatic
    fun gamemode(sender: CommandSender, @Param(name = "mode", defaultValue = "-0*toggle*0-") mode: GameMode, @Param(name = "player", defaultValue = "self") target: Player) {
        run(sender, target, mode)
    }

    @Command(["gms", "gm0"], permission = "stark.command.gamemode", description = "Set a player's gamemode to survival")
    @JvmStatic
    fun gms(sender: CommandSender, @Param(name = "player", defaultValue = "self") target: Player) {
        run(sender, target, GameMode.SURVIVAL)
    }

    @Command(["gmc", "gm1"], permission = "stark.command.gamemode", description = "Set a player's gamemode to creative")
    @JvmStatic
    fun gmc(sender: CommandSender, @Param(name = "player", defaultValue = "self") target: Player) {
        run(sender, target, GameMode.CREATIVE)
    }

    @Command(["gma", "gm2"], permission = "stark.command.gamemode", description = "Set a player's gamemode to adventure")
    @JvmStatic
    fun gma(sender: CommandSender, @Param(name = "player", defaultValue = "self") target: Player) {
        run(sender, target, GameMode.ADVENTURE)
    }

    private fun run(sender: CommandSender, target: Player, mode: GameMode) {
        if (sender != target && !sender.isOp) {
            sender.sendMessage("${ChatColor.RED}No permission to set other player's gamemode.")
            return
        }
        target.gameMode = mode
        if (sender != target) {
            sender.sendMessage("${target.displayName}${ChatColor.GOLD} is now in ${ChatColor.WHITE}$mode${ChatColor.GOLD} mode.")
        }
        target.sendMessage("${ChatColor.GOLD}You are now in ${ChatColor.WHITE}$mode${ChatColor.GOLD} mode.")
    }
}