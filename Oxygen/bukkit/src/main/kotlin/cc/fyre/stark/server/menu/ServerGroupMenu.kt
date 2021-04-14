/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.server.Server
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.server.conversation.ServerGroupPrompt
import cc.fyre.stark.util.CC
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/6/2020.
 */
class ServerGroupMenu(val server: Server) : PaginatedMenu() {

    init {
        this.updateAfterClick = true
        this.autoUpdate = true
        this.placeholder = true
    }

    override fun size(buttons: Map<Int, Button>): Int {
        return 27
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>? {
        val toReturn = HashMap<Int, Button>()

        toReturn[4] = object : Button() {
            override fun getName(player: Player): String? {
                return "${ChatColor.GREEN}Create Group"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to create a server group.")
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")

                return toReturn
            }

            override fun getMaterial(player: Player): Material? {
                return Material.NETHER_STAR
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                player.closeInventory()
                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(ServerGroupPrompt())
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))
            }
        }

        return toReturn
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.DARK_RED}Server Group"
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val toReturn = HashMap<Int, Button>()


        for (group in Stark.instance.core.servers.groups.values) {
            toReturn[toReturn.size] = object : Button() {
                override fun getName(player: Player): String {
                    return "${ChatColor.DARK_RED}${group.groupName}"
                }

                override fun getDescription(player: Player): List<String>? {
                    val toReturn = ArrayList<String>()
                    toReturn.add("&7Left click to &aselect&7.")
                    toReturn.add("&7Right click to &cdelete&7.")
                    return toReturn
                }

                override fun getMaterial(player: Player): Material? {
                    return Material.PAPER
                }

                override fun getDamageValue(player: Player): Byte {
                    return 0
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    if (clickType.isLeftClick) {
                        player.closeInventory()
                        player.sendMessage("${ChatColor.DARK_RED}${server.serverName} ${ChatColor.GRAY}server group has been updated to ${ChatColor.DARK_RED}${group.groupName}${ChatColor.GRAY}.")
                        server.serverGroup = group.groupName
                        Stark.instance.core.servers.saveServer(server)
                    } else if (clickType.isRightClick) {
                        Stark.instance.core.servers.deleteServerGroup(group.groupName)
                    }
                }
            }
        }
        return toReturn
    }
}