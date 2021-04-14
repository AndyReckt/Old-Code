/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.core.profile.Profile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.permissions.PermissionAttachment
import java.util.*

class BukkitProfile(uuid: UUID) : Profile(uuid) {

    var attachment: PermissionAttachment? = null

    fun getPlayer(): Player? {
        return Bukkit.getPlayer(this.uuid)
    }

    override fun apply() {
        val player = this.getPlayer() ?: return

        if (attachment == null) {
            attachment = player.addAttachment(Stark.instance)
        } else {
            val permissions = listOf<String>(*attachment!!.permissions.keys.toTypedArray())

            for (permission in permissions) {
                attachment!!.unsetPermission(permission)
            }
        }

        for (permission in getCompoundedPermissions().toSet()) {
            if (permission.startsWith("-")) {
                attachment!!.setPermission(permission.substring(1), false)
            } else {
                attachment!!.setPermission(permission, true)
            }
        }

        player.recalculatePermissions()

        var playerListName = getPlayerListName()
        if (Stark.instance.config.getBoolean("coloredNamesTab", false)) {
            if (playerListName.length > 16) {
                playerListName = playerListName.substring(0, 15)
            }
            player.playerListName = playerListName
        }

        player.displayName = ChatColor.translateAlternateColorCodes('&', getRank().gameColor) + Stark.instance.core.uuidCache.name(uuid)
        player.setMetadata("RankPrefix", FixedMetadataValue(Stark.instance, ChatColor.translateAlternateColorCodes('&', getRank().prefix)))

        Stark.instance.nametagEngine.reloadPlayer(player)
    }
}