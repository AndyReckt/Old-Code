/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile.punishment

import org.bson.Document
import java.util.*

object ProfilePunishmentSerializer {

    @JvmStatic
    fun deserialize(document: Document): ProfilePunishment {
        val punishment = ProfilePunishment()

        punishment.uuid = UUID.fromString(document.getString("id"))
        punishment.type = ProfilePunishmentType.valueOf(document.getString("type"))
        punishment.reason = document.getString("reason")
        punishment.issuedAt = document.getLong("issuedAt")!!

        val addedBy = document.getString("issuedBy")
        if (addedBy != null) {
            punishment.issuedBy = UUID.fromString(addedBy)
        }

        punishment.expiresAt = document.getLong("expiresAt")
        punishment.removalReason = document.getString("removalReason")
        punishment.removedServer = document.getString("removedServer")
        punishment.removedAt = document.getLong("removedAt")
        punishment.server = document.getString("server")

        val removedBy = document.getString("removedBy")
        if (removedBy != null) {
            punishment.removedBy = UUID.fromString(removedBy)
        }

        if (document.containsKey("duration") && document.getString("duration") != null) {
            punishment.duration = document.getString("duration")
        }

        return punishment
    }

}