/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.player

import cc.fyre.stark.engine.command.Command
import org.bukkit.entity.Player

object CraftCommand {
    @Command(["craft"], permission = "stark.command.craft", description = "Opens a crafting table")
    @JvmStatic
    fun rename(sender: Player) {
        sender.openWorkbench(sender.location, true)
    }
}