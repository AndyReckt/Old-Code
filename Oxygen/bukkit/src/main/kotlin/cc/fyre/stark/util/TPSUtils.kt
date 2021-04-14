/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import org.bukkit.ChatColor
import java.text.DecimalFormat

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2020.
 */
class TPSUtils : Runnable {
    override fun run() {
        TICKS[TICK_COUNT % TICKS.size] = System.currentTimeMillis()
        ++TICK_COUNT
    }

    companion object {
        private var TICK_COUNT = 0
        private val TICKS = LongArray(600)
        val tps: Double
            get() = getTPS(100)

        fun getTPS(ticks: Int): Double {
            if (TICKS.isEmpty() || TICK_COUNT == -1) {
                return 0.0
            }
            return if (TICK_COUNT < ticks) {
                20.0
            } else {
                try {
                    val target = (TICK_COUNT - 1 - ticks) % TICKS.size
                    val elapsed = System.currentTimeMillis() - TICKS[target]
                    ticks.toDouble() / (elapsed.toDouble() / 1000.0)
                } catch (e: ArrayIndexOutOfBoundsException) {
                    0.0
                }
            }
        }

        fun prettyTPS(tpsdouble: Double, format: DecimalFormat): String {
            if (tpsdouble > 18) {
                return ChatColor.GREEN.toString() + format.format(tpsdouble)
            }
            if (tpsdouble > 15) {
                return ChatColor.DARK_GREEN.toString() + format.format(tpsdouble)
            }
            if (tpsdouble > 13) {
                return ChatColor.YELLOW.toString() + format.format(tpsdouble)
            }
            if (tpsdouble > 10) {
                return ChatColor.RED.toString() + format.format(tpsdouble)
            }
            //Default DARK_RED tps
            return ChatColor.DARK_RED.toString() + format.format(tpsdouble)
        }

        fun prettyTPS(format: DecimalFormat): String {
            return prettyTPS(tps, format)
        }

        fun prettyTPS(): String {
            return prettyTPS(tps, DecimalFormat("0.00"))
        }
    }
}