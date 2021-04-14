/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.menu.pagination

import cc.fyre.stark.engine.menu.Button
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class JumpToPageButton(private val page: Int, private val menu: PaginatedMenu) : Button() {

    override fun getName(player: Player): String {
        return "§ePage " + this.page
    }

    override fun getDescription(player: Player): List<String>? {
        return null
    }

    override fun getMaterial(player: Player): Material {
        return Material.BOOK
    }

    override fun getAmount(player: Player): Int {
        return this.page
    }

    override fun clicked(player: Player, i: Int, clickType: ClickType) {
        menu.modPage(player, page - menu.page)
        playNeutral(player)
    }

}