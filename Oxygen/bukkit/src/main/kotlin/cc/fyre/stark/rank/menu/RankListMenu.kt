/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.menu

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/1/2020.
 */
class RankListMenu(val rank: Rank) : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.LIGHT_PURPLE}Select a rank..."
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {

        val toReturn = HashMap<Int, Button>()

        for (listedRank in StarkCore.instance.rankHandler.getRanks().sortedBy { rank -> rank.displayOrder }) {

            toReturn[toReturn.size] = object : Button() {
                override fun getName(player: Player): String? {
                    return listedRank.getColoredName()
                }

                override fun getDescription(player: Player): List<String> {
                    val toReturn = ArrayList<String>()
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    toReturn.add("${ChatColor.YELLOW}Click to select this rank.")
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    return toReturn
                }

                override fun getMaterial(player: Player): Material {
                    return Material.INK_SACK
                }

                override fun getDamageValue(player: Player): Byte {
                    return (colorMap[ChatColor.getByChar(listedRank.gameColor.replace("&", ""))] ?: 15).toByte()
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    rank.inherits.add(listedRank.displayName)
                    StarkCore.instance.rankHandler.saveRank(rank)
                    player.sendMessage("${ChatColor.YELLOW}Rank ${rank.displayName} now inherits ${listedRank.displayName}.")
                    player.closeInventory()
                }
            }
        }
        return toReturn
    }

    private val colorMap = mapOf(
            ChatColor.WHITE to 15,
            ChatColor.GOLD to 14,
            ChatColor.AQUA to 12,
            ChatColor.YELLOW to 11,
            ChatColor.GREEN to 10,
            ChatColor.LIGHT_PURPLE to 9,
            ChatColor.GRAY to 8,
            ChatColor.DARK_GRAY to 7,
            ChatColor.DARK_AQUA to 6,
            ChatColor.DARK_PURPLE to 5,
            ChatColor.BLUE to 4,
            ChatColor.DARK_GREEN to 2,
            ChatColor.RED to 1,
            ChatColor.DARK_RED to 1,
            ChatColor.BLACK to 0
    )
}
