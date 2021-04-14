/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.reboot

import cc.fyre.stark.Stark
import java.util.*
import java.util.concurrent.TimeUnit

class RebootHandler {

    var rebootTimes: List<Int> = ArrayList()
    var rebootTask: RebootTask? = null

    fun isRebooting(): Boolean {
        return rebootTask != null
    }

    fun rebootSecondsRemaining(): Int {
        return if (rebootTask == null) {
            -1
        } else {
            rebootTask!!.secondsRemaining
        }
    }

    fun load() {
        rebootTimes = Stark.instance.config.getIntegerList("RebootTimes")
    }

    fun rebootServer(seconds: Int) {
        rebootServer(seconds, TimeUnit.SECONDS)
    }

    fun rebootServer(timeUnitAmount: Int, timeUnit: TimeUnit) {
        if (rebootTask != null) {
            throw IllegalStateException("Reboot already in progress")
        }

        rebootTask = RebootTask(timeUnitAmount, timeUnit)
        rebootTask!!.runTaskTimer(Stark.instance, 20L, 20L)
    }

    fun cancelReboot() {
        if (rebootTask != null) {
            rebootTask!!.cancel()
            rebootTask = null
        }
    }

}