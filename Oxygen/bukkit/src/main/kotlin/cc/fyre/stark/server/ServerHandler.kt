/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server

import cc.fyre.stark.Stark
import cc.fyre.stark.server.chat.ChatFilterEntry
import cc.fyre.stark.util.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class ServerHandler {

    var frozen: Boolean = false
    val disallowedCommands = HashSet<String>()
    val announcements = ArrayList<String>()

    val bannedRegexes = setOf(
            ChatFilterEntry("Restricted Phrase \"ip farm\"", "[i1l1|]+p+ ?f[a4]+rm+"),
            ChatFilterEntry("Racism \"Nigger\"", "n+[i1l|]+gg+[e3]+r+"),
            ChatFilterEntry("Racism \"Beaner\"", "b+e+a+n+e+r+"),
            ChatFilterEntry("Suicide Encouragement", "k+i+l+l+ *y*o*u+r+ *s+e+l+f+"),
            ChatFilterEntry("Suicide Encouragement", "\\bk+y+s+\\b"),
            ChatFilterEntry("Offensive \"Faggot\"", "f+a+g+[o0]+t+"),
            ChatFilterEntry("IP Address", "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"),
            ChatFilterEntry("Phishing Link \"optifine\"", "optifine\\.(?=\\w+)(?!net)"),
            ChatFilterEntry("Phishing Link \"gyazo\"", "gyazo\\.(?=\\w+)(?!com)"),
            ChatFilterEntry("Phishing Link \"prntscr\"", "prntscr\\.(?=\\w+)(?!com)")
    )

    fun load() {
        val list = Stark.instance.config.getStringList("disallowedCommands")

        if (list != null) {
            disallowedCommands.addAll(list)
        }

        disallowedCommands.add("/calc")
        disallowedCommands.add("/calculate")
        disallowedCommands.add("/eval")
        disallowedCommands.add("/evaluate")
        disallowedCommands.add("/solve")
        disallowedCommands.add("/worldedit:calc")
        disallowedCommands.add("/worldedit:eval")
        disallowedCommands.add("me")
        disallowedCommands.add("pl")
        disallowedCommands.add("bukkit:me")
        disallowedCommands.add("icanhasbukkit")
        disallowedCommands.add("bukkit:icanhasbukkit")
        disallowedCommands.add("minecraft:me")
        disallowedCommands.add("bukkit:plugins")
        disallowedCommands.add("bukkit:pl")
        disallowedCommands.add("plugins")
        disallowedCommands.add("version")
        disallowedCommands.add("ver")
        disallowedCommands.add("?")
        disallowedCommands.add("bukkit:help")
        disallowedCommands.add("minecraft:help")
        disallowedCommands.add("about")
        disallowedCommands.add("minecraft:about")
        disallowedCommands.add("bukkit:about")

        val announcementConfigList = Stark.instance.config.getStringList("announcements")
        if (announcementConfigList != null && !announcementConfigList.isEmpty()) {
            announcements.addAll(announcementConfigList)
        }

        Bukkit.getServer().scheduler.runTaskTimer(Stark.instance, {
            val randomAnnouncement = announcements[ThreadLocalRandom.current().nextInt(announcements.size)]
            if (randomAnnouncement != null) {
                for (onlinePlayer in Bukkit.getServer().onlinePlayers) {
                    onlinePlayer.sendMessage(" ")
                    onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', randomAnnouncement).replace("%DOUBLE_ARROW%", CC.UNICODE_ARROWS_RIGHT))
                    onlinePlayer.sendMessage(" ")
                }
            }
        }, 0L, 3000L)
    }

    fun freeze(player: Player) {
        player.setMetadata("frozen", FixedMetadataValue(Stark.instance, true as Any) as MetadataValue)
        player.sendMessage("${ChatColor.RED}You have been frozen by a staff member.")

        val uuid = player.uniqueId

        Bukkit.getScheduler().runTaskLater(Stark.instance, { this.unfreeze(uuid) }, 20L * TimeUnit.HOURS.toSeconds(2L))

        val location = player.location
        var tries = 0

        while (1.0 <= location.y && !location.block.type.isSolid && tries++ < 100) {
            location.subtract(0.0, 1.0, 0.0)
            if (location.y <= 0.0) {
                break
            }
        }

        if (100 <= tries) {
            Bukkit.getLogger().info("Hit the 100 try limit on the freeze command.")
        }

        location.y = location.blockY.toDouble()

        player.teleport(location.add(0.0, 1.0, 0.0))
    }

    fun unfreeze(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid)
        if (player != null) {
            player.removeMetadata("frozen", Stark.instance)
            player.sendMessage("${ChatColor.GREEN}You have been unfrozen by a staff member.")
        }
    }

}