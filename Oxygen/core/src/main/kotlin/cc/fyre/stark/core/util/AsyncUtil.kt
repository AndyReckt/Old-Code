/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.util

import cc.fyre.stark.core.StarkCore

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
object AsyncUtil {

    @JvmStatic
    fun ensureAsync() {
        if (StarkCore.instance.isPrimaryThread()) {
            throw IllegalStateException("Cannot query databases on primary thread")
        }
    }
}