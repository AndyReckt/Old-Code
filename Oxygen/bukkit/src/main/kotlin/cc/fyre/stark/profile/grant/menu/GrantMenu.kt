/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.grant.conversation.GrantCreationReasonPrompt
import it.unimi.dsi.fastutil.Hash
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.HashMap

class GrantMenu(val target: BukkitProfile) : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player): String = "Grant ${target.getPlayerListName()}"

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val senderProfile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!
        val buttons = HashMap<Int, Button>()

        Stark.instance.core.rankHandler.getRanks().sortedBy { it.displayOrder }.forEach { rank ->

          //  Bukkit.broadcastMessage(rank.displayName)

            var name = rank.gameColor + rank.displayName

            if (rank.prefix.isNotBlank()) {
                name = name + " " + rank.prefix
            }

            buttons[buttons.size] = object : Button() {
                override fun getName(player: Player): String? {
                    return name
                }

                override fun getDescription(player: Player): List<String> {
                    return listOf("&7Click to grant &r${target.getPlayerListName()}&7 the ${rank.gameColor + rank.displayName}&7 rank.")
                }

                override fun getMaterial(player: Player): Material {
                    return Material.INK_SACK
                }

                override fun getDamageValue(player: Player): Byte {
                    return (colorMap[ChatColor.getByChar(rank.gameColor.replace("&", ""))] ?: 15).toByte()
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    if (senderProfile.getRank().id == rank.id) {
                        player.sendMessage("${ChatColor.RED}The player already has this grant.")
                    }
                    player.closeInventory()

                    val factory = ConversationFactory(Stark.instance)
                            .withFirstPrompt(GrantCreationReasonPrompt(target, rank))
                            .withLocalEcho(false)
                            .thatExcludesNonPlayersWithMessage("Go away evil console!")

                    player.beginConversation(factory.buildConversation(player))
                }
            }

        }
        return buttons
    }

        override fun getMaxItemsPerPage(player: Player): Int = 27

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
