/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile.punishment

import cc.fyre.stark.core.util.TimeUtils
import org.bson.Document
import java.util.*
import java.util.concurrent.TimeUnit

class ProfilePunishment {

    //todo implt punishment ids aka #343 ext
    lateinit var uuid: UUID
    lateinit var type: ProfilePunishmentType
    lateinit var reason: String
    var issuedBy: UUID? = null
    var issuedAt: Long = -1
    var expiresAt: Long? = null
    var removalReason: String? = null
    var removedBy: UUID? = null
    var removedAt: Long? = null
    var removedServer: String? = "Unknown"
    var duration: String? = null
    var server: String? = "Unknown"

    fun isActive(): Boolean {
        return removedAt == null && (expiresAt == null || System.currentTimeMillis() < expiresAt!!)
    }

    fun timeLeft(): String {
        return if (expiresAt == null) "forever" else TimeUtils.formatIntoDetailedString(TimeUnit.MILLISECONDS.toSeconds(expiresAt!! - System.currentTimeMillis()).toInt() + 1)
    }

    fun toDocument(): Document {
        val document = Document()
        document["id"] = this.uuid.toString()
        document["type"] = this.type.name
        document["reason"] = this.reason
        document["issuedAt"] = this.issuedAt
        document["issuedBy"] = if (this.issuedBy != null) this.issuedBy!!.toString() else null
        document["expiresAt"] = this.expiresAt
        document["removalReason"] = this.removalReason
        document["removedAt"] = this.removedAt
        document["removedBy"] = if (this.removedBy != null) this.removedBy!!.toString() else null
        document["removedServer"] = this.removedServer
        document["duration"] = this.duration
        document["server"] = this.server
        return document
    }
}