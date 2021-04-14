/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.modsuite

import cc.fyre.stark.Stark
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit

object ModRequestHandler {

    fun hasRequestCooldown(player: Player): Boolean {
        val key = "stark:request:" + player.uniqueId + ":cooldown"

        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.exists(key)
        }
    }

    fun addRequestCooldown(player: Player, time: Long, unit: TimeUnit) {
        val key = "stark:request:" + player.uniqueId + ":cooldown"

        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.setex(key, unit.toSeconds(time).toInt(), "")
        }
    }

    fun getReportCount(target: UUID): Int {
        val key = "stark:request:$target:reports"
        val time = System.currentTimeMillis()

        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.zcount(key, (time - TimeUnit.MINUTES.toMillis(15L)).toDouble(), time.toDouble())!!.toInt()
        }
    }

    fun incrementReportCount(target: Player) {
        val key = "stark:request:" + target.uniqueId + ":reports"
        val time = System.currentTimeMillis()

        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.zadd(key, time.toDouble(), time.toString())
            redis.expire(key, TimeUnit.MINUTES.toMillis(15L).toInt())
        }
    }

}
