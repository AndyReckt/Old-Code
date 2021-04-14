/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.grant.conversation.GrantRemovalPrompt
import cc.fyre.stark.util.ItemBuilder
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashMap

class GrantsMenu(private val target: BukkitProfile) : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.RED}Grants - ${Stark.instance.core.uuidCache.name(target.uuid)}"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val senderProfile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!
        val buttons = HashMap<Int, Button>()
        val grants = target.grants.sortedBy { -it.issuedAt }
        for (grant in grants) {
            val builder = ItemBuilder(Material.INK_SACK)

            if (grant.isActive()) {
                builder.data(10)
            } else {
                builder.data(1)
            }

            var issuerProfile: BukkitProfile? = null
            var removerProfile: BukkitProfile? = null

            if (grant.issuedBy != null) {
                issuerProfile = Stark.instance.core.getProfileHandler().loadProfile(grant.issuedBy!!)
            }

            if (grant.removedBy != null) {
                removerProfile = Stark.instance.core.getProfileHandler().loadProfile(grant.removedBy!!)
            }

            builder.name(grant.rank.gameColor + StringUtils.capitalize(grant.rank.displayName.toLowerCase()))
            builder.addToLore(BAR)
            builder.addToLore("&eIssued By:&c " + if (grant.issuedBy == null) "Console" else issuerProfile?.getPlayerListName())
            builder.addToLore("&eIssued On:&c ${TimeUtils.formatIntoCalendarString(Date(grant.issuedAt))}")
            builder.addToLore("&eReason:&c&o ${grant.reason}")
            builder.addToLore("&eServer Issued On:&c ${grant.server}")

            if (grant.isActive()) {
                if (grant.expiresAt == null) {
                    builder.addToLore("&eDuration:&c Permanent")
                } else {
                    builder.addToLore("&eTime remaining:&c " + TimeUtils.formatIntoDetailedString(((grant.expiresAt!! - System.currentTimeMillis()) / 1_000L).toInt()))
                }

                if ((senderProfile.getRank().id == "owner" || senderProfile.getRank().displayOrder < grant.rank.displayOrder) && player.isOp) {
                    builder.addToLore(BAR)
                    builder.addToLore("&eClick to remove this grant.")
                }
            } else if (grant.removedAt != null) {
                builder.addToLore(BAR)
                builder.addToLore("&eRemoved By:&c " + (if (grant.removedBy == null) "Console" else removerProfile?.getPlayerListName()))
                builder.addToLore("&eRemoved On:&c ${TimeUtils.formatIntoCalendarString(Date(grant.removedAt!!))}")
                builder.addToLore("&eReason:&c&o ${grant.removalReason}")
                builder.addToLore("&eServer Removed On:&c ${grant.removedServer}")
            }

            builder.addToLore(BAR)

            buttons[buttons.size] = object : Button() {
                override fun getName(player: Player): String? {
                    return null
                }

                override fun getDescription(player: Player): List<String>? {
                    return null
                }

                override fun getMaterial(player: Player): Material? {
                    return null
                }

                override fun getButtonItem(player: Player): ItemStack {
                    return builder.build()
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    if (grant.removedAt == null && (senderProfile.getRank().id == "owner" || senderProfile.getRank().displayOrder < grant.rank.displayOrder) && player.isOp) {
                        player.closeInventory()

                        val factory = ConversationFactory(Stark.instance)
                            .withFirstPrompt(GrantRemovalPrompt(target, grant))
                            .withLocalEcho(false)
                            .thatExcludesNonPlayersWithMessage("Go away evil console!")

                        player.beginConversation(factory.buildConversation(player))
                    }
                }
            }
        }

        return buttons
    }

    companion object {
        private val BAR = "&7&m${StringUtils.repeat("-", 32)}"
    }

}