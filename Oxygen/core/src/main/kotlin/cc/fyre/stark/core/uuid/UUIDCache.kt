/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.uuid

import cc.fyre.stark.core.StarkCore
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class UUIDCache {

    private var uuidToName = ConcurrentHashMap<UUID, String>()
    private var nameToUuid = ConcurrentHashMap<String, UUID>()

    fun load() {
        val newUuidToName = ConcurrentHashMap<UUID, String>()
        val newNameToUuid = ConcurrentHashMap<String, UUID>()

        val cache = StarkCore.instance.redis.runBackboneRedisCommand { redis ->
            redis.hgetAll("UUIDCache")
        }

        cache!!.forEach { (key, name) ->
            val uuid = UUID.fromString(key)
            newUuidToName[uuid] = name
            newNameToUuid[name.toLowerCase()] = uuid
        }

        uuidToName = newUuidToName
        nameToUuid = newNameToUuid
    }

    fun uuid(name: String): UUID? {
        return nameToUuid[name.toLowerCase()]
    }

    fun name(uuid: UUID): String {
        return uuidToName[uuid] ?: "Unknown"
    }

    fun ensure(uuid: UUID) {

    }

    fun update(uuid: UUID, name: String) {
        val toRemove = ArrayList<String>()

        nameToUuid.forEach { (key, value) ->
            if (value === uuid) {
                toRemove.add(key)
            }
        }

        for (remove in toRemove) {
            nameToUuid.remove(remove)
        }

        uuidToName[uuid] = name
        nameToUuid[name.toLowerCase()] = uuid

        StarkCore.instance.redis.runBackboneRedisCommand { redis ->
            redis.hset("UUIDCache", uuid.toString(), name)
        }
    }

    fun cache(uuid: UUID, name: String) {
        uuidToName[uuid] = name
        nameToUuid[name.toLowerCase()] = uuid
    }

    fun reset() {
        nameToUuid.clear()
        uuidToName.clear()
    }

}