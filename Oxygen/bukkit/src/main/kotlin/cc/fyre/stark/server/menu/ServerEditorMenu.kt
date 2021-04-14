/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.server.Server
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
 * Created by DaddyDombo daddydombo@gmail.com on 4/9/2020.
 */
class ServerEditorMenu(val server: Server) : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.DARK_RED}Server Editor"
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>? {
        val buttons = HashMap<Int, Button>()

        buttons[4] = BackButton(object : Callback<Player> {
            override fun callback(value: Player) {
                ServerManagerMenu().openMenu(player)
            }
        })
        return buttons
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val toReturn = HashMap<Int, Button>()

        toReturn[2] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}Whitelist"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.RED}Whitelist: ${ChatColor.WHITE}${server.whitelisted}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to change the whitelist status.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.GOLD_BLOCK
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                if (clickType.isLeftClick) {
                    server.whitelisted = !server.whitelisted
                    Stark.instance.core.servers.saveServer(server)

                    StarkCore.instance.globalMessageChannel.sendMessage(Message("UPDATE_WHITELIST", server.toMap()))
                }
            }
        }

        toReturn[4] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}Max Slots"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.RED}Slots: ${ChatColor.WHITE}${server.maxSlots}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit server slots.")
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
                    clickType.isLeftClick -> server.maxSlots += if (clickType.isShiftClick) 10 else 1
                    clickType.isRightClick -> server.maxSlots -= if (clickType.isShiftClick) 10 else 1
                    clickType == ClickType.MIDDLE -> server.maxSlots = 500
                }
                Stark.instance.core.servers.saveServer(server)
                StarkCore.instance.globalMessageChannel.sendMessage(Message("UPDATE_SLOTS", server.toMap()))
            }
        }

        toReturn[6] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}Server Group"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.RED}Group: ${ChatColor.WHITE}${server.serverGroup}")
                toReturn.add("")
                toReturn.add("${ChatColor.GREEN}Click to edit server group.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.EMERALD_BLOCK
            }

            override fun getDamageValue(player: Player): Byte {
                return 0
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                ServerGroupMenu(server).openMenu(player)
            }
        }
        return toReturn
    }
}