/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.boss

import cc.fyre.stark.Stark

class BossBarEngine {
    var bossbargetter: BossBarGetter? = null
    var handler: BossBarHandler? = null

    fun load() {
        if (Stark.instance.config.getBoolean("disableBossBar", false)) {
            Stark.instance.logger.info("BossBar is disabled by config")
            return
        }
        handler = BossBarHandler()
        handler!!.init()

        BossBarThread().start()
    }


}