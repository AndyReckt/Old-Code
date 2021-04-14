/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.grant.ProfileGrant
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.buttons.BackButton
import cc.fyre.stark.engine.menu.pagination.PaginatedMenu
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.grant.conversation.GrantCreationPeriodPrompt
import cc.fyre.stark.util.CC
import cc.fyre.stark.util.Callback
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2020.
 */
class GrantDurationMenu(var profile: BukkitProfile, val rank: Rank, val reason: String) : PaginatedMenu() {

    var conversationContext: ConversationContext? = null

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GOLD}Select a duration..."
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>? {
        val buttons = HashMap<Int, Button>()

        buttons[4] = BackButton(object : Callback<Player> {
            override fun callback(value: Player) {
                player.closeInventory()
                player.sendMessage("${ChatColor.RED}Grant process has been canceled!")
            }
        })
        return buttons
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val toReturn = HashMap<Int, Button>()

        toReturn[0] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}12 Hours"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to 12 Hours.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.expiresAt = TimeUnit.HOURS.toMillis(12) + System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}12 Hours${ChatColor.GREEN}.")
            }
        }

        toReturn[1] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}1 Day"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to 1 Day.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.expiresAt = TimeUnit.DAYS.toMillis(1) + System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}1 Day${ChatColor.GREEN}.")
            }
        }

        toReturn[2] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}3 Days"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to 3 Days.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.expiresAt = TimeUnit.DAYS.toMillis(3) + System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}3 Days${ChatColor.GREEN}.")
            }
        }

        toReturn[3] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}1 Week"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to 1 Week.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.expiresAt = TimeUnit.DAYS.toMillis(7) + System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}1 Week${ChatColor.GREEN}.")
            }
        }

        toReturn[4] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}2 Weeks"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to 2 Weeks.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.expiresAt = TimeUnit.DAYS.toMillis(14) + System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}2 Weeks${ChatColor.GREEN}.")
            }
        }

        toReturn[5] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}1 Month"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to 1 Month.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.expiresAt = TimeUnit.DAYS.toMillis(30) + System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}1 Month${ChatColor.GREEN}.")
            }
        }

        toReturn[6] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}Permanent"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set the duration to Permanent.")
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
                player.closeInventory()

                val grant = ProfileGrant()
                grant.uuid = UUID.randomUUID()
                grant.rank = rank
                grant.reason = reason
                grant.issuedBy = player.uniqueId
                grant.issuedAt = System.currentTimeMillis()
                grant.server = Bukkit.getServerId()
                profile.grants.add(grant)

                Stark.instance.core.getProfileHandler().saveProfile(profile)
                Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))

                player.sendMessage("${ChatColor.GREEN}You've granted " + profile.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}Permanent${ChatColor.GREEN}.")
            }
        }

        toReturn[8] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.DARK_RED}Custom Duration"
            }

            override fun getDescription(player: Player): List<String>? {
                val toReturn = ArrayList<String>()
                toReturn.add("${ChatColor.GRAY}${CC.MENU_BAR}")
                toReturn.add("${ChatColor.GREEN}Click to set a custom duration.")
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
                player.closeInventory()

                val factory = ConversationFactory(Stark.instance)
                        .withFirstPrompt(GrantCreationPeriodPrompt(profile, rank, reason, player))
                        .withLocalEcho(false)
                        .thatExcludesNonPlayersWithMessage("Go away evil console!")

                player.beginConversation(factory.buildConversation(player))
            }
        }
        return toReturn
    }
}