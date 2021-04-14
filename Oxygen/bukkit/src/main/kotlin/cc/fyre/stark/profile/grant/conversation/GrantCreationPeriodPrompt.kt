/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.conversation

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.grant.ProfileGrant
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.util.DateUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/9/2020.
 */
class GrantCreationPeriodPrompt(val target: BukkitProfile, val rank: Rank, val reason: String, val fromWhom: Player) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid time."
    }

    override fun acceptInput(conversationContext: ConversationContext, input: String): Prompt? {
        var perm = false
        var expiresAt = 0L

        if (input.toLowerCase() == "perm") {
            perm = true
        }

        if (!(perm)) {
            try {
                expiresAt = System.currentTimeMillis() - DateUtil.parseDateDiff(input, false)
            } catch (exception: Exception) {
                conversationContext.forWhom.sendRawMessage("${ChatColor.RED}Invalid duration.")
                return Prompt.END_OF_CONVERSATION
            }
        }

        val grant = ProfileGrant()
        grant.uuid = UUID.randomUUID()
        grant.rank = rank
        grant.reason = reason
        grant.issuedBy = fromWhom.uniqueId
        grant.issuedAt = System.currentTimeMillis()
        grant.server = Bukkit.getServerId()

        if (!perm) {
            grant.expiresAt = expiresAt + System.currentTimeMillis()
        }

        target.grants.add(grant)

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to target.uuid.toString(), "grant" to grant.uuid.toString())))

        val period = if (grant.expiresAt == null) {
            "forever"
        } else {
            TimeUtils.formatIntoDetailedString(((grant.expiresAt!! - System.currentTimeMillis()) / 1000).toInt())
        }

        conversationContext.forWhom.sendRawMessage("${ChatColor.GREEN}You've granted " + target.getPlayerListName() + " ${ChatColor.GREEN}the ${grant.rank.getColoredName()} ${ChatColor.GREEN}rank for ${ChatColor.YELLOW}$period${ChatColor.GREEN}.")

        return Prompt.END_OF_CONVERSATION
    }

}