/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicReference

class TabLayout(val is18: Boolean) {

    var tabNames: Array<String> = if (is18) ZERO_VALUE_STRING_18.clone() else ZERO_VALUE_STRING.clone()
    var tabPings: IntArray = if (is18) IntArray(WIDTH * HEIGHT + 20) else IntArray(WIDTH * HEIGHT)
    var header: String = "{\"translate\":\"\"}"
    var footer: String = "{\"translate\":\"\"}"

    constructor(is18: Boolean, fill: Boolean) : this(is18) {
        if (fill) {
            for (i in this.tabNames.indices) {
                tabNames[i] = genEmpty()
                tabPings[i] = 0
            }
        }

        Arrays.sort(this.tabNames)
    }

    operator fun set(x: Int, y: Int, name: String, ping: Int) {
        if (!this.validate(x, y, true)) {
            return
        }

        val pos = if (this.is18) y + x * HEIGHT else x + y * WIDTH
        tabNames[pos] = ChatColor.translateAlternateColorCodes('&', name)
        tabPings[pos] = ping
    }

    operator fun set(x: Int, y: Int, name: String) {
        this[x, y, name] = 0
    }

    operator fun set(x: Int, y: Int, player: Player) {
        this[x, y, player.name] = (player as CraftPlayer).handle.ping
    }

    fun getStringAt(x: Int, y: Int): String {
        validate(x, y)

        val pos = if (this.is18) y + x * HEIGHT else x + y * WIDTH
        return tabNames[pos]
    }

    fun getPingAt(x: Int, y: Int): Int {
        validate(x, y)

        val pos = if (this.is18) y + x * HEIGHT else x + y * WIDTH
        return tabPings[pos]
    }

    fun validate(x: Int, y: Int, silent: Boolean): Boolean {
        if (x >= WIDTH) {
            if (!silent) {
                throw IllegalArgumentException("x >= WIDTH ($WIDTH)")
            }

            return false
        } else {
            if (y < HEIGHT) {
                return true
            }

            if (!silent) {
                throw IllegalArgumentException("y >= HEIGHT ($HEIGHT)")
            }

            return false
        }
    }

    fun validate(x: Int, y: Int): Boolean {
        return validate(x, y, false)
    }

    fun setHeaderText(header: String) {
        this.header = ComponentSerializer.toString(TextComponent(ChatColor.translateAlternateColorCodes('&', header)) as BaseComponent)
    }

    fun setFooterText(footer: String) {
        this.footer = ComponentSerializer.toString(TextComponent(ChatColor.translateAlternateColorCodes('&', footer)) as BaseComponent)
    }

    fun reset() {
        tabNames = if (is18) ZERO_VALUE_STRING_18.clone() else ZERO_VALUE_STRING.clone()
        tabPings = if (is18) IntArray(WIDTH * HEIGHT + 20) else IntArray(WIDTH * HEIGHT)
    }

    companion object {
        private var TAB_LAYOUT_1_8: AtomicReference<Any> = AtomicReference()
        private var TAB_LAYOUT_DEFAULT: AtomicReference<Any> = AtomicReference()
        private var ZERO_VALUE_STRING: Array<String> = arrayOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        private var ZERO_VALUE_STRING_18: Array<String> = arrayOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        private var tabLayouts: MutableMap<String, TabLayout> = HashMap()
        var WIDTH: Int = 3
        var HEIGHT: Int = 20
        private val emptyStrings: MutableList<String>? = arrayListOf()

        private fun genEmpty(): String {
            val colorChars = "abcdefghijpqrstuvwxyz0123456789"
            val builder = StringBuilder()

            for (i in 0..7) {
                builder.append('§').append(colorChars[ThreadLocalRandom.current().nextInt(colorChars.length)])
            }

            val s = builder.toString()
            if (emptyStrings!!.contains(s)) {
                return genEmpty()
            }
            emptyStrings.add(s)
            return s
        }

        @JvmStatic
        fun remove(player: Player) {
            tabLayouts.remove(player.name)
        }

        @JvmStatic
        fun create(player: Player): TabLayout {
            if (tabLayouts.containsKey(player.name)) {
                val layout = tabLayouts[player.name]!!
                layout.reset()
                return layout
            }

            val layout = TabLayout(TabUtils.is18(player))
            tabLayouts[player.name] = layout
            return layout
        }

        @JvmStatic
        fun createEmpty(player: Player): TabLayout {
            return if (TabUtils.is18(player)) {
                getTAB_LAYOUT_1_8()
            } else getTAB_LAYOUT_DEFAULT()
        }

        @JvmStatic
        fun getTAB_LAYOUT_1_8(): TabLayout {
            var value: Any? = TAB_LAYOUT_1_8.get()
            if (value == null) {
                synchronized(TAB_LAYOUT_1_8) {
                    value = TAB_LAYOUT_1_8.get()
                    if (value == null) {
                        val actualValue = TabLayout(true, true)
                        value = actualValue
                        TAB_LAYOUT_1_8.set(value)
                    }
                }
            }
            return (if (value === TAB_LAYOUT_1_8) null else value) as TabLayout
        }

        @JvmStatic
        fun getTAB_LAYOUT_DEFAULT(): TabLayout {
            var value = TAB_LAYOUT_DEFAULT.get()
            if (value == null) {
                synchronized(TAB_LAYOUT_DEFAULT) {
                    value = TAB_LAYOUT_DEFAULT.get()
                    if (value == null) {
                        val actualValue = TabLayout(false, true)
                        value = actualValue
                        TAB_LAYOUT_DEFAULT.set(value)
                    }
                }
            }
            return (if (value === TAB_LAYOUT_DEFAULT) null else value) as TabLayout
        }
    }

}