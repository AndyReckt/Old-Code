/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.buttons.BackButton
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.rank.conversation.RankDisplayNamePrompt
import cc.fyre.stark.rank.conversation.RankPlayerListPrefixPrompt
import cc.fyre.stark.rank.conversation.RankPrefixPrompt
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
 * Created by DaddyDombo daddydombo@gmail.com on 4/2/2020.
 */
class RankAttributesMenu(val rank: Rank) : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GOLD}Rank Attributes"
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
        return buttons
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val toReturn = HashMap<Int, Button>()

        toReturn[0] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Color"
            }

            var Colorname: String = ""
            override fun getDescription(player: Player): List<String>? {
                val byte = (colorMap[ChatColor.getByChar(rank.gameColor.replace("&", ""))] ?: 15).toByte()
                for (color in reverseColorMap.values) {
                    if (color.ordinal == byte.toInt()) {
                        Colorname = color.name
                        break
                    }
                }

                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Color: $Colorname")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit the rank color.")
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
                RankColorMenu(rank).openMenu(player)
            }
        }

        toReturn[1] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Weight"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Weight: ${ChatColor.GRAY}${rank.displayOrder}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit rank display order.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.GHAST_TEAR
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                when {
                    clickType.isLeftClick -> rank.displayOrder += if (clickType.isShiftClick) 10 else 1
                    clickType.isRightClick -> rank.displayOrder -= if (clickType.isShiftClick) 10 else 1
                    clickType == ClickType.MIDDLE -> rank.displayOrder = 0
                }
            }
        }

        toReturn[2] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Display Name"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Display Name: ${ChatColor.GRAY}${rank.displayName}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit rank display name.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.PAINTING
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                player.closeInventory()
                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(RankDisplayNamePrompt(rank))
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))
            }
        }

        toReturn[3] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Player List Prefix"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Player List Prefix: ${ChatColor.GRAY}${rank.playerListPrefix}${player.name}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit the rank player list prefix.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.ITEM_FRAME
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                player.closeInventory()

                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(RankPlayerListPrefixPrompt(rank))
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))
            }
        }



        toReturn[4] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Prefix"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Prefix: ${ChatColor.GRAY}${rank.prefix}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit the rank prefix.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.SIGN
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                player.closeInventory()

                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(RankPrefixPrompt(rank))
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))
            }
        }

        toReturn[6] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Staff"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Staff: ${ChatColor.GRAY}${rank.staff}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit the rank staff status.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return if (rank.staff) {
                    Material.REDSTONE_TORCH_ON
                } else {
                    Material.LEVER
                }
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                if (clickType.isLeftClick) {
                    rank.staff = !rank.staff
                }
            }
        }

        toReturn[7] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Hidden"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Hidden: ${ChatColor.GRAY}${rank.hidden}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit the rank hidden status.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return if (rank.hidden) {
                    Material.IRON_DOOR
                } else {
                    Material.WOOD_DOOR
                }
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                if (clickType.isLeftClick) {
                    rank.hidden = !rank.hidden
                }
            }
        }

        toReturn[8] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Rank Default"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GOLD}Default: ${ChatColor.GRAY}${rank.default}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit the rank default status.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return if (rank.default) {
                    Material.STONE_PICKAXE
                } else {
                    Material.WOOD_PICKAXE
                }
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                if (clickType.isLeftClick) {
                    rank.default = !rank.default
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

    private val reverseColorMap = mapOf(
            15 to ChatColor.WHITE,
            14 to ChatColor.GOLD,
            12 to ChatColor.AQUA,
            11 to ChatColor.YELLOW,
            10 to ChatColor.GREEN,
            9 to ChatColor.LIGHT_PURPLE,
            8 to ChatColor.GRAY,
            7 to ChatColor.DARK_GRAY,
            6 to ChatColor.DARK_AQUA,
            5 to ChatColor.DARK_PURPLE,
            4 to ChatColor.BLUE,
            2 to ChatColor.DARK_GREEN,
            1 to ChatColor.RED,
            1 to ChatColor.DARK_RED,
            0 to ChatColor.BLACK
    )
}