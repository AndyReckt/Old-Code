/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.profile.punishment.ProfilePunishment
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentSerializer
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.core.util.mojanguser.MojangUser
import com.google.gson.JsonParser
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.bson.Document
import java.util.*
import java.util.stream.Collectors

abstract class ProfileHandler<T : Profile> {

    lateinit var collection: MongoCollection<Document>
    val profiles = HashMap<UUID, T>()

    fun load() {
        collection = StarkCore.instance.mongo.database.getCollection("profiles")
        collection.createIndex(Document("uuid", 1))
        collection.createIndex(Document("ipAddress", 1))
    }

    abstract fun createProfileInstance(uuid: UUID): T

    fun getByUUID(uuid: UUID): T? {
        return this.profiles[uuid]
    }

    fun loadProfile(uuid: UUID): T {
        return loadProfile(uuid, null)
    }

    fun loadProfile(uuid: UUID, ipAddress: String?): T {
        if (StarkCore.instance.isPrimaryThread()) {
            throw IllegalStateException("Cannot query mongo on primary thread")
        }

        if (this.profiles.containsKey(uuid)) {
            return this.profiles[uuid]!!
        }

        val profile = createProfileInstance(uuid)
        var save = false

        val document = collection.find(Document("uuid", uuid.toString())).first()

        if (document != null) {
            profile.update(document)
        } else {
            save = true
        }

        if (ipAddress != null) {
            // add ip to ip history
            if (!profile.ipAddresses.contains(ipAddress)) {
                save = true
                profile.ipAddresses.add(ipAddress)
            }
        }

        if (save) {
            saveProfile(profile)
        }

        return profile
    }

    fun pullProfileUpdates(uuid: UUID): T {
        val profile: Profile

        if (profiles.containsKey(uuid)) {
            profile = profiles[uuid]!!
            profile.update(collection.find(Document("uuid", uuid.toString())).first()!!)
        } else {
            profile = loadProfile(uuid)
        }

        profile.apply()

        return profile
    }

    fun saveProfile(profile: T) {
        val grants = profile.grants.stream().map<Document> { it.toDocument() }.collect(Collectors.toList())
        val punishments = profile.punishments.stream().map<Document> { it.toDocument() }.collect(Collectors.toList())


        val document = Document()
        document["uuid"] = profile.uuid.toString()
        document["userName"] = profile.getClearedDisplayName()
        document["grants"] = grants
        document["punishments"] = punishments
        document["dateJoined"] = profile.dateJoined.time
        document["ipAddresses"] = profile.ipAddresses
        document["playerperms"] = profile.playerPerms

        if (profile.tag != null) {
            document["tag"] = profile.tag!!.name
        } else {
            document["tag"] = null
        }

        collection.replaceOne(Document("uuid", profile.uuid.toString()), document, ReplaceOptions().upsert(true))
    }

    fun findActivePunishment(uuid: UUID, ipAddress: String): Pair<UUID, ProfilePunishment>? {
        val orExpressions = listOf(
                Document("uuid", uuid.toString()),
                Document("ipAddresses", ipAddress)
        )

        for (profileDocument in collection.find(Document("\$or", orExpressions))) {
            val punishments = profileDocument.getList("punishments", Document::class.java)
                    .stream()
                    .map { ProfilePunishmentSerializer.deserialize(it) }
                    .filter { Objects.nonNull(it) }
                    .collect(Collectors.toList())

            for (punishment in punishments) {
                if (punishment.type == ProfilePunishmentType.BLACKLIST || punishment.type == ProfilePunishmentType.BAN) {
                    if (punishment.isActive()) {
                        return Pair(UUID.fromString(profileDocument.getString("uuid")), punishment)
                    }
                }
            }
        }

        return null
    }

    fun findAlts(target: T): List<UUID> {
        if (target.ipAddresses.isEmpty()) {
            return emptyList()
        }

        val query = Document("ipAddresses", target.ipAddresses)
        val results = arrayListOf<UUID>()

        for (profileDocument in collection.find(query)) {
            val uuid = UUID.fromString(profileDocument.getString("uuid"))

            if (uuid != null && !results.contains(uuid) && target.uuid != uuid) {
                results.add(uuid)
            }
        }

        return results
    }

    fun fetchProfileByUsername(username: String): T? {
        val uuid = StarkCore.instance.uuidCache.uuid(username)

        if (uuid != null && this.profiles.containsKey(uuid)) {
            return this.profiles[uuid]!!
        }

        val mojangUser = fetchMojangUser(username)

        return if (mojangUser != null) {
            loadProfile(mojangUser.uuid)
        } else null
    }

    fun fetchMojangUser(username: String): MojangUser? {
        return fetchMojangUserFromRedis(username) ?: fetchMojangUserFromMojang(username)
    }

    private fun fetchMojangUserFromRedis(username: String): MojangUser? {
        val uuid = StarkCore.instance.uuidCache.uuid(username)

        return if (uuid != null) {
            MojangUser(uuid, username)
        } else null
    }

    private fun fetchMojangUserFromMojang(username: String): MojangUser? {
        StarkCore.instance.logger.info("Fetching username from Mojang API: $username")

        try {
            val request = Request.Builder().url("https://api.mojang.com/users/profiles/minecraft/$username").build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val json = parser.parse(response.body().string()).asJsonObject
                val uuid = UUID.fromString(json.get("id").asString.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5"))
                val name = json.get("name").asString

                // update uuid cache
                StarkCore.instance.uuidCache.update(uuid, name)

                return MojangUser(uuid, name)
            }
        } catch (e: Exception) {
            StarkCore.instance.logger.info("Failed to retrieve user info from Mojang API: $username")
        }

        return null
    }

    companion object {
        private val client = OkHttpClient()
        private val parser = JsonParser()
    }

}