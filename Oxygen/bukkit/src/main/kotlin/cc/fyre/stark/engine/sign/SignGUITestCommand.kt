/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.sign

import cc.fyre.stark.engine.command.Command
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/3/2020.
 */
object SignGUITestCommand {
    @Command(["signgui test"], description = "", async = true)
    @JvmStatic
    fun execute(player: Player) {
        SignGUI().listeners[player.name]?.let { SignGUI().open(player, arrayOf("", "^^^^^^^", "Test Sign 2", "Test Sign 3"), it) }
/*        SignGUI().open(player, arrayOf("", "^^^^^^^^^^^^^^", "Type a name", "you wish to use"), { player1, lines -> DisguiseCommands.startDisguiseProcess(player, lines.get(0)) })
        SignGUI().open(player, arrayOf("", "^^^^^^^^^^^^^^", "Type a name", "you wish to use" ), (player, lines) -> DisguiseCommands.startDisguiseProcess(player, lines[0]))*/
    }
}