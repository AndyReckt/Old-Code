/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.nametag.impl

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.nametag.NametagInfo
import cc.fyre.stark.engine.nametag.NametagProvider
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class StarkNametagProvider : NametagProvider("Stark Provider", 1) {

    override fun fetchNametag(toRefresh: Player, refreshFor: Player): NametagInfo {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(toRefresh.uniqueId)

        return if (profile == null) {
            createNametag("", "")
        } else {
            createNametag(ChatColor.translateAlternateColorCodes('&', profile.getRank().gameColor), "")
        }
    }
}