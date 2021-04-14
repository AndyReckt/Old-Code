/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.player

import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * Created by DaddyDombo daddydombo@gmail.com on 6/4/2020.
 */
object NightVisionCommand {

    @Command(names = ["nv", "nightvision"], permission = "stark.command.nightvision")
    @JvmStatic
    fun nv(sender: Player) {
        if (sender.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            sender.removePotionEffect(PotionEffectType.NIGHT_VISION)
            sender.sendMessage("${ChatColor.RED}Your night vision has been removed.")
        } else {
            sender.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 9999999, 1))
            sender.sendMessage("${ChatColor.GREEN}Your night vision has been activated.")
        }
    }
}