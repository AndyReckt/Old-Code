package cc.fyre.stark.profile.grant

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.profile.grant.event.GrantCreatedEvent
import cc.fyre.stark.profile.grant.event.GrantRemovedEvent
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ProfileGrantListeners : Listener {

    @EventHandler
    fun onGrantCreatedEvent(event: GrantCreatedEvent) {
        event.player.sendMessage("${ChatColor.GREEN}You've been granted the ${ChatColor.translateAlternateColorCodes('&', event.grant.rank.gameColor + event.grant.rank.displayName)} ${ChatColor.GREEN}rank for a period of ${ChatColor.YELLOW}${if (event.grant.expiresAt == null) "forever" else TimeUtils.formatIntoDetailedString(((event.grant.expiresAt!! - System.currentTimeMillis()) / 1000).toInt())}${ChatColor.GREEN}.")
        Stark.instance.core.getProfileHandler().getByUUID(event.player.uniqueId)?.apply()
        Stark.instance.nametagEngine.reloadPlayer(event.player)
    }

    @EventHandler
    fun onGrantRemovedEvent(event: GrantRemovedEvent) {
        event.player.sendMessage("${ChatColor.RED}Your grant of ${ChatColor.translateAlternateColorCodes('&', event.grant.rank.gameColor + event.grant.rank.displayName)}${ChatColor.RED} has been removed!")
        Stark.instance.core.getProfileHandler().getByUUID(event.player.uniqueId)?.apply()
        Stark.instance.nametagEngine.reloadPlayer(event.player)
    }
}