/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark

import cc.fyre.stark.availability.AvailabilityHeartbeatRunnable
import cc.fyre.stark.availability.AvailabilityListeners
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.mongo.MongoCredentials
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.ProfileHandler
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.core.rank.runnable.RankLoadRunnable
import cc.fyre.stark.core.redis.RedisCredentials
import cc.fyre.stark.core.tags.runnable.TagUpdateRunnable
import cc.fyre.stark.core.uuid.UUIDCacheLoadRunnable
import cc.fyre.stark.engine.boss.BossBarEngine
import cc.fyre.stark.engine.command.CommandHandler
import cc.fyre.stark.engine.economy.EconomyHandler
import cc.fyre.stark.engine.menu.ButtonListeners
import cc.fyre.stark.engine.nametag.NametagEngine
import cc.fyre.stark.engine.nametag.impl.StarkNametagProvider
import cc.fyre.stark.engine.protocol.InventoryAdapter
import cc.fyre.stark.engine.protocol.LagCheck
import cc.fyre.stark.engine.protocol.PingAdapter
import cc.fyre.stark.engine.scoreboard.ScoreboardEngine
import cc.fyre.stark.engine.scoreboard.ScoreboardListeners
import cc.fyre.stark.engine.sign.SignAdapter
import cc.fyre.stark.engine.sign.SignGUI
import cc.fyre.stark.engine.tab.TabAdapter
import cc.fyre.stark.engine.tab.TabEngine
import cc.fyre.stark.engine.tab.TabListeners
import cc.fyre.stark.engine.visibility.VisibilityEngine
import cc.fyre.stark.filter.Filter
import cc.fyre.stark.filter.FilterListener
import cc.fyre.stark.inventory.TrackedPlayerInventoryListeners
import cc.fyre.stark.messaging.MessagingManager
import cc.fyre.stark.modsuite.ModSuiteMessageListeners
import cc.fyre.stark.modsuite.options.ModOptionsListeners
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.ProfileListeners
import cc.fyre.stark.profile.ProfileMessageListeners
import cc.fyre.stark.profile.ProfileParameterType
import cc.fyre.stark.profile.grant.ProfileGrantListeners
import cc.fyre.stark.profile.runnable.ProfileHeartbeatRunnable
import cc.fyre.stark.rank.RankParameterType
import cc.fyre.stark.reboot.RebootHandler
import cc.fyre.stark.reboot.RebootListener
import cc.fyre.stark.server.ServerHandler
import cc.fyre.stark.server.ServerSyncRunnable
import cc.fyre.stark.server.ServerUpdateListener
import cc.fyre.stark.server.data.HCFDataLivesUpdateRunnable
import cc.fyre.stark.server.handler.DataFetcherHandler
import cc.fyre.stark.server.listener.*
import cc.fyre.stark.util.CC
import cc.fyre.stark.util.TPSUtils
import cc.fyre.stark.util.event.HourEvent
import cc.fyre.stark.util.serialization.*
import cc.fyre.stark.uuid.UUIDListeners
import com.comphenix.protocol.ProtocolLibrary
import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.util.BlockVector
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Stark : JavaPlugin() {

    lateinit var core: StarkCore<BukkitProfile>

    val serverHandler: ServerHandler = ServerHandler()
    val messagingManager: MessagingManager = MessagingManager()
    val rebootHandler: RebootHandler = RebootHandler()
    val commandHandler: CommandHandler = CommandHandler()
    val nametagEngine: NametagEngine = NametagEngine()
    val visibilityEngine: VisibilityEngine = VisibilityEngine()
    val tabEngine: TabEngine = TabEngine()
    val scoreboardEngine: ScoreboardEngine = ScoreboardEngine()
    val economyHandler: EconomyHandler = EconomyHandler()
    val bossbarEngine: BossBarEngine = BossBarEngine()
    val random: Random = Random()
    var enabledAt: Long = -1
    val filter: Filter = Filter()
    val dataFetcherHandler: DataFetcherHandler = DataFetcherHandler()

    override fun onEnable() {
        instance = this
        enabledAt = System.currentTimeMillis()

        try {
            saveDefaultConfig()

            core = object : StarkCore<BukkitProfile>(logger) {
                val profileHandler = object : ProfileHandler<BukkitProfile>() {
                    override fun createProfileInstance(uuid: UUID): BukkitProfile {
                        return BukkitProfile(uuid)
                    }
                }

                override fun isPrimaryThread(): Boolean {
                    return Bukkit.isPrimaryThread()
                }

                override fun getProfileHandler(): ProfileHandler<BukkitProfile> {
                    return profileHandler
                }
            }

            core.load(config.getString("TimeZone"),
                    getRedisCredentials("Local"),
                    getRedisCredentials("Backbone"),
                    getMongoCredentials())

            if (!core.servers.getServerByName(Bukkit.getServerName()).isPresent) {
                core.servers.loadOrCreateServer(Bukkit.getServerName(), Bukkit.getPort())
            }

            checkForServerMismatch()
            checkServerGroup()

            core.globalMessageChannel.registerListener(ModSuiteMessageListeners())
            core.globalMessageChannel.registerListener(ProfileMessageListeners())
            core.globalMessageChannel.registerListener(ServerUpdateListener())

            loadEngine()

            registerListeners()
            registerCommands()

            setupHourEvents()
            SignGUI()

            server.scheduler.runTaskTimerAsynchronously(this, ProfileHeartbeatRunnable(), 20L * 30, 20L * 30)
            server.scheduler.runTaskTimerAsynchronously(this, UUIDCacheLoadRunnable(), 20L * 120 * 2, 20L * 120)
            server.scheduler.runTaskTimerAsynchronously(this, RankLoadRunnable(), 20L * 60, 20L * 60)
            server.scheduler.runTaskTimerAsynchronously(this, TagUpdateRunnable(), 20L * 30L, 20L * 30L)
            server.scheduler.runTaskTimerAsynchronously(this, AvailabilityHeartbeatRunnable(), 20L * 3, 20L * 3)
            server.scheduler.runTaskTimerAsynchronously(this, ServerSyncRunnable(), 40L, 40L)
            server.scheduler.runTaskTimerAsynchronously(this, HCFDataLivesUpdateRunnable(), 60L, 20L * 60)
            server.scheduler.scheduleSyncRepeatingTask(this, TPSUtils(), 1L, 1L)

            server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

            logger.info("Finished loading stark in ${((System.currentTimeMillis() - enabledAt) / 1000)}ms")

            // Broadcast Server Initialized Message
            val data = mapOf(
                    "serverName" to Bukkit.getServerName(),
                    "port" to server.port,
                    "whitelisted" to server.hasWhitelist()
            )

            instance.core.globalMessageChannel.sendMessage(Message("SERVER_START", data))
        } catch (e: Exception) {
            logger.severe("An error occurred on startup")
            e.printStackTrace()
            server.shutdown()
        }
    }

    override fun onDisable() {
        val data = mapOf(
                "serverName" to Bukkit.getServerName()
        )

        instance.core.globalMessageChannel.sendMessage(Message("SERVER_STOP", data))

        for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
            onlinePlayer.kickPlayer(CC.translate("&4Server is restarting!\n&cYou will be able to reconnect shortly."))
        }

        core.tagHandler.saveAll()
        core.rankHandler.saveAllRanks()
        core.redis.close()
        core.mongo.close()
    }

    private fun loadEngine() {
        serverHandler.load()
        rebootHandler.load()
        bossbarEngine.load()
        commandHandler.load()
        nametagEngine.load()
        nametagEngine.registerProvider(StarkNametagProvider())
        visibilityEngine.load()
        tabEngine.load()
        scoreboardEngine.load()
        economyHandler.load()


        val pingAdapter = PingAdapter()

        ProtocolLibrary.getProtocolManager().addPacketListener(pingAdapter)
        ProtocolLibrary.getProtocolManager().addPacketListener(InventoryAdapter())
        ProtocolLibrary.getProtocolManager().addPacketListener(TabAdapter())
        ProtocolLibrary.getProtocolManager().addPacketListener(SignAdapter())

        Bukkit.getPluginManager().registerEvents(pingAdapter, this)

        LagCheck().runTaskTimerAsynchronously(this, 100L, 100L)
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(ButtonListeners(), this)
        pm.registerEvents(UUIDListeners(), this)
        pm.registerEvents(AvailabilityListeners(), this)
        pm.registerEvents(ProfileListeners(), this)
        pm.registerEvents(ProfileGrantListeners(), this)
        pm.registerEvents(RebootListener(), this)
        pm.registerEvents(DisallowedCommandsListeners(), this)
        pm.registerEvents(FreezeListeners(), this)
        pm.registerEvents(FrozenServerListeners(), this)
        pm.registerEvents(FrozenPlayerListeners(), this)
        pm.registerEvents(HeadNameListeners(), this)
        pm.registerEvents(ColoredSignListeners(), this)
        pm.registerEvents(TeleportationListeners(), this)
        pm.registerEvents(TrackedPlayerInventoryListeners(), this)
        pm.registerEvents(ModOptionsListeners(), this)
        pm.registerEvents(TabListeners(), this)
        pm.registerEvents(ScoreboardListeners(), this)
        pm.registerEvents(ChatFilterListeners(), this)
        pm.registerEvents(FilterListener(), this)
    }

    private fun registerCommands() {
        commandHandler.registerAll(this)
        commandHandler.registerParameterType(BukkitProfile::class.java, ProfileParameterType())
        commandHandler.registerParameterType(Rank::class.java, RankParameterType())
    }

    private fun checkForServerMismatch() {
        if (!core.servers.checkNamePortMatch(Bukkit.getServerName(), Bukkit.getPort())) {
            logger.severe("********************************************************")
            logger.severe("Can't start server because server.properties config doesn't match the bungee config.yml.")
            logger.severe("Make sure the `server-id` value in `server.properties` matches the server-id assigned to")
            logger.severe("this server's port in your bungee config.yml.")
            logger.severe("********************************************************")
            server.pluginManager.disablePlugin(this)
            server.shutdown()
        }
    }

    private fun checkServerGroup() {
        if (Bukkit.getServerGroup() == null) {
            logger.severe("********************************************************")
            logger.severe("Can't start server. (Reason: No server group specified)")
            logger.severe("********************************************************")
            server.pluginManager.disablePlugin(this)
            server.shutdown()
        }
    }

    private fun setupHourEvents() {
        val executor = Executors.newSingleThreadScheduledExecutor(ThreadFactoryBuilder().setNameFormat("stark - Hour Event Thread").setDaemon(true).build())
        val minOfHour = Calendar.getInstance().get(12)
        val minToHour = 60 - minOfHour
        executor.scheduleAtFixedRate({ instance.server.scheduler.runTask(instance) { Bukkit.getPluginManager().callEvent(HourEvent(Calendar.getInstance().get(11))) } }, minToHour.toLong(), 60L, TimeUnit.MINUTES)
    }

    private fun getRedisCredentials(prefix: String): RedisCredentials {
        val builder = RedisCredentials.Builder()
                .host(config.getString("${prefix}Redis.Host"))
                .port(config.getInt("${prefix}Redis.Port"))

        if (config.contains("${prefix}Redis.Password")) {
            builder.password(config.getString("${prefix}Redis.Password"))
        }

        if (config.contains("${prefix}Redis.DbId")) {
            builder.dbId(config.getInt("${prefix}Redis.DbId"))
        }

        return builder.build()
    }

    private fun getMongoCredentials(): MongoCredentials {
        val builder = MongoCredentials.Builder()
                .host(config.getString("Mongo.Host"))
                .port(config.getInt("Mongo.Port"))

        if (config.contains("Mongo.Username")) {
            builder.username(config.getString("Mongo.Username"))
        }

        if (config.contains("Mongo.Password")) {
            builder.password(config.getString("Mongo.Password"))
        }

        return builder.build()
    }

    companion object {

        @JvmStatic
        lateinit var instance: Stark

        @JvmStatic
        val gson = GsonBuilder()
                .registerTypeHierarchyAdapter(PotionEffect::class.java, PotionEffectAdapter())
                .registerTypeHierarchyAdapter(ItemStack::class.java, ItemStackAdapter())
                .registerTypeHierarchyAdapter(Location::class.java, LocationAdapter())
                .registerTypeHierarchyAdapter(org.bukkit.util.Vector::class.java, VectorAdapter())
                .registerTypeAdapter(BlockVector::class.java, BlockVectorAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create()

        @JvmStatic
        val plainGson = GsonBuilder()
                .registerTypeHierarchyAdapter(PotionEffect::class.java, PotionEffectAdapter())
                .registerTypeHierarchyAdapter(ItemStack::class.java, ItemStackAdapter())
                .registerTypeHierarchyAdapter(Location::class.java, LocationAdapter())
                .registerTypeHierarchyAdapter(Vector::class.java, VectorAdapter())
                .registerTypeAdapter(BlockVector::class.java, BlockVectorAdapter())
                .serializeNulls()
                .create()
    }
}