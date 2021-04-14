/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.punishment.command.create

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.profile.Profile
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.util.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object KickCommand {
    @Command(["kick", "k"], permission = "stark.command.kick", description = "Kick a player from the server")
    @JvmStatic
    fun execute(sender: Player, @Flag(value = ["s", "silentMode"], description = "Silently kick the player") silent: Boolean, @Param(name = "player") target: Player, @Param(name = "reason", defaultValue = "Kicked by a staff member", wildcard = true) reason: String) {

        if (target.hasPermission("stark.punishment.protected")) {
            sender.sendMessage("${ChatColor.RED}User is protected from punishments.")
            return
        }

        val targerProfile: Profile = StarkCore.instance.getProfileHandler().getByUUID(target.uniqueId)!!
        val issuer: Profile = StarkCore.instance.getProfileHandler().getByUUID(sender.uniqueId)!!

        if (!silent) {
            Bukkit.broadcastMessage(ChatColor.GREEN.toString() + targerProfile.getPlayerListName() + CC.GREEN + " was kicked by " + issuer.getPlayerListName() + CC.GREEN + ".")
        } else {
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("stark.staff")) {
                    player.sendMessage(ChatColor.GREEN.toString() + targerProfile.getPlayerListName() + CC.GREEN + " was " + ChatColor.YELLOW + "silently" + ChatColor.GREEN + " kicked by " + issuer.getPlayerListName() + CC.GREEN + ".")
                }
            }
        }

        target.kickPlayer("${ChatColor.RED}You were kicked: ${ChatColor.YELLOW}" + reason)
    }
}