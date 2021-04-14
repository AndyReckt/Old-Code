/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.filter

import cc.fyre.stark.Stark
import cc.fyre.stark.util.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*

class FilterListener : Listener {

    var PREFIX = ChatColor.translateAlternateColorCodes('&', "&9&l[&4Filter&9&l] ")
    val lastMessageMap = hashMapOf<UUID, String>()

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        if (Stark.instance.filter.isFiltered(event.message)) {
            if (event.player.hasPermission("stark.staff")) {
                event.player.sendMessage(CC.RED + "That message would have been filtered!")
            } else {
                for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
                    if (onlinePlayer.hasPermission("stark.staff")) {
                        onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + event.player.displayName + "&7: &f" + event.message))
                    }
                }
                event.recipients.clear()
                event.recipients.add(event.player)
            }
        }

        if (event.isCancelled) return
        if (!event.player.hasPermission("stark.staff") && !event.player.hasPermission("stark.allowdoublepost") && lastMessageMap.containsKey(event.player.uniqueId) && lastMessageMap[event.player.uniqueId].equals(event.message, true)) {
            event.player.sendMessage(CC.RED + "Please refrain from double posting in chat!")
            event.player.sendMessage(CC.GRAY + "If you would like to bypass this filter purchase a rank at store.vyrix.us.")
            event.isCancelled = true
        } else {
            lastMessageMap[event.player.uniqueId] = event.message
        }

    }

}