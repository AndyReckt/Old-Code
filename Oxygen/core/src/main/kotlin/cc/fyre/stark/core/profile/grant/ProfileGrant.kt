/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile.grant

import cc.fyre.stark.core.rank.Rank
import org.bson.Document
import java.util.*

class ProfileGrant {

    lateinit var uuid: UUID
    lateinit var rank: Rank
    lateinit var reason: String
    var issuedBy: UUID? = null
    var issuedAt: Long = -1
    var expiresAt: Long? = null
    var server: String = "Unknown"
    var removalReason: String? = null
    var removedBy: UUID? = null
    var removedAt: Long? = null
    var removedServer: String = "Unknown"

    fun isActive(): Boolean {
        return removedAt == null && (expiresAt == null || System.currentTimeMillis() < expiresAt!!)
    }

    fun toDocument(): Document {
        val document = Document()
        document["id"] = this.uuid.toString()
        document["rank"] = this.rank.id
        document["reason"] = this.reason
        document["issuedBy"] = if (this.issuedBy != null) this.issuedBy.toString() else null
        document["issuedAt"] = this.issuedAt
        document["expiresAt"] = this.expiresAt
        document["server"] = this.server
        document["removalReason"] = this.removalReason
        document["removedBy"] = if (this.removedBy != null) this.removedBy!!.toString() else null
        document["removedAt"] = this.removedAt
        document["removedServer"] = this.removedServer
        return document
    }

}