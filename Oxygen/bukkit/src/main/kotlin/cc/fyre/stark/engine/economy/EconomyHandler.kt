/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.economy

import cc.fyre.stark.Stark
import java.util.*

class EconomyHandler {

    val balances: MutableMap<UUID, Double> = hashMapOf()

    fun load() {
        Stark.instance.server.pluginManager.registerEvents(EconomyListeners(this), Stark.instance)

        Stark.instance.core.redis.runRedisCommand { jedis ->
            jedis.keys("balance.*").forEach { key ->
                balances[UUID.fromString(key.substring(8))] = jedis.get(key).toDouble()
            }
        }
    }

    fun getBalance(uuid: UUID): Double {
        return balances[uuid] ?: 0.0
    }

    fun setBalance(uuid: UUID, balance: Double) {
        if (balance < 0) {
            throw IllegalArgumentException("Balance cannot be negative")
        }
        balances[uuid] = balance
        save(uuid)
    }

    fun withdraw(uuid: UUID, amount: Double) {
        val balance = getBalance(uuid).minus(amount)
        if (balance < 0) {
            throw IllegalArgumentException("Balance cannot be negative")
        }
        setBalance(uuid, balance)
        save(uuid)
    }

    fun deposit(uuid: UUID, amount: Double) {
        val balance = getBalance(uuid).plus(amount)
        if (amount < 0) { // if this happens, just why...
            throw IllegalStateException("$amount is a negative number. Use EconomyHandler#subtract")
        }
        setBalance(uuid, balance)
        save(uuid)
    }

    fun save(uuid: UUID) {
        Stark.instance.core.redis.runRedisCommand { redis ->
            redis.set("balance.$uuid", getBalance(uuid).toString())
        }
    }
}