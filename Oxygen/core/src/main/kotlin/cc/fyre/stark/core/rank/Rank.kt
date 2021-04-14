/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.rank

import cc.fyre.stark.core.StarkCore
import org.bson.Document

class Rank(var id: String,
           var displayName: String,
           var displayOrder: Int,
           var permissions: MutableList<String>,
           var prefix: String,
           var playerListPrefix: String,
           var gameColor: String,
           var default: Boolean,
           var hidden: Boolean,
           var staff: Boolean,
           var scope: String = "Global",
           var inherits: MutableList<String> = mutableListOf()) {

    constructor(id: String) : this(id, id, 999, mutableListOf(), "&7", "&7", "&7", false, false, false)

    /**
     * Recursively gets all permissions of this rank and the ranks it inherits.
     */
    fun getCompoundedPermissions(): List<String> {
        val toReturn = ArrayList<String>()
        toReturn.addAll(permissions)

        for (inheritedRank in getInheritedRanks()) {
            toReturn.addAll(inheritedRank.getCompoundedPermissions())
        }

        return toReturn
    }

    fun getInheritedRanks(): List<Rank> {
        val toReturn = arrayListOf<Rank>()
        for (rank in StarkCore.instance.rankHandler.getRanks()) {
            if (inherits.contains(rank.displayName)) {
                toReturn.add(rank)
            }
        }
        return toReturn
    }

    fun getColoredName(): String {
        return gameColor.replace("&", "§") + displayName
    }

    fun toDocument(): Document {
        val document = Document()
        document["id"] = this.id
        document["displayName"] = this.displayName
        document["displayOrder"] = this.displayOrder
        document["prefix"] = this.prefix
        document["playerListPrefix"] = this.playerListPrefix
        document["gameColor"] = this.gameColor
        document["default"] = this.default
        document["inherits"] = this.inherits
        document["permissions"] = this.permissions
        document["hidden"] = this.hidden
        document["staff"] = this.staff
        document["scope"] = this.scope
        return document
    }

}