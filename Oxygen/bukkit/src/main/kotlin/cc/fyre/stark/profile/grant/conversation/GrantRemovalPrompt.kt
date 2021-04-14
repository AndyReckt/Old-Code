/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.conversation

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.Message
import cc.fyre.stark.core.profile.grant.ProfileGrant
import cc.fyre.stark.engine.menu.menus.ConfirmMenu
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.util.Callback
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class GrantRemovalPrompt(val profile: BukkitProfile, val grant: ProfileGrant) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid reason."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {

        val actor = conversationContext.forWhom as? Player ?: return Prompt.END_OF_CONVERSATION

        ConfirmMenu("${ChatColor.GOLD}Grant Removal Confirm", object : Callback<Boolean> {
            override fun callback(value: Boolean) {
                when (value) {
                    true -> {
                        grant.removedBy = (conversationContext.forWhom as Player).uniqueId
                        grant.removedAt = System.currentTimeMillis()
                        grant.removalReason = s
                        grant.removedServer = Bukkit.getServerId()

                        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                            Stark.instance.core.getProfileHandler().saveProfile(profile)
                            Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))
                            conversationContext.forWhom.sendRawMessage("${ChatColor.GOLD}Grant removed.")
                        }
                    }
                    false -> conversationContext.forWhom.sendRawMessage("${ChatColor.RED}Cancelled the grant removal process")
                }
                actor.closeInventory()
            }
        }).openMenu(actor)

        return Prompt.END_OF_CONVERSATION
    }

}
