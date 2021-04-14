/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark

import cc.fyre.stark.availability.AvailabilityHeartbeatRunnable
import cc.fyre.stark.availability.AvailabilityListeners
import cc.fyre.stark.command.HubCommand
import cc.fyre.stark.command.ReloadCommand
import cc.fyre.stark.command.SetMotdStateCommand
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.mongo.MongoCredentials
import cc.fyre.stark.core.pidgin.Pidgin
import cc.fyre.stark.core.profile.ProfileHandler
import cc.fyre.stark.core.redis.RedisCredentials
import cc.fyre.stark.core.uuid.UUIDCacheLoadRunnable
import cc.fyre.stark.motd.MotdHandler
import cc.fyre.stark.motd.MotdListeners
import cc.fyre.stark.profile.ProfileCheckRunnable
import cc.fyre.stark.profile.ProfileListeners
import cc.fyre.stark.profile.ProfileMessageListeners
import cc.fyre.stark.profile.ProxyProfile
import cc.fyre.stark.staff.StaffListeners
import cc.fyre.stark.staff.StaffMessageListeners
import cc.fyre.stark.sync.SyncHandler
import cc.fyre.stark.sync.SyncListeners
import cc.fyre.stark.sync.SyncMessageListeners
import cc.fyre.stark.sync.runnable.BroadcastCountRunnable
import cc.fyre.stark.sync.runnable.HeartbeatProxyRunnable
import cc.fyre.stark.sync.runnable.RankSyncRunnable
import cc.fyre.stark.whitelist.WhitelistListeners
import cc.fyre.stark.whitelist.WhitelistMessageListeners
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit

class Stark : Plugin() {

    lateinit var configuration: Configuration
    lateinit var core: StarkCore<ProxyProfile>

    var syncHandler: SyncHandler = SyncHandler()
    var motdHandler: MotdHandler = MotdHandler()
    lateinit var proxyMessageChannel: Pidgin

    lateinit var mainThread: Thread

    override fun onEnable() {
        instance = this
        mainThread = Thread.currentThread()

        try {
            saveDefaultConfig()
            loadConfig()

            core = object : StarkCore<ProxyProfile>(proxy.logger) {
                val profileHandler = object : ProfileHandler<ProxyProfile>() {
                    override fun createProfileInstance(uuid: UUID): ProxyProfile {
                        return ProxyProfile(uuid)
                    }
                }

                override fun isPrimaryThread(): Boolean {
                    return Thread.currentThread() == mainThread
                }

                override fun getProfileHandler(): ProfileHandler<ProxyProfile> {
                    return profileHandler
                }
            }

            core.load(configuration.getString("TimeZone"),
                    getRedisCredentials("Local"),
                    getRedisCredentials("Backbone"),
                    getMongoCredentials())

            proxy.servers.values.forEach {
                core.servers.loadOrCreateServer(it.name, it.address.port)
            }

            core.globalMessageChannel.registerListener(ProfileMessageListeners())
            core.globalMessageChannel.registerListener(WhitelistMessageListeners())

            proxyMessageChannel = Pidgin("Stark:PROXY", core.redis.backboneJedisPool!!)
            proxyMessageChannel.registerListener(SyncMessageListeners())
            proxyMessageChannel.registerListener(StaffMessageListeners())

            motdHandler.loadStates()

            registerListeners()
            registerCommands()

            proxy.scheduler.schedule(this, HeartbeatProxyRunnable(), 1, 1, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, BroadcastCountRunnable(), 500, 500, TimeUnit.MILLISECONDS)
            proxy.scheduler.schedule(this, AvailabilityHeartbeatRunnable(), 1, 5, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, ProfileCheckRunnable(), 1, 3, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, RankSyncRunnable(), 1, 30, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, { proxy.scheduler.runAsync(this, UUIDCacheLoadRunnable()) }, 2, 2, TimeUnit.MINUTES)
        } catch (e: Exception) {
            logger.severe("An error occurred on startup")
            e.printStackTrace()
            proxy.stop()
        }
    }

    override fun onDisable() {
        core.redis.close()
        core.mongo.close()
    }


    fun isPunishmentServer(server: ServerInfo): Boolean {
        return server.name.startsWith("Punishments-Hub")
    }

    fun isHubServer(server: ServerInfo): Boolean {
        return server.name.startsWith("Hub-")
    }

    fun isRestrictedHubServer(server: ServerInfo): Boolean {
        return server.name.startsWith("Restricted-Hub")
    }

    private fun saveDefaultConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            try {
                configFile.createNewFile()
                getResourceAsStream("config.yml").use { `is` -> FileOutputStream(configFile).use { os -> ByteStreams.copy(`is`, os) } }
            } catch (e: Throwable) {
                throw RuntimeException("Unable to create configuration file", e)
            }
        }
    }

    private fun loadConfig() {
        configuration = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))
    }

    fun reloadConfig() {
        loadConfig()
        motdHandler.loadStates()
    }

    private fun registerListeners() {
        proxy.pluginManager.registerListener(this, SyncListeners())
        proxy.pluginManager.registerListener(this, WhitelistListeners())
        proxy.pluginManager.registerListener(this, MotdListeners())
        proxy.pluginManager.registerListener(this, AvailabilityListeners())
        proxy.pluginManager.registerListener(this, ProfileListeners())
        proxy.pluginManager.registerListener(this, StaffListeners())
    }

    private fun registerCommands() {
        proxy.pluginManager.registerCommand(this, ReloadCommand())
        proxy.pluginManager.registerCommand(this, SetMotdStateCommand())
        proxy.pluginManager.registerCommand(this, HubCommand())
    }

    private fun getRedisCredentials(prefix: String): RedisCredentials {
        val builder = RedisCredentials.Builder()
                .host(configuration.getString("${prefix}Redis.Host"))
                .port(configuration.getInt("${prefix}Redis.Port"))

        if (configuration.contains("${prefix}Redis.Password")) {
            builder.password(configuration.getString("${prefix}Redis.Password"))
        }

        if (configuration.contains("${prefix}Redis.DbId")) {
            builder.dbId(configuration.getInt("${prefix}Redis.DbId"))
        }

        return builder.build()
    }

    private fun getMongoCredentials(): MongoCredentials {
        val builder = MongoCredentials.Builder()
                .host(configuration.getString("Mongo.Host"))
                .port(configuration.getInt("Mongo.Port"))

        if (configuration.contains("Mongo.Username")) {
            builder.username(configuration.getString("Mongo.Username"))
        }

        if (configuration.contains("Mongo.Password")) {
            builder.password(configuration.getString("Mongo.Password"))
        }

        return builder.build()
    }

    companion object {
        lateinit var instance: Stark
    }

}