/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.menu.pagination

import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import cc.fyre.stark.engine.menu.buttons.BackButton
import cc.fyre.stark.util.Callback
import org.bukkit.entity.Player
import java.util.*

class ViewAllPagesMenu(private val menu: PaginatedMenu) : Menu() {

    init {
        autoUpdate = true
    }

    override fun getTitle(player: Player): String {
        return "Jump to page"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = HashMap<Int, Button>()

        buttons[0] = BackButton(object : Callback<Player> {
            override fun callback(value: Player) {
                menu.openMenu(player)
            }
        })

        var index = 10
        for (i in 1..menu.getPages(player)) {
            buttons[index++] = JumpToPageButton(i, menu)
            if ((index - 8) % 9 == 0) {
                index += 2
            }
        }

        return buttons
    }

}