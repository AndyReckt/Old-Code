/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.motd

data class MotdState(val name: String, val motd: String) {

    var countdown: Long? = null
    var countdownFinishMotd: String? = null

}