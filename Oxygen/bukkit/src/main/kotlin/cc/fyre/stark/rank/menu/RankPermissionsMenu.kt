/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.buttons.BackButton
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.rank.conversation.RankPermissionAddPrompt
import cc.fyre.stark.util.CC
import cc.fyre.stark.util.Callback
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/31/2020.
 */
class RankPermissionsMenu(val rank: Rank) : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GOLD}Permissions ${ChatColor.GRAY}(Editor)"
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>? {
        val buttons = HashMap<Int, Button>()

        buttons[4] = BackButton(object : Callback<Player> {
            override fun callback(value: Player) {
                RankManageMenu().openMenu(player)
            }
        })

        buttons[1] = object : Button() {
            override fun getName(player: Player): String? {
                return "${ChatColor.GREEN}Add Permission"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to add a Permission.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.NETHER_STAR
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                player.closeInventory()

                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(RankPermissionAddPrompt(rank))
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))
            }
        }

        return buttons
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val toReturn = HashMap<Int, Button>()


        for (permissions in this.getPermissions().stream()) {
            toReturn[toReturn.size] = object : Button() {
                override fun getName(player: Player): String {
                    return permissions
                }

                override fun getDescription(player: Player): List<String>? {
                    val toReturn = ArrayList<String>()
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    toReturn.add("${ChatColor.RED}Click to remove this permission.")
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                    return toReturn
                }

                override fun getMaterial(player: Player): Material? {
                    return Material.PAPER
                }

                override fun getDamageValue(player: Player): Byte {
                    return 0
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    rank.permissions.remove(permissions)
                    player.sendMessage("${ChatColor.YELLOW}The Permission $permissions was removed from the rank ${rank.displayName}")
                    StarkCore.instance.rankHandler.saveRank(rank)
                }
            }
        }
        return toReturn
    }

    private fun getPermissions(): List<String> {
        val permissions = ArrayList<String>()

        for (permission in rank.permissions.stream()) {
            permissions.add(permission)
        }

        return permissions
    }
}
