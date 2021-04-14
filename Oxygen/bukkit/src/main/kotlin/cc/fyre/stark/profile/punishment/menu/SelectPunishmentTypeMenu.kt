/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.punishment.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.util.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class SelectPunishmentTypeMenu(private val target: BukkitProfile) : Menu("${ChatColor.YELLOW}${Stark.instance.core.uuidCache.name(target.uuid)}'s Punishments") {

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        if (player.hasPermission("stark.command.check.blacklists")) {
            buttons[10] = SelectPunishmentTypeButton(target, ProfilePunishmentType.BLACKLIST)
            buttons[12] = SelectPunishmentTypeButton(target, ProfilePunishmentType.BAN)
            buttons[14] = SelectPunishmentTypeButton(target, ProfilePunishmentType.MUTE)
            buttons[16] = SelectPunishmentTypeButton(target, ProfilePunishmentType.WARN)
        } else {
            buttons[11] = SelectPunishmentTypeButton(target, ProfilePunishmentType.BAN)
            buttons[13] = SelectPunishmentTypeButton(target, ProfilePunishmentType.MUTE)
            buttons[15] = SelectPunishmentTypeButton(target, ProfilePunishmentType.WARN)
        }

//        buttons[26] = Button.placeholder(Material.AIR)

        for (x in 0..26) {
            if (!buttons.containsKey(x) || buttons[x] == null) {
                buttons[x] = FillButton()
            }
        }

        return buttons
    }

    class FillButton : Button() {

        override fun getName(player: Player): String? {
            return " "
        }

        override fun getDescription(player: Player): List<String>? {
            return null
        }

        override fun getMaterial(player: Player): Material? {
            return null
        }

        override fun getButtonItem(player: Player): ItemStack {
            return ItemBuilder.of(Material.STAINED_GLASS_PANE)
                    .name(" ")
                    .data(7)
                    .build()
        }

        override fun clicked(player: Player, slot: Int, clickType: ClickType) {}
    }

}