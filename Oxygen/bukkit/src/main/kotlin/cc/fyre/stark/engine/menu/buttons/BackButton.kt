/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.menu.buttons

import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.util.Callback
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

class BackButton(private val callback: Callback<Player>) : Button() {

    override fun getMaterial(player: Player): Material? {
        return Material.BED
    }

    override fun getName(player: Player): String? {
        return "§cGo back"
    }

    override fun getDescription(player: Player): List<String>? {
        return ArrayList()
    }

    override fun clicked(player: Player, i: Int, clickType: ClickType) {
        playNeutral(player)
        player.closeInventory()

        callback.callback(player)
    }

}