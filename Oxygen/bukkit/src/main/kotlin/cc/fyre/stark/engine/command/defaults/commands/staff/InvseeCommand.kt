/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.staff

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.engine.command.data.parameter.impl.offlineplayer.OfflinePlayerWrapper
import cc.fyre.stark.inventory.TrackedPlayerInventory
import cc.fyre.stark.util.Callback
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object InvseeCommand {
    @Command(["invsee"], permission = "stark.command.invsee", description = "Open a player's inventory")
    @JvmStatic
    fun invsee(sender: Player, @Param(name = "player") wrapper: OfflinePlayerWrapper) {
        wrapper.loadAsync(object : Callback<Player?> {
            override fun callback(value: Player?) {
                if (value == null) {
                    sender.sendMessage("${ChatColor.RED}No online or offline player with the name ${wrapper.name} found.")
                    return
                }

                if (value == sender) {
                    sender.sendMessage("${ChatColor.RED}You can't invsee yourself!")
                    return
                }

                sender.openInventory(TrackedPlayerInventory.get(value).getBukkitInventory())
                TrackedPlayerInventory.open.add(sender.uniqueId)
            }
        })
    }
}