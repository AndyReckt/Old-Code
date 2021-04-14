/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.rank

import org.bson.Document
import java.util.*

object RankSerializer {

    @JvmStatic
    fun deserialize(document: Document): Rank {
        val rank = Rank(document.getString("id"))

        rank.permissions = ArrayList()
        rank.displayName = document.getString("displayName")
        rank.displayOrder = document.getInteger("displayOrder")!!
        rank.prefix = document.getString("prefix")
        rank.playerListPrefix = document.getString("playerListPrefix")
        rank.gameColor = document.getString("gameColor")
        rank.default = document.getBoolean("default")!!
        rank.inherits = document.getList("inherits", String::class.java)
        rank.staff = document.getBoolean("staff")
        rank.scope = document.getString("scope")
        rank.hidden = document.getBoolean("hidden")

        if (document.containsKey("permissions")) {
            rank.permissions.addAll(document.getList("permissions", String::class.java))
        }

        return rank
    }

    @JvmStatic
    fun serialize(rank: Rank): Document {
        val document = Document("id", rank.id)

        document["hidden"] = rank.hidden
        document["permissions"] = rank.permissions
        document["displayName"] = rank.displayName
        document["displayOrder"] = rank.displayOrder
        document["prefix"] = rank.prefix.replace("§", "&")
        document["playerListPrefix"] = rank.playerListPrefix.replace("§", "&")
        document["gameColor"] = rank.gameColor.replace("§", "&")
        document["default"] = rank.default
        document["inherits"] = rank.inherits
        document["staff"] = rank.staff
        document["scope"] = rank.scope
        document["hidden"] = rank.hidden

        return document
    }
}