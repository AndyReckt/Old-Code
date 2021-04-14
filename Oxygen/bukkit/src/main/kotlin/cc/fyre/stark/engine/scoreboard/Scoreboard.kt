/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.scoreboard

import cc.fyre.stark.Stark
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableSet
import net.minecraft.server.v1_7_R4.Packet
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.collections.set

class Scoreboard(private val player: Player) {

    private val objective: Objective
    private val displayedScores: HashMap<String, Int> = HashMap()
    private val scorePrefixes: HashMap<String, String> = HashMap()
    private val scoreSuffixes: HashMap<String, String> = HashMap()
    private val sentTeamCreates: HashSet<String> = HashSet()
    private val separateScoreBuilder: StringBuilder = StringBuilder()
    private val separateScores: ArrayList<String> = ArrayList()
    private val recentlyUpdatedScores: HashSet<String> = HashSet()
    private val usedBaseScores: HashSet<String> = HashSet()
    private val prefixScoreSuffix: Array<String> = arrayOf("", "", "")
    private val localList: ThreadLocal<LinkedList<String>> = ThreadLocal.withInitial { LinkedList<String>() }

    init {
        val board = Stark.instance.server.scoreboardManager.newScoreboard

        objective = board.registerNewObjective("Fyre", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR

        player.scoreboard = board
    }

    fun update() {
        if (Stark.instance.scoreboardEngine.configuration == null) {
            return
        }

        val configuration = Stark.instance.scoreboardEngine.configuration!!
        val untranslatedTitle = configuration.titleGetter.getTitle(player)
        val title = ChatColor.translateAlternateColorCodes('&', untranslatedTitle)
        val lines = localList.get()

        if (!lines.isEmpty()) {
            lines.clear()
        }

        configuration.scoreGetter.getScores(this.localList.get(), this.player)
        this.recentlyUpdatedScores.clear()
        this.usedBaseScores.clear()

        var nextValue = lines.size
        Preconditions.checkArgument(lines.size < 16, "Too many lines passed!" as Any)
        Preconditions.checkArgument(title.length < 32, "Title is too long!" as Any)

        if (this.objective.displayName != title) {
            this.objective.displayName = title
        }

        for (line in lines) {
            if (48 <= line.length) {
                throw IllegalArgumentException("Line is too long! Offending line: $line")
            }

            val separated = this.separate(line, this.usedBaseScores)
            val prefix = separated[0]
            val score = separated[1]
            val suffix = separated[2]

            this.recentlyUpdatedScores.add(score)

            if (!this.sentTeamCreates.contains(score)) {
                this.createAndAddMember(score)
            }

            if (!this.displayedScores.containsKey(score) || this.displayedScores[score] != nextValue) {
                this.setScore(score, nextValue)
            }

            if (!this.scorePrefixes.containsKey(score) || !this.scorePrefixes[score].equals(prefix) || !this.scoreSuffixes[score].equals(suffix)) {
                this.updateScore(score, prefix, suffix)
            }

            --nextValue
        }

        for (displayedScore in ImmutableSet.copyOf(this.displayedScores.keys)) {
            if (this.recentlyUpdatedScores.contains(displayedScore)) {
                continue
            }

            this.removeScore(displayedScore)
        }
    }

    private fun setField(packet: Packet, field: String, value: Any) {
        try {
            val fieldObject = packet::class.java.getDeclaredField(field)
            fieldObject.isAccessible = true
            fieldObject.set(packet, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun createAndAddMember(scoreTitle: String) {
        val scoreboardTeamAdd = ScoreboardTeamPacketMod(scoreTitle, "_", "_", arrayListOf(), 0)
        val scoreboardTeamAddMember = ScoreboardTeamPacketMod(scoreTitle, arrayListOf(scoreTitle), 3)
        scoreboardTeamAdd.sendToPlayer(this.player)
        scoreboardTeamAddMember.sendToPlayer(this.player)
        this.sentTeamCreates.add(scoreTitle)
    }

    private fun setScore(score: String, value: Int) {
        val scoreboardScorePacket = PacketPlayOutScoreboardScore()
        this.setField(scoreboardScorePacket, "a", score)
        this.setField(scoreboardScorePacket, "b", this.objective.name)
        this.setField(scoreboardScorePacket, "c", value)
        this.setField(scoreboardScorePacket, "d", 0)
        this.displayedScores[score] = value
        (this.player as CraftPlayer).handle.playerConnection.sendPacket(scoreboardScorePacket)
    }

    private fun removeScore(score: String) {
        this.displayedScores.remove(score)
        this.scorePrefixes.remove(score)
        this.scoreSuffixes.remove(score)
        (this.player as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutScoreboardScore(score))
    }

    private fun updateScore(score: String, prefix: String, suffix: String) {
        this.scorePrefixes[score] = prefix
        this.scoreSuffixes[score] = suffix
        ScoreboardTeamPacketMod(score, prefix, suffix, null, 2).sendToPlayer(this.player)
    }

    private fun separate(line: String, usedBaseScores: MutableCollection<String>): Array<String> {
        var line = line
        line = ChatColor.translateAlternateColorCodes('&', line)
        var prefix = ""
        var score = ""
        var suffix = ""

        this.separateScores.clear()
        this.separateScoreBuilder.setLength(0)

        for (i in 0 until line.length) {
            val c = line[i]
            if (c == '*' || this.separateScoreBuilder.length == 16 && this.separateScores.size < 3) {
                this.separateScores.add(this.separateScoreBuilder.toString())
                this.separateScoreBuilder.setLength(0)
                if (c == '*') {
                    continue
                }
            }
            this.separateScoreBuilder.append(c)
        }

        this.separateScores.add(this.separateScoreBuilder.toString())

        when (this.separateScores.size) {
            1 -> {
                score = this.separateScores[0]
            }
            2 -> {
                score = this.separateScores[0]
                suffix = this.separateScores[1]
            }
            3 -> {
                prefix = this.separateScores[0]
                score = this.separateScores[1]
                suffix = this.separateScores[2]
            }
            else -> {
                Stark.instance.logger.warning("Failed to separate scoreboard line. Input: $line")
            }
        }

        if (usedBaseScores.contains(score)) {
            if (score.length <= 14) {
                for (chatColor in ChatColor.values()) {
                    val possibleScore = chatColor.toString() + score
                    if (!usedBaseScores.contains(possibleScore)) {
                        score = possibleScore
                        break
                    }
                }
            }
        }

        if (prefix.length > 16) {
            prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16"
        }

        if (score.length > 16) {
            score = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16"
        }

        if (suffix.length > 16) {
            suffix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16"
        }

        usedBaseScores.add(score)

        this.prefixScoreSuffix[0] = prefix
        this.prefixScoreSuffix[1] = score
        this.prefixScoreSuffix[2] = suffix

        return this.prefixScoreSuffix
    }

}