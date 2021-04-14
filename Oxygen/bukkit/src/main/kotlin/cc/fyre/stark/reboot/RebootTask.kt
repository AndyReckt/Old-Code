/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.reboot

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.TimeUnit

class RebootTask(timeUnitAmount: Int, timeUnit: TimeUnit) : BukkitRunnable() {

    var secondsRemaining: Int = timeUnit.toSeconds(timeUnitAmount.toLong()).toInt()

    override fun run() {
        if (this.secondsRemaining == 0) {
            Stark.instance.server.shutdown()
        }

        when (this.secondsRemaining) {
            5, 10, 15, 30, 60, 120, 180, 240, 300 -> {
                Stark.instance.server.broadcastMessage("")
                Stark.instance.server.broadcastMessage("${ChatColor.RED}${ChatColor.BOLD}⚠ ${ChatColor.YELLOW}Server rebooting in " + TimeUtils.formatIntoDetailedString(this.secondsRemaining) + "!")
                Stark.instance.server.broadcastMessage("")
            }
        }
        --this.secondsRemaining
    }

    @Synchronized
    @Throws(IllegalStateException::class)
    override fun cancel() {
        super.cancel()
    }

}