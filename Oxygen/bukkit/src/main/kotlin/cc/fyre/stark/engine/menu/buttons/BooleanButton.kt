/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.menu.buttons

import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.util.Callback
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

class BooleanButton(private val confirm: Boolean, private val callback: Callback<Boolean>) : Button() {

    override fun getName(player: Player): String? {
        return if (confirm) "§aConfirm" else "§cCancel"
    }

    override fun getDescription(player: Player): List<String>? {
        return ArrayList()
    }

    override fun getDamageValue(player: Player): Byte {
        return (if (this.confirm) 5 else 14).toByte()
    }

    override fun getMaterial(player: Player): Material? {
        return Material.WOOL
    }

    override fun clicked(player: Player, i: Int, clickType: ClickType) {
        if (confirm) {
            player.playSound(player.location, Sound.NOTE_PIANO, 20.0f, 0.1f)
        } else {
            player.playSound(player.location, Sound.DIG_GRAVEL, 20.0f, 0.1f)
        }

        player.closeInventory()
        callback.callback(confirm)
    }

}