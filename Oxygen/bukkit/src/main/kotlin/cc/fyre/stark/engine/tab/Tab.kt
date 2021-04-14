/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.scoreboard.ScoreboardTeamPacketMod
import net.minecraft.server.v1_7_R4.ChatSerializer
import net.minecraft.util.com.mojang.authlib.GameProfile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.spigotmc.ProtocolInjector
import java.util.*

class Tab(private val player: Player) {

    private val previousNames: MutableMap<String, String> = HashMap()
    private val previousPings: MutableMap<String, Int> = HashMap()
    private var lastHeader: String = "{\"translate\":\"\"}"
    private var lastFooter: String = "{\"translate\":\"\"}"
    private val createdTeams: MutableSet<String> = HashSet()
    private var initialLayout: TabLayout? = null
    private val removeColorCodesBuilder = StringBuilder()

    private fun createAndAddMember(name: String, member: String) {
        val scoreboardTeamAdd = ScoreboardTeamPacketMod("$$name", "", "", arrayListOf(member), 0)
        scoreboardTeamAdd.sendToPlayer(this.player)
    }

    private fun init() {
        val initialLayout = TabLayout.createEmpty(this.player)
        if (!initialLayout.is18) {
            for (n in Bukkit.getOnlinePlayers()) {
                this.updateTabList(n.name, 0, (n as CraftPlayer).profile, 4)
            }
        }

        for (s in initialLayout.tabNames) {
            this.updateTabList(s, 0, 0)
            val teamName = s.replace("§".toRegex(), "")
            if (!this.createdTeams.contains(teamName)) {
                this.createAndAddMember(teamName, s)
                this.createdTeams.add(teamName)
            }
        }

        this.initialLayout = initialLayout
    }

    private fun updateScore(score: String, prefix: String, suffix: String) {
        val scoreboardTeamModify = ScoreboardTeamPacketMod(score, prefix, suffix, null, 2)
        scoreboardTeamModify.sendToPlayer(player)
    }

    private fun updateTabList(name: String, ping: Int, action: Int) {
        this.updateTabList(name, ping, TabUtils.getOrCreateProfile(name), action)
    }

    private fun updateTabList(name: String, ping: Int, profile: GameProfile, action: Int) {
        val playerInfoPacketMod = PlayerInfoPacketMod("$$name", ping, profile, action)
        playerInfoPacketMod.sendToPlayer(player)
    }

    private fun splitString(line: String): Array<String> {
        return if (line.length <= 16) {
            arrayOf(line, "")
        } else arrayOf(line.substring(0, 16), line.substring(16, line.length))
    }

    fun update() {
        if (Stark.instance.tabEngine.layoutProvider != null) {
            val tabLayout = Stark.instance.tabEngine.layoutProvider?.provide(this.player)
            if (tabLayout == null) {
                reset()
                return
            }

            init()

            for (y in 0 until TabLayout.HEIGHT) {
                for (x in 0 until TabLayout.WIDTH) {
                    val entry = tabLayout.getStringAt(x, y)
                    val ping = tabLayout.getPingAt(x, y)
                    val entryName = this.initialLayout!!.getStringAt(x, y)
                    this.removeColorCodesBuilder.setLength(0)
                    this.removeColorCodesBuilder.append('$')
                    this.removeColorCodesBuilder.append(entryName)
                    var j = 0
                    for (i in 0 until this.removeColorCodesBuilder.length) {
                        if ('§' != this.removeColorCodesBuilder[i]) {
                            this.removeColorCodesBuilder.setCharAt(j++, this.removeColorCodesBuilder[i])
                        }
                    }
                    this.removeColorCodesBuilder.delete(j, this.removeColorCodesBuilder.length)
                    val teamName = this.removeColorCodesBuilder.toString()
                    if (this.previousNames.containsKey(entryName)) {
                        if (this.previousNames[entryName] != entry) {
                            this.update(entryName, teamName, entry, ping)
                        } else if (this.previousPings.containsKey(entryName) && this.pingToBars(this.previousPings[entryName]!!) != this.pingToBars(ping)) {
                            this.updateTabList(entryName, ping, 2)
                            this.previousPings[entryName] = ping
                        }
                    } else {
                        this.update(entryName, teamName, entry, ping)
                    }
                }
            }

            var sendHeader = false
            var sendFooter = false
            val header = tabLayout.header
            val footer = tabLayout.footer

            if (header != this.lastHeader) {
                sendHeader = true
            }

            if (footer != this.lastFooter) {
                sendFooter = true
            }

            if (tabLayout.is18 && (sendHeader || sendFooter)) {
                val packet = ProtocolInjector.PacketTabHeader(ChatSerializer.a(header), ChatSerializer.a(footer))
                (this.player as CraftPlayer).handle.playerConnection.sendPacket(packet)
                this.lastHeader = header
                this.lastFooter = footer
            }
        }
    }

    private fun reset() {
        for (s in this.initialLayout!!.tabNames) {
            this.updateTabList(s, 0, 4)
        }

        var ePlayer = (this.player as CraftPlayer).handle

        this.updateTabList(this.player.getName(), ePlayer.ping, ePlayer.profile, 0)

        var count = 1

        for (player in Bukkit.getOnlinePlayers()) {
            if (this.player === player) {
                continue
            }

            if (count > this.initialLayout!!.tabNames.size - 1) {
                break
            }

            ePlayer = (player as CraftPlayer).handle

            this.updateTabList(player.getName(), ePlayer.ping, ePlayer.profile, 0)

            ++count
        }
    }

    private fun update(entryName: String, teamName: String, entry: String, ping: Int) {
        val entryStrings = this.splitString(entry)
        var prefix = entryStrings[0]
        var suffix = entryStrings[1]

        if (suffix.isNotEmpty()) {
            if (prefix[prefix.length - 1] == '§') {
                prefix = prefix.substring(0, prefix.length - 1)
                suffix = "§$suffix"
            }

            var suffixPrefix = ChatColor.RESET.toString()

            if (ChatColor.getLastColors(prefix).isNotEmpty()) {
                suffixPrefix = ChatColor.getLastColors(prefix)
            }

            suffix = if (suffix.length <= 14) {
                suffixPrefix + suffix
            } else {
                suffixPrefix + suffix.substring(0, 14)
            }
        }

        this.updateScore(teamName, prefix, suffix)
        this.updateTabList(entryName, ping, 2)

        this.previousNames[entryName] = entry
        this.previousPings[entryName] = ping
    }

    private fun pingToBars(ping: Int): Int {
        return when {
            ping < 0 -> 5
            ping < 150 -> 0
            ping < 300 -> 1
            ping < 600 -> 2
            ping < 1000 -> 3
            ping < 32767 -> 4
            else -> 5
        }
    }

}