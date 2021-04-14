/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.player

import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

object HealCommand {
    private val NEGATIVE_EFFECTS: Set<PotionEffectType> = setOf(PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.HARM, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.HUNGER, PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.WITHER)

    @Command(["heal"], permission = "stark.command.heal", description = "Heal a player.")
    @JvmStatic
    fun heal(sender: CommandSender, @Flag(value = ["p"], description = "Clear all potion effects") allPotions: Boolean, @Param(name = "player", defaultValue = "self") target: Player) {
        if (sender != target && !sender.isOp) {
            sender.sendMessage(ChatColor.RED.toString() + "No permission to heal other players.")
            return
        }

        target.foodLevel = 20
        target.saturation = 10.0f
        target.health = target.maxHealth
        target.activePotionEffects.stream().filter { effect -> allPotions || NEGATIVE_EFFECTS.contains(effect.type) }.forEach { effect -> target.removePotionEffect(effect.type) }
        target.fireTicks = 0

        if (sender != target) {
            sender.sendMessage(target.displayName + ChatColor.GOLD + " has been healed.")
        }

        target.sendMessage(ChatColor.GOLD.toString() + "You have been healed.")
    }

}