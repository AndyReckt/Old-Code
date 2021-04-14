/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.conversation

import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.profile.grant.menu.GrantDurationMenu
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class GrantCreationReasonPrompt(val target: BukkitProfile, val rank: Rank) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid reason."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        val actor = conversationContext.forWhom as? Player ?: return Prompt.END_OF_CONVERSATION
        conversationContext.setSessionData("rank", rank)
        conversationContext.setSessionData("reason", s)

        if (s == "cancel") {
            return Prompt.END_OF_CONVERSATION
        }

        GrantDurationMenu(target, rank, s).openMenu(actor)
        return Prompt.END_OF_CONVERSATION
    }

}
