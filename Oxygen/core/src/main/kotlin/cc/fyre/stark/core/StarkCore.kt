/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core

import cc.fyre.stark.core.availability.AvailabilityHandler
import cc.fyre.stark.core.fetcher.HCFDataFetcher
import cc.fyre.stark.core.mongo.Mongo
import cc.fyre.stark.core.mongo.MongoCredentials
import cc.fyre.stark.core.pidgin.Pidgin
import cc.fyre.stark.core.profile.Profile
import cc.fyre.stark.core.profile.ProfileHandler
import cc.fyre.stark.core.rank.RankHandler
import cc.fyre.stark.core.redis.Redis
import cc.fyre.stark.core.redis.RedisCredentials
import cc.fyre.stark.core.server.ServerMessageListeners
import cc.fyre.stark.core.server.Servers
import cc.fyre.stark.core.tags.TagHandler
import cc.fyre.stark.core.uuid.UUIDCache
import cc.fyre.stark.core.whitelist.Whitelist
import java.util.*
import java.util.logging.Logger

abstract class StarkCore<T : Profile>(val logger: Logger) {

    lateinit var redis: Redis
    lateinit var mongo: Mongo

    val uuidCache: UUIDCache = UUIDCache()
    val servers: Servers = Servers()
    val rankHandler: RankHandler = RankHandler()
    val availabilityHandler: AvailabilityHandler = AvailabilityHandler()
    val whitelist: Whitelist = Whitelist()
    val tagHandler: TagHandler = TagHandler()
    var hcfdataFetcher: HCFDataFetcher? = null

    lateinit var globalMessageChannel: Pidgin

    init {
        instance = this
    }

    fun load(timezone: String, localRedisCredentials: RedisCredentials, backboneRedisCredentials: RedisCredentials, mongoCredentials: MongoCredentials) {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone))

        redis = Redis()
        redis.load(localRedisCredentials, backboneRedisCredentials)

        mongo = Mongo("stark")
        mongo.load(mongoCredentials)

        uuidCache.load()
        servers.initialLoad()
        rankHandler.load()
        whitelist.load()
        tagHandler.load()

        getProfileHandler().load()

        globalMessageChannel = Pidgin("Stark:ALL", redis.backboneJedisPool!!)
        globalMessageChannel.registerListener(ServerMessageListeners)
    }

    abstract fun isPrimaryThread(): Boolean

    abstract fun getProfileHandler(): ProfileHandler<T>

    companion object {
        lateinit var instance: StarkCore<*>

        const val COLOR_CODE_CHAR = '§'
    }

}