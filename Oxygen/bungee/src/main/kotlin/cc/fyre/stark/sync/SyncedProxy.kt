/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.sync

data class SyncedProxy(val proxyId: String, var online: Int = 0, var lastUpdate: Long = System.currentTimeMillis()) {

    fun isOnline(): Boolean {
        return System.currentTimeMillis() - lastUpdate < 5000
    }

}