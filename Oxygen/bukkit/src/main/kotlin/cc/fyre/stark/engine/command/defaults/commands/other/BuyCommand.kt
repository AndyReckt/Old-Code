/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/18/2020.
 */
object BuyCommand {
    @Command(["buy"], permission = "")
    @JvmStatic
    fun spawn(sender: Player) {
        sender.sendMessage(CC.CHAT_BAR)
        sender.sendMessage("${ChatColor.WHITE}Please visit our store at ${ChatColor.GOLD}https://store.vyrix.us")
        sender.sendMessage(CC.CHAT_BAR)
    }
}