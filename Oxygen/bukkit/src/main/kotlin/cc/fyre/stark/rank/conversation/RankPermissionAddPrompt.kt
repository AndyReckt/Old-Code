/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.conversation

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.rank.Rank
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/31/2020.
 */
class RankPermissionAddPrompt(val rank: Rank) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid rank permission."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        conversationContext.setSessionData("rank", rank)
        conversationContext.setSessionData("permission", s)

        rank.permissions.add(s)
        StarkCore.instance.rankHandler.saveRank(rank)
        conversationContext.forWhom.sendRawMessage("${ChatColor.YELLOW}Updated to ${ChatColor.GOLD}$s.")

        return Prompt.END_OF_CONVERSATION
    }
}