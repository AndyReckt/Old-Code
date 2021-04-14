/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.tags

import org.bson.Document

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
object TagSerializer {

    /**
     * Deserializes a given [Document] to a [Tag]
     *
     * @param document, the [Document] to be deserialized
     */
    @JvmStatic
    fun deserialize(document: Document): Tag {
        val name = document.getString("name")
        val displayName = document.getString("displayName")
        val tagType = TagType.valueOf(document.getString("tagType"))

        return Tag(name, displayName.replace("&", "§"), tagType)
    }


    /**
     * Serializes a given [Tag] to a [Document]
     * for storage in the database
     *
     * @param tag, the [Tag] to be serialized
     */
    @JvmStatic
    fun serialize(tag: Tag): Document {
        val document = Document()

        document["name"] = tag.name
        document["displayName"] = tag.display.replace("§", "&") // don't send the section symbol, unicode does weird things sometimes
        document["tagType"] = tag.tagType.name

        return document
    }

}