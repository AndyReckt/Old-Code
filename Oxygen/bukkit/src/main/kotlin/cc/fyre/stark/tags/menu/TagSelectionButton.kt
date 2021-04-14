/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.tags.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.tags.Tag
import cc.fyre.stark.core.tags.TagType
import cc.fyre.stark.engine.menu.Button
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
class TagSelectionButton(private val tag: Tag) : Button() {

    override fun getName(player: Player): String? {
        return "${ChatColor.YELLOW}${tag.name}"
    }

    override fun getDescription(player: Player): List<String>? {
        val list = arrayListOf<String>()

        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)
                ?: return listOf("${ChatColor.RED}Failed loading your profile.")

        list.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}---------------------------")
        list.add("${ChatColor.YELLOW}Tag${ChatColor.GRAY}: ${tag.display}")
        list.add("${ChatColor.YELLOW}Display: ${if (tag.tagType == TagType.BEFORE) tag.display else ""} ${profile.getDisplayName()}${if (tag.tagType == TagType.AFTER) tag.display else ""}${ChatColor.RESET}: Hello, World!")
//        list.add("${ChatColor.YELLOW}Position${ChatColor.GRAY}: ${ChatColor.LIGHT_PURPLE}${tag.tagType.toString().capitalize()}")
        list.add("${ChatColor.YELLOW}Position: ${ChatColor.LIGHT_PURPLE}${if (tag.tagType == TagType.BEFORE) "Prefix" else "Suffix"}")
        list.add("")
        if (player.hasPermission("tag." + tag.name.toLowerCase())) {
            if (profile.tag == tag) {
                list.add("${ChatColor.GREEN}Active ${ChatColor.GRAY}(Right click to disable)")
            } else {
                list.add("${ChatColor.RED}Inactive ${ChatColor.GRAY}(Left click to enable)")
            }
        } else {
            list.add("${ChatColor.GRAY}Purchase at ${ChatColor.GOLD}store.vyrix.us")
        }
        list.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}---------------------------")
        return list
    }

    override fun getMaterial(player: Player): Material? {
        return Material.INK_SACK
    }

    override fun getDamageValue(player: Player): Byte {
        val currentTag = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!.tag
        return if (currentTag == tag) {
            10 // lime green
        } else if (player.hasPermission("tag." + tag.name.toLowerCase()) && currentTag != tag) {
            8 // gray (dark)
        } else {
            1 // red
        }
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (clickType.isLeftClick) {
            if (player.hasPermission("tag." + tag.name.toLowerCase())) {
                playSuccess(player)
                Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!.tag = tag
                player.sendMessage("${ChatColor.YELLOW}Equipped the ${tag.name} tag!")
            } else {
                playFail(player)
                player.sendMessage("${ChatColor.YELLOW}You do not own this tag. ${ChatColor.GRAY}Purchase at ${ChatColor.GOLD}store.vyrix.us")
            }
        } else if (clickType.isRightClick && StarkCore.instance.getProfileHandler().getByUUID(player.uniqueId)!!.tag != null) {
            playNeutral(player)
            Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!.tag = null
            player.sendMessage("${ChatColor.YELLOW}Removed your current tag")
        }
    }
}