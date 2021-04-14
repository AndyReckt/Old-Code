/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.messaging

import cc.fyre.stark.Stark
import cc.fyre.stark.messaging.event.PlayerMessageEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

class MessagingManager {

    private val key = "stark:messageSettings"
    val globalSpy: MutableSet<UUID> = hashSetOf()

    /**
     * Gets the last messaged player for a player.
     *
     * @param player the player
     *
     * @return the last player's uuid that [player] has messaged
     */
    fun getLastMessaged(player: UUID): UUID? {
        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hget("$key:$player", "lastMessaged")
        }?.run { UUID.fromString(this) }
    }

    /**
     * Sets the last messaged player for a player.
     *
     * @param player the player
     */
    fun setLastMessaged(player: UUID, lastMessaged: UUID) {
        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hset("$key:$player", hashMapOf("lastMessaged" to "$lastMessaged"))
        }
    }

    /**
     * Gets if a player's messages are disabled.
     *
     * @param player the player
     */
    fun isMessagesDisabled(player: UUID): Boolean {
        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hget("$key:$player", "messagesDisabled")
        }?.toBoolean() ?: false
    }

    /**
     * Toggles if a player's messages are disabled.
     *
     * @param player the player
     *
     * @return if the [player]'s messages are disabled after toggle
     */
    fun toggleMessages(player: UUID): Boolean {
        val value = !isMessagesDisabled(player)

        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hmset("$key:$player", hashMapOf("messagesDisabled" to value.toString()))
        }

        return value
    }


    /**
     * Gets if a player's message sounds are disabled.
     *
     * @param player the player
     */
    fun isSoundsDisabled(player: UUID): Boolean {
        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hget("$key:$player", "soundsDisabled")
        }?.toBoolean() ?: false
    }

    /**
     * Toggles if a player's message sounds are disabled.
     *
     * @param player the player
     *
     * @return if the [player]'s message sounds are disabled after toggle
     */
    fun toggleSounds(player: UUID): Boolean {
        val value = !isSoundsDisabled(player)

        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hmset("$key:$player", hashMapOf("soundsDisabled" to value.toString()))
        }

        return value
    }

    /**
     * Gets whether or not the [player] is ignored by the [target].
     *
     * @param player the player
     * @param target the target
     *
     * @return if the [player] is ignored by the [target]
     */
    fun isIgnored(player: UUID, target: UUID): Boolean {
        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hget("$key:$target", "$player")
        }?.toBoolean() ?: false
    }

    /**
     * Adds a target to a player's ignore list.
     */
    fun addToIgnoreList(player: UUID, target: UUID) {
        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hmset("$key:ignoreList:$player", hashMapOf(target.toString() to "true"))
        }
    }

    /**
     * Removes a target from a player's ignore list.
     *
     * @param player the player's UUID
     * @param target the target's UUID
     */
    fun removeFromIgnoreList(player: UUID, target: UUID) {
        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hdel("$key:ignoreList:$player", target.toString())
        }
    }

    /**
     * Clears a player's ignore list.
     *
     * @param player the player's UUID
     */
    fun clearIgnoreList(player: UUID) {
        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.del("$key:ignoreList:$player")
        }
    }

    /**
     * Retrieves a player's ignore list.
     *
     * @param player the player's UUID
     *
     * @return the player's ignore list as {@link List<UUID>}
     */
    fun getIgnoreList(player: UUID): List<UUID> {
        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hgetAll("$key:ignoreList:$player").map { entry -> UUID.fromString(entry.key) }
        }
    }

    /**
     * Determines whether a sender player can message a target.
     *
     * @param sender the player trying to send the message
     * @param target the player the [sender] is trying to send a message to
     *
     * @return true if the sender can message the target.
     */
    fun canMessage(sender: Player, target: Player): Boolean {
        if (isIgnored(sender.uniqueId, target.uniqueId)) {
            sender.sendMessage(ChatColor.RED.toString() + "That player is ignoring you.")
            return false
        }

        if (isIgnored(target.uniqueId, sender.uniqueId)) {
            sender.sendMessage(ChatColor.RED.toString() + "You are ignoring that player.")
            return false
        }

        if (isMessagesDisabled(sender.uniqueId)) {
            sender.sendMessage(ChatColor.RED.toString() + "You have messages turned off.")
            return false
        }

        if (isMessagesDisabled(target.uniqueId)) {
            sender.sendMessage(target.displayName + ChatColor.RED + " has messages turned off.")
            return false
        }

        return true
    }

    /**
     * Sends a [message] from the [sender] to the [target].
     *
     * @param sender the player sending the [message]
     * @param target the player the [sender] is sending the [message] to
     * @param message the message the [sender] is trying to send to the [target]
     */
    fun sendMessage(sender: Player, target: Player, message: String) {
        val event = PlayerMessageEvent(sender, target, message)

        Stark.instance.server.pluginManager.callEvent(event)

        if (event.isCancelled) {
            return
        }

        setLastMessaged(sender.uniqueId, target.uniqueId)
        setLastMessaged(target.uniqueId, sender.uniqueId)

        val senderProfile = Stark.instance.core.getProfileHandler().getByUUID(sender.uniqueId)!!
        val targetProfile = Stark.instance.core.getProfileHandler().getByUUID(target.uniqueId)!!

        target.sendMessage(ChatColor.GRAY.toString() + "(From " + senderProfile.getPlayerListName() + ChatColor.GRAY + ") " + message)
        sender.sendMessage(ChatColor.GRAY.toString() + "(To " + targetProfile.getPlayerListName() + ChatColor.GRAY + ") " + message)

        if (!isSoundsDisabled(target.uniqueId)) {
            target.playSound(target.location, Sound.SUCCESSFUL_HIT, 1.0F, 0.1F)
        }

        for (player in Bukkit.getOnlinePlayers()) {
            if (player !== sender) {
                if (player === target) {
                    continue
                }

                if (globalSpy.contains(player.uniqueId)) {
                    player.sendMessage(ChatColor.GRAY.toString() + "(" + ChatColor.WHITE + sender.displayName + ChatColor.GRAY + " to " + ChatColor.WHITE + target.displayName + ChatColor.GRAY + ") " + message)
                }
            }
        }
    }
}