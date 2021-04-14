/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.tags

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.util.AsyncUtil
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import java.util.*

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
class TagHandler {

    private lateinit var collection: MongoCollection<Document>

    private val tags = TreeMap<String, Tag>(String.CASE_INSENSITIVE_ORDER)

    fun load() {
        collection = StarkCore.instance.mongo.database.getCollection("tags")
        loadTags()
    }

    /**
     * Loads all of the [Tag]'s from the database
     */
    private fun loadTags() {
        for (document in collection.find()) {
            try {
                val tag = TagSerializer.deserialize(document)
                tags[tag.name] = tag
            } catch (ex: Exception) {
                if (document.containsKey("name")) {
                    throw RuntimeException("Couldn't load tag from document, id: ${document.getString("name")}")
                } else {
                    throw RuntimeException("Couldn't load tag from malformed document. Offending document: ${document.toJson()}")
                }
            }
        }
    }

    /**
     * Refreshes [Tag]'s that are applicable
     */
    fun refreshTags() {
        for (document in collection.find()) {
            try {
                val tag = TagSerializer.deserialize(document)
                if (update(tags[tag.name], tag)) { // don't update tags that haven't changed
                    tags[tag.name] = tag
                }
            } catch (ex: Exception) {
                if (document.containsKey("name")) {
                    throw RuntimeException("Couldn't load tag from document, id: ${document.getString("name")}")
                } else {
                    throw RuntimeException("Couldn't load tag from malformed document. Offending document: ${document.toJson()}")
                }
            }
        }
    }

    /**
     * Get's a [Tag], based on it's [Tag.name]
     *
     * @return the tag if it exists, else null
     */
    fun getByName(name: String?): Tag? {
        for (tag in tags) {
            if (tag.key.equals(name, ignoreCase = true)) {
                return tag.value
            }
        }
        return null
    }


    fun delete(tag: Tag) {
        AsyncUtil.ensureAsync()
        tags.remove(tag.name)
        // todo: ensure this mongo query is correct
        StarkCore.instance.getProfileHandler().collection.updateMany(Filters.eq("tag", tag.name), Document("\$set", Document("tag", null)))
        collection.deleteOne(Filters.eq("name", tag.name))
    }

    /**
     * Check to see if it is necessary to update a [Tag]
     * that is already present in the system
     *
     * @param old, the old [Tag] (can be nullable)
     * @param new, the new [Tag]
     */
    private fun update(old: Tag?, new: Tag): Boolean {
        if (old == null || old != new) {
            return true
        }
        return false
    }

    /**
     * Saves all of the tags to the database
     *
     * @see save
     */
    fun saveAll() {
        for (tag in tags.values) {
            save(tag)
        }
    }

    /**
     * Saves a [Tag] to the database
     *
     * @param tag, the [Tag] to save
     */
    fun save(tag: Tag) {
        val document = TagSerializer.serialize(tag)
        collection.replaceOne(Filters.eq("name", tag.name), document, ReplaceOptions().upsert(true))
    }

    fun getTags(): MutableMap<String, Tag> {
        return tags
    }
}