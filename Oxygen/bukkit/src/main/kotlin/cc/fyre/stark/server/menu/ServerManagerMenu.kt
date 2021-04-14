/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.command.defaults.commands.other.UptimeCommand
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.util.BungeeUtil
import cc.fyre.stark.util.CC
import cc.fyre.stark.util.TPSUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.text.DecimalFormat
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/9/2020.
 */
class ServerManagerMenu : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
        this.placeholder = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.DARK_RED}Server Manager"
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val toReturn = HashMap<Int, Button>()


        for (server in Stark.instance.core.servers.streamServers()) {
            toReturn[toReturn.size] = object : Button() {
                override fun getName(player: Player): String {
                    return "${ChatColor.DARK_RED}${server.serverName}"
                }

                override fun getDescription(player: Player): List<String>? {
                    val toReturn = ArrayList<String>()
                    toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                    toReturn.add("${ChatColor.RED}Players: ${ChatColor.WHITE}${server.playerCount}")
                    toReturn.add("${ChatColor.RED}Staff: ${ChatColor.WHITE}${server.staffCount}")
                    toReturn.add("${ChatColor.RED}TPS: ${TPSUtils.prettyTPS(server.currentTps, DecimalFormat("0.00"))}")
                    if (server.isOnline() && !server.whitelisted) {
                        toReturn.add("${ChatColor.RED}Status: ${ChatColor.GREEN}Online")
                    } else if (server.whitelisted && server.isOnline()) {
                        toReturn.add("${ChatColor.RED}Status: ${ChatColor.WHITE}Whitelisted")
                    } else if (!server.isOnline()) {
                        toReturn.add("${ChatColor.RED}Status: ${ChatColor.RED}Offline")
                    }
                    toReturn.add("${ChatColor.RED}Uptime: ${UptimeCommand.uptimeColor((server.currentUptime / 1000).toInt())}${TimeUtils.formatLongIntoDetailedString(server.currentUptime / 1000L)}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.RED}Group: ${ChatColor.WHITE}${server.serverGroup}")
                    toReturn.add("${ChatColor.RED}Port: ${ChatColor.WHITE}${server.serverPort}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}Left click to connect to this server.")
                    toReturn.add("${ChatColor.GREEN}Right click to manage this server.")
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
                    when {
                        clickType.isLeftClick -> BungeeUtil.sendToServer(player, server.serverName)
                        clickType.isRightClick -> ServerEditorMenu(server).openMenu(player)
                    }
                }
            }
        }
        return toReturn
    }
}