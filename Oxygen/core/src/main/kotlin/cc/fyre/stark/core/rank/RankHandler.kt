/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.rank

import cc.fyre.stark.core.StarkCore
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import java.util.*
import java.util.concurrent.ForkJoinPool

class RankHandler {

    lateinit var collection: MongoCollection<Document>
    val ranks = HashMap<String, Rank>()
    private val defaultRank = Rank("default", "Default", Integer.MAX_VALUE, arrayListOf(), "&f", "&f", "&f", default = true, hidden = false, staff = false)

    fun load() {
        collection = StarkCore.instance.mongo.database.getCollection("ranks")
        collection.createIndex(Document("id", 1))

        loadRanks()
    }

    fun getRanks(): List<Rank> {
        return ArrayList(ranks.values)
    }

    fun getById(id: String): Rank? {
        return ranks[id]
    }

    fun getByName(name: String): Rank? = ranks[name.toLowerCase()]

    fun getDefaultRank(): Rank {
        for (rank in ranks.values) {
            if (rank.default) {
                return rank
            }
        }

        return defaultRank
    }

    fun loadRanks() {
        // Coming in from the database
        val freshRanks = ArrayList<Rank>()

        for (document in collection.find()) {
            try {
                freshRanks.add(RankSerializer.deserialize(document))
            } catch (e: Exception) {
                if (document.containsKey("id")) {
                    throw RuntimeException("Failed to load rank from document: " + document.getString("id"), e)
                } else {
                    throw RuntimeException("Failed to load rank from document: Couldn't identify rank ID", e)
                }
            }
        }

        // Iterate through "freshRanks"
        for (freshRank in freshRanks) {
            var internalRank: Rank? = getById(freshRank.id)

            // If the rank is not found locally, assign the fresh rank
            if (internalRank == null) {
                internalRank = freshRank
            }

            // If an internal rank was found but is not the fresh rank, assign details
            if (freshRank != internalRank) {
                internalRank.displayOrder = freshRank.displayOrder
                internalRank.displayName = freshRank.displayName
                internalRank.prefix = freshRank.prefix
                internalRank.playerListPrefix = freshRank.playerListPrefix
                internalRank.inherits = freshRank.inherits
                internalRank.permissions = freshRank.permissions
                internalRank.hidden = freshRank.hidden
                internalRank.staff = freshRank.staff
            }

            ranks[freshRank.id] = internalRank
        }
    }

    fun addRank(rank: Rank) {
        ranks[rank.id] = rank
        saveRank(rank)
    }

    fun saveRank(rank: Rank) {

        if (!ranks.containsValue(rank)) {
            ranks[rank.id] = rank
        }

        val doc = rank.toDocument()
        collection.replaceOne(Filters.eq("id", rank.id), doc, ReplaceOptions().upsert(true))
    }

    //todo impl
    fun saveAllRanks() {
        this.ranks.values.forEach { this.saveRank(it) }
    }
}
