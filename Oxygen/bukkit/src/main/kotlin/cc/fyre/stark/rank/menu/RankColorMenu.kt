/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.menu

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.buttons.BackButton
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.util.CC
import cc.fyre.stark.util.Callback
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/4/2020.
 */
class RankColorMenu(val rank: Rank) : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GOLD}Color ${ChatColor.GRAY}(Editor)"
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


        for (color in reverseColorMap.values) {
            toReturn[toReturn.size] = object : Button() {
                override fun getName(player: Player): String {
                    return color.name
                }

                override fun getDescription(player: Player): List<String>? {
                    val toReturn = ArrayList<String>()
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    toReturn.add("${ChatColor.RED}Click to set color.")
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                    return toReturn
                }

                override fun getMaterial(player: Player): Material {
                    return Material.INK_SACK
                }

                override fun getDamageValue(player: Player): Byte {
                    return (colorMap[ChatColor.getByChar(color.char)] ?: 15).toByte()
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {

                    //TODO: SINDRE FIX
                    rank.gameColor = color.name
                    player.sendMessage(color.toString())
                    //player.sendMessage("This is a test to make sure were saving properly to the Database.")
                    StarkCore.instance.rankHandler.saveRank(rank)
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