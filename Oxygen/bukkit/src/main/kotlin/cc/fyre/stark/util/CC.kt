/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import net.minecraft.util.org.apache.commons.lang3.StringEscapeUtils
import org.bukkit.ChatColor

/**
 * Created by DaddyDombo daddydombo@gmail.com on 1/19/2020.
 */
object CC {

    val BLUE = ChatColor.BLUE.toString()
    val AQUA = ChatColor.AQUA.toString()
    val YELLOW = ChatColor.YELLOW.toString()
    val RED = ChatColor.RED.toString()
    val GRAY = ChatColor.GRAY.toString()
    val GOLD = ChatColor.GOLD.toString()
    val GREEN = ChatColor.GREEN.toString()
    val WHITE = ChatColor.WHITE.toString()
    val BLACK = ChatColor.BLACK.toString()
    val BOLD = ChatColor.BOLD.toString()
    val ITALIC = ChatColor.ITALIC.toString()
    val UNDER_LINE = ChatColor.UNDERLINE.toString()
    val STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString()
    val RESET = ChatColor.RESET.toString()
    val MAGIC = ChatColor.MAGIC.toString()
    val DARK_BLUE = ChatColor.DARK_BLUE.toString()
    val DARK_AQUA = ChatColor.DARK_AQUA.toString()
    val DARK_GRAY = ChatColor.DARK_GRAY.toString()
    val DARK_GREEN = ChatColor.DARK_GREEN.toString()
    val DARK_PURPLE = ChatColor.DARK_PURPLE.toString()
    val DARK_RED = ChatColor.DARK_RED.toString()
    val PINK = ChatColor.LIGHT_PURPLE.toString()
    val UNICODE_VERTICAL_BAR = CC.GRAY + StringEscapeUtils.unescapeJava("\u2503")
    val UNICODE_CAUTION = StringEscapeUtils.unescapeJava("\u26a0")
    val UNICODE_ARROW_LEFT = StringEscapeUtils.unescapeJava("\u25C0")
    val UNICODE_ARROW_RIGHT = StringEscapeUtils.unescapeJava("\u25B6")
    val UNICODE_ARROWS_LEFT = StringEscapeUtils.unescapeJava("\u00AB")
    val UNICODE_ARROWS_RIGHT = StringEscapeUtils.unescapeJava("\u00BB")
    val UNICODE_HEART = StringEscapeUtils.unescapeJava("\u2764")
    val MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------"
    val CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------"
    val SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------"

    fun translate(`in`: String): String {
        return ChatColor.translateAlternateColorCodes('&', `in`)
    }

    fun translate(lines: List<String>): List<String> {
        val toReturn = ArrayList<String>()

        for (line in lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line))
        }

        return toReturn
    }

    fun translate(lines: Array<String>): List<String> {
        val toReturn = ArrayList<String>()

        for (line in lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line))
            }
        }

        return toReturn
    }

}