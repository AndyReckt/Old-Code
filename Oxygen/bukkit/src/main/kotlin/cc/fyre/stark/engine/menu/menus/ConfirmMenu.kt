/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.menu.menus

import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import cc.fyre.stark.engine.menu.buttons.BooleanButton
import cc.fyre.stark.util.Callback
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class ConfirmMenu(private val title: String, private val callback: Callback<Boolean>) : Menu() {

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = HashMap<Int, Button>()

        for (i in 0..8) {
            when (i) {
                3 -> buttons[i] = BooleanButton(true, callback)
                5 -> buttons[i] = BooleanButton(false, callback)
                else -> buttons[i] = Button.placeholder(Material.STAINED_GLASS_PANE, 14.toByte())
            }
        }

        return buttons
    }

    override fun getTitle(player: Player): String {
        return title
    }

}