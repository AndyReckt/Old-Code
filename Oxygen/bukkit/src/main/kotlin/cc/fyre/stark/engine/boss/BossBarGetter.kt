/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.boss

import org.bukkit.entity.Player

abstract class BossBarGetter {
    abstract fun getBossBar(player: Player): BossBar?

    class BossBar(var player: Player, var msg: String, var health: Float)

    companion object {
        @JvmStatic
        fun create(player: Player): BossBar = BossBar(player, "", 1.toFloat())
    }
}