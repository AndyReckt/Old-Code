/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import org.bukkit.ChatColor
import org.bukkit.Color

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/15/2020
 */
object ColorUtil {

    private val colorMap = mapOf(
            ChatColor.WHITE to Color.WHITE,
            ChatColor.GOLD to Color.ORANGE,
            ChatColor.AQUA to Color.AQUA,
            ChatColor.YELLOW to Color.YELLOW,
            ChatColor.GREEN to Color.LIME,
            ChatColor.LIGHT_PURPLE to Color.FUCHSIA,
            ChatColor.GRAY to Color.GRAY,
            ChatColor.DARK_GRAY to Color.GRAY,
            ChatColor.DARK_AQUA to Color.TEAL,
            ChatColor.DARK_PURPLE to Color.PURPLE,
            ChatColor.BLUE to Color.BLUE,
            ChatColor.DARK_GREEN to Color.OLIVE,
            ChatColor.RED to Color.RED,
            ChatColor.DARK_RED to Color.MAROON,
            ChatColor.BLACK to Color.BLACK)

    fun getColorFromChatColor(chatColor: ChatColor): Color {
        return colorMap[chatColor] ?: Color.WHITE
    }

    fun getColorFromChatColor(chatColor: String): Color {
        return getColorFromChatColor(ChatColor.getByChar(chatColor.replace("&", "")))
    }

    fun getColorFromChatCharMenu(chatColor: String): Color {
        return getColorFromChatColor(ChatColor.getByChar(chatColor.replace("§", "")))
    }
}