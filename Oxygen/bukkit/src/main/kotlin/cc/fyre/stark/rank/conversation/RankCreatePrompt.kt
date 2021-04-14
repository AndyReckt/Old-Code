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
 * Created by DaddyDombo daddydombo@gmail.com on 9/4/2020.
 */
class RankCreatePrompt(val rank: String) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a rank name."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        conversationContext.setSessionData("rank", rank)

        val createdRank = Rank(rank.toLowerCase(), rank, 0, mutableListOf(), "&7", "&7", "&7", default = false, hidden = false, staff = false)
        StarkCore.instance.rankHandler.addRank(createdRank)
        conversationContext.forWhom.sendRawMessage("${ChatColor.YELLOW}The rank ${ChatColor.LIGHT_PURPLE}$rank ${ChatColor.YELLOW}has been created!")
        return Prompt.END_OF_CONVERSATION
    }
}