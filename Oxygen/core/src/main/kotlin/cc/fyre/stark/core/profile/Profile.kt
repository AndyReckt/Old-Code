/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.profile.grant.ProfileGrant
import cc.fyre.stark.core.profile.grant.ProfileGrantSerializer
import cc.fyre.stark.core.profile.punishment.ProfilePunishment
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentSerializer
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.core.tags.Tag
import org.bson.Document
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

abstract class Profile(val uuid: UUID) {

    var userName: String = ""
    var grants: MutableList<ProfileGrant> = ArrayList()
    var punishments: MutableList<ProfilePunishment> = ArrayList()
    var dateJoined = Date()
    var ipAddresses: MutableSet<String> = HashSet()
    var playerPerms: MutableList<String> = ArrayList()
    var tag: Tag? = null // if they don't have a tag we can let it be null

    fun getCompoundedPermissions(): List<String> {

        val permissions = ArrayList<String>()
        permissions.addAll(StarkCore.instance.rankHandler.getDefaultRank().getCompoundedPermissions())

        permissions.addAll(this.playerPerms.toList())
        permissions.addAll(getRank().getCompoundedPermissions())

        ArrayList(this.grants)
                .sortedBy { grant -> grant.rank.displayOrder }
                .forEach { grant ->
                    if (grant.isActive()) {
                        permissions.addAll(grant.rank.getCompoundedPermissions())
                    }
                }

        return permissions
    }

    fun getRank(): Rank {
        var currentGrant: ProfileGrant? = null

        for (grant in this.grants) {
            if (grant.isActive()) {
                if (currentGrant == null) {
                    currentGrant = grant
                    continue
                }

                if (currentGrant.rank.displayOrder > grant.rank.displayOrder) {
                    currentGrant = grant
                }
            }
        }

        return currentGrant?.rank ?: StarkCore.instance.rankHandler.getDefaultRank()
    }

    fun getPlayerListName(): String {
        return getRank().playerListPrefix.replace('&', StarkCore.COLOR_CODE_CHAR) + StarkCore.instance.uuidCache.name(this.uuid)
    }

    fun getDisplayName(): String {
        return getRank().prefix.replace('&', StarkCore.COLOR_CODE_CHAR) + StarkCore.instance.uuidCache.name(this.uuid)
    }

    fun getClearedDisplayName(): String {
        return StarkCore.instance.uuidCache.name(this.uuid)
    }

    fun update(document: Document) {
        val freshGrants = ArrayList<ProfileGrant>()
        val freshPunishments = ArrayList<ProfilePunishment>()


        document.getList("grants", Document::class.java).forEach grantForEach@ {
            try {
                val grant = ProfileGrantSerializer.deserialize(it) ?: return@grantForEach

                freshGrants.add(grant)
            } catch (ignore: IllegalStateException) {
            }

        }

        for (punishmentDocument in document.getList("punishments", Document::class.java)) {
            try {
                freshPunishments.add(ProfilePunishmentSerializer.deserialize(punishmentDocument))
            } catch (ignore: IllegalStateException) {
            }
        }
        this.userName = document.getString("userName")
        this.grants = freshGrants
        this.punishments = freshPunishments
        this.dateJoined = Date(document.getLong("dateJoined")!!)
        this.ipAddresses = document.getList("ipAddresses", String::class.java).toHashSet()
        this.playerPerms = document.getList("playerperms", String::class.java).distinct().toMutableList()

        if (document.containsKey("tag")) {
            val tagName = document.getString("tag")
            this.tag = StarkCore.instance.tagHandler.getByName(tagName)
        }
    }

    fun getActivePunishment(type: ProfilePunishmentType): ProfilePunishment? {
        for (punishment in this.punishments) {
            if (punishment.type == type && punishment.isActive()) {
                return punishment
            }
        }
        return null
    }

    fun getActivePunishments(): ProfilePunishment? {
        for (punishment in this.punishments) {
            if (punishment.isActive()) {
                return punishment
            }
        }
        return null
    }

    fun hasPermission(permission: String): Boolean {
        return getCompoundedPermissions().contains(permission)
    }

    fun isStaff(): Boolean {
        for (grant in grants) {
            if (grant.isActive() && grant.rank.staff) {
                return true
            }
        }
        return false
    }

    abstract fun apply()

}
