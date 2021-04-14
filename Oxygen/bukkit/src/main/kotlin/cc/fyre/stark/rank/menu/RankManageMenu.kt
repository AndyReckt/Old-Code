/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.menu

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/30/2020.
 */
class RankManageMenu : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GOLD}Rank Manage"
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>? {
        val toReturn = HashMap<Int, Button>()

        toReturn[4] = object : Button() {
            override fun getName(player: Player): String? {
                return "${ChatColor.GREEN}Create Rank"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to create a rank.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.NETHER_STAR
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {

/*                player.closeInventory()

                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(RankPermissionAddPrompt(rank))
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))*/
            }
        }

        return toReturn
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {

        val toReturn = HashMap<Int, Button>()

        for (rank in StarkCore.instance.rankHandler.getRanks().sortedBy { rank -> rank.displayOrder }) {

            toReturn[toReturn.size] = object : Button() {
                override fun getName(player: Player): String? {
                    return rank.getColoredName()
                }

                override fun getDescription(player: Player): List<String> {
                    val toReturn = ArrayList<String>()
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    toReturn.add("${ChatColor.GOLD}${ChatColor.BOLD}Data:")
                    toReturn.add("${ChatColor.GOLD}   Weight: ${ChatColor.WHITE}${rank.displayOrder}")
                    toReturn.add("${ChatColor.GOLD}   Prefix: ${rank.prefix}")
                    toReturn.add("${ChatColor.GOLD}   Scope: ${rank.scope}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.GOLD}${ChatColor.BOLD}Example Prefix:")
                    toReturn.add("${rank.prefix}${rank.gameColor}${player.name}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.GOLD}${ChatColor.BOLD}Example Player List Prefix:")
                    toReturn.add("${rank.gameColor}${player.name}")

                    if (rank.inherits.isNotEmpty() || rank.permissions.isNotEmpty()) {
                        toReturn.add("")
                    }
                    if (rank.permissions.isNotEmpty()) {
                        toReturn.add("${ChatColor.GRAY}Left click to view Permissions. (${rank.permissions.size})")
                    }
                    if (rank.inherits.isNotEmpty()) {
                        toReturn.add("${ChatColor.GRAY}Right click to view Inherits. (${rank.inherits.size})")
                    }
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}Shift left click to edit rank attributes.")
                    toReturn.add("${ChatColor.RED}Shift right click to delete this rank.")
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    return toReturn
                }

                override fun getMaterial(player: Player): Material {
                    return Material.INK_SACK
                }

                override fun getDamageValue(player: Player): Byte {
                    return (colorMap[ChatColor.getByChar(rank.gameColor.replace("&", ""))] ?: 15).toByte()
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    when {
                        clickType.isShiftClick && clickType.isLeftClick -> RankAttributesMenu(rank).openMenu(player)
                        clickType.isShiftClick && clickType.isRightClick -> StarkCore.instance.rankHandler.collection.deleteOne(rank.toDocument())
                        clickType.isLeftClick -> RankPermissionsMenu(rank).openMenu(player)
                        clickType.isRightClick -> RankInheritsMenu(rank).openMenu(player)
                    }
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
