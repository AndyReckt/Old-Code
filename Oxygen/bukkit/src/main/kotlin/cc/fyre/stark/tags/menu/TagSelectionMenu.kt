/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.tags.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
class TagSelectionMenu : PaginatedMenu() {

    init {
        updateAfterClick = true
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        val map = mutableMapOf<Int, Button>()
        for (tag in StarkCore.instance.tagHandler.getTags().values) {
            map[map.size] = TagSelectionButton(tag)
        }
        return map
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 18
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.YELLOW}Select a tag..."
    }

    override fun onClose(player: Player) {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId) ?: return
        Bukkit.getServer().scheduler.runTaskAsynchronously(Stark.instance) {
            Stark.instance.core.getProfileHandler().saveProfile(profile)
        }
    }
}