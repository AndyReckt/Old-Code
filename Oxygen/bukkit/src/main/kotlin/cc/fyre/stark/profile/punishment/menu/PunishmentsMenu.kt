/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.punishment.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.profile.punishment.ProfilePunishmentType
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.buttons.BackButton
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.punishment.conversation.PunishmentRemovalPrompt
import cc.fyre.stark.util.Callback
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

class PunishmentsMenu(private val target: BukkitProfile, private val punishmentType: ProfilePunishmentType) : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.RED}${punishmentType.name.toLowerCase().capitalize()}s"
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>? {
        val buttons = HashMap<Int, Button>()
        buttons[4] = BackButton(object : Callback<Player> {
            override fun callback(value: Player) {
                SelectPunishmentTypeMenu(target).openMenu(player)
            }
        })

        return buttons
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val buttons = HashMap<Int, Button>()
        val punishments = target.punishments.sortedBy { -it.issuedAt }
        for (punishment in punishments) {
            if (punishment.type != punishmentType) {
                continue
            }

            val builder = ItemBuilder(Material.INK_SACK)

            if (punishment.isActive()) {
                builder.data(10)
            } else {
                builder.data(1)
            }

            var addedByProfile: BukkitProfile? = null
            var removedByProfile: BukkitProfile? = null

            if (punishment.issuedBy != null) {
                addedByProfile = Stark.instance.core.getProfileHandler().loadProfile(punishment.issuedBy!!)
            }

            if (punishment.removedBy != null) {
                removedByProfile = Stark.instance.core.getProfileHandler().loadProfile(punishment.removedBy!!)
            }

            builder.name("${ChatColor.translateAlternateColorCodes('&', punishment.type.color)}&l${punishment.type.name}")
            builder.addToLore(BAR)
            builder.addToLore("&eIssued By:&c " + if (punishment.issuedBy == null) "Console" else addedByProfile?.getPlayerListName())
            builder.addToLore("&eIssued On:&c ${TimeUtils.formatIntoCalendarString(Date(punishment.issuedAt))}")
            builder.addToLore("&eServer Issued on: &c${punishment.server}")
            builder.addToLore("&eReason:&c&o ${punishment.reason}")
            if (punishment.duration != null) {
                builder.addToLore("&eDuration: &c" + punishment.duration)
            }

            if (punishment.isActive()) {
                if (punishment.expiresAt != null) {
                    builder.addToLore("&eTime Remaining:&c " + TimeUtils.formatIntoDetailedString(((punishment.expiresAt!! - System.currentTimeMillis()) / 1_000L).toInt()))
                }

                if (player.hasPermission("stark.punishment.${punishment.type.name.toLowerCase()}.delete")) {
                    builder.addToLore(BAR)
                    builder.addToLore("&eClick to remove this punishment.")
                }
            } else if (punishment.removedAt != null) {
                builder.addToLore(BAR)
                builder.addToLore("&eRemoved By:&c " + (if (punishment.removedBy == null) "Console" else removedByProfile?.getPlayerListName()))
                builder.addToLore("&eRemoved On:&c ${TimeUtils.formatIntoCalendarString(Date(punishment.removedAt!!))}")
                builder.addToLore("&eServer Removed On:&c ${punishment.removedServer}")
                builder.addToLore("&eReason:&c&o ${punishment.removalReason}")
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
                    if (punishment.removedAt == null) {
                        player.closeInventory()

                        if (!player.hasPermission("stark.command.un" + punishment.type.name.toLowerCase())) {
                            player.closeInventory()
                            return
                        }

                        val factory = ConversationFactory(Stark.instance)
                                .withFirstPrompt(PunishmentRemovalPrompt(target, punishment))
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