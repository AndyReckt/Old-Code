/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.availability

data class AvailabilityData(val online: Boolean = false, val proxyId: String = "unknown", val serverId: String = "unknown", val lastTime: Long = -1) {

    fun isOnline(): Boolean {
        return online && System.currentTimeMillis() - lastTime <= 3_000L
    }

}