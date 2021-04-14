/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.whitelist

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.pidgin.message.Message
import java.util.*

class Whitelist {

    var modeType: WhitelistType = WhitelistType.NONE
    var verified: HashSet<UUID> = hashSetOf()

    fun load() {
        StarkCore.instance.redis.runBackboneRedisCommand {
            if (it.exists("stark:whitelistMode")) {
                modeType = WhitelistType.valueOf(it.get("stark:whitelistMode"))
            }

            verified.addAll(it.smembers("stark:verifiedStatus").map { member -> UUID.fromString(member) })
        }
    }

    fun setMode(type: WhitelistType, update: Boolean) {
        modeType = type

        if (update) {
            StarkCore.instance.redis.runBackboneRedisCommand {
                it.set("stark:whitelistMode", type.toString())
            }
        }
    }

    fun getWhitelist(uuid: UUID): WhitelistType {
        return StarkCore.instance.redis.runBackboneRedisCommand {
            val retrieved = it.get("stark:whitelistAccess:player.$uuid")

            if (retrieved == null || retrieved.isEmpty()) {
                WhitelistType.values()[0]
            } else {
                WhitelistType.valueOf(retrieved)
            }
        }
    }

    fun setWhitelist(uuid: UUID, type: WhitelistType) {
        StarkCore.instance.redis.runBackboneRedisCommand {
            it.set("stark:whitelistAccess:player.$uuid", type.toString())
        }
    }

    fun getWhitelistTokens(uuid: UUID): Int {
        return StarkCore.instance.redis.runBackboneRedisCommand {
            val retrieved = it.get("stark:whitelistTokens:player.$uuid")

            if (retrieved == null) {
                0
            } else {
                val toInt = retrieved.toInt()

                if (toInt <= 0) {
                    0
                } else {
                    toInt
                }
            }
        }
    }

    fun setWhitelistTokens(uuid: UUID, tokens: Int) {
        StarkCore.instance.redis.runBackboneRedisCommand {
            it.set("stark:whitelistTokens:player.$uuid", tokens.toString())
        }
    }

    fun setVerified(uuid: UUID, status: Boolean): Boolean {
        val successful = if (status) {
            verified.add(uuid)
        } else {
            verified.remove(uuid)
        }

        if (successful) {
            StarkCore.instance.redis.runBackboneRedisCommand {
                if (status) {
                    it.sadd("stark:verifiedStatus", uuid.toString())
                } else {
                    it.srem("stark:verifiedStatus", uuid.toString())
                }
            }

            StarkCore.instance.globalMessageChannel.sendMessage(Message("VERIFIED_UPDATE", mapOf("status" to status, "playerUuid" to uuid.toString())))
        }

        return successful
    }

}