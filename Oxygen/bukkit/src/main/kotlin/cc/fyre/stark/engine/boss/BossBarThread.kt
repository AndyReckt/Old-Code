/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.boss

import cc.fyre.stark.Stark
import org.bukkit.Bukkit

class BossBarThread : Thread() {
    init {
        this.isDaemon = true
    }

    override fun run() {
        while (Stark.instance.bossbarEngine.handler != null) {
            if (Stark.instance.bossbarEngine.bossbargetter != null) {
                for (x in Bukkit.getOnlinePlayers()) {
                    val bossbar = Stark.instance.bossbarEngine.bossbargetter!!.getBossBar(x)
                    if (bossbar != null) {
                        if (bossbar.health > 1f) bossbar.health = 1f
                        if (bossbar.health < 0f) bossbar.health = 0f
                        Stark.instance.bossbarEngine.handler!!.setBossBar(x, bossbar.msg, bossbar.health)
                    } else if (Stark.instance.bossbarEngine.handler!!.hasBossBar(x)) {
                        Stark.instance.bossbarEngine.handler!!.removeBossBar(x)
                    }
                }
            }

            try {
                sleep(2 * 50L)
            } catch (e2: InterruptedException) {
                e2.printStackTrace()
            }
        }
    }
}