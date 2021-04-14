/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile.grant

import cc.fyre.stark.core.StarkCore
import org.bson.Document
import java.util.*

object ProfileGrantSerializer {

    @JvmStatic
    fun deserialize(document: Document): ProfileGrant? {
        val rank = StarkCore.instance.rankHandler.getById(document.getString("rank")) ?: return null

        val grant = ProfileGrant()

        grant.uuid = UUID.fromString(document.getString("id"))
        grant.rank = rank
        grant.reason = document.getString("reason")
        grant.issuedAt = document.getLong("issuedAt")!!
        grant.server = document.getString("server")
        val addedBy = document.getString("issuedBy")
        if (addedBy != null) {
            grant.issuedBy = UUID.fromString(addedBy)
        }

        grant.expiresAt = document.getLong("expiresAt")
        grant.removalReason = document.getString("removalReason")
        grant.removedAt = document.getLong("removedAt")
        grant.removedServer = document.getString("removedServer")

        val removedBy = document.getString("removedBy")
        if (removedBy != null) {
            grant.removedBy = UUID.fromString(removedBy)
        }

        return grant
    }

}