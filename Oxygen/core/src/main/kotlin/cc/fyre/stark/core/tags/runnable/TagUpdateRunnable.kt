/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.tags.runnable

import cc.fyre.stark.core.StarkCore

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
class TagUpdateRunnable : Runnable {

    override fun run() {
        StarkCore.instance.tagHandler.refreshTags()
    }
}