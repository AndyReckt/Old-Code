/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.conversation

import cc.fyre.stark.core.StarkCore
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/6/2020.
 */
class ServerGroupPrompt : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a server group name."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        conversationContext.setSessionData("group", s)
        StarkCore.instance.servers.loadOrCreateServerGroup(s)
        return Prompt.END_OF_CONVERSATION
    }
}