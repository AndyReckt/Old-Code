/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.permissions

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.util.CC
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2020.
 */
object PlayerPermissionsCommands {

    @JvmStatic
    @Command(names = ["listindividualperm", "playerperms", "player listperms"], permission = "op", async = true)
    fun execute(sender: CommandSender, @Param("target") target: BukkitProfile) {
        sender.sendMessage(CC.CHAT_BAR)
        sender.sendMessage("Username: ${target.getPlayerListName()}")
        if (target.playerPerms.isNullOrEmpty()) {
            sender.sendMessage("Player Permissions: Empty")
        } else {
            sender.sendMessage("Player Permissions: ${target.playerPerms.joinToString()}")
        }
        sender.sendMessage(CC.CHAT_BAR)
    }

    @JvmStatic
    @Command(names = ["addindividualperm", "addplayerperm", "player addperm"], permission = "op", async = true)
    fun add(sender: CommandSender, @Param("target") target: BukkitProfile, @Param("perm") perm: String) {
        if (sender is ConsoleCommandSender) {
            target.playerPerms.add(perm)
            update(target)
            sender.sendMessage("Added perm ($perm) to ${target.uuid}")
        } else {
            sender.sendMessage("${CC.RED}You must be console to execute this command!")
        }
    }

    @JvmStatic
    @Command(names = ["removeindividualperm", "removeplayerperm", "player delperm"], permission = "op", async = true)
    fun remove(sender: CommandSender, @Param("target") target: BukkitProfile, @Param("perm") perm: String) {
        if (sender is ConsoleCommandSender) {
            target.playerPerms.remove(perm)
            update(target)
            sender.sendMessage("Removed perm ($perm) to ${target.uuid}")
        } else {
            sender.sendMessage("${CC.RED}You must be console to execute this command!")
        }
    }

    fun update(profile: BukkitProfile) {
        profile.apply()
        Stark.instance.core.getProfileHandler().saveProfile(profile)
    }
}