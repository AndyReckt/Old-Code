/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent

object ChatUtil {

    fun color(string: String): String {
        return ChatColor.translateAlternateColorCodes('&', string)
    }

    fun compile(components: Array<BaseComponent>): BaseComponent {
        val root = components[0]
        var current = components[0]

        for (i in 1 until components.size) {
            current.addExtra(components[i])
            current = components[i]
        }

        return root
    }

}