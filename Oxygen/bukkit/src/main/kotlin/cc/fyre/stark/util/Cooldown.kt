/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

/**
 * Created by DaddyDombo daddydombo@gmail.com on 1/20/2020.
 */
import cc.fyre.stark.core.util.TimeUtils
import lombok.Data

@Data
class Cooldown(duration: Long) {

    var start = System.currentTimeMillis()
        set(start) {
            field = this.start
        }
    private val expire: Long
    private var notified: Boolean = false

    val passed: Long
        get() = System.currentTimeMillis() - this.start

    val remaining: Long
        get() = this.expire - System.currentTimeMillis()

    val timeLeft: String
        get() = if (this.remaining >= 60000) {
            TimeUtils.formatLongIntoHHMMSS(this.remaining)
        } else {
            TimeUtils.formatLongIntoMMSS(this.remaining)
        }

    init {
        this.expire = this.start + duration

        if (duration == 0L) {
            this.notified = true
        }
    }

    fun hasExpired(): Boolean {
        return System.currentTimeMillis() - this.expire >= 0
    }

}
