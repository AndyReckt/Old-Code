/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter.impl

import cc.fyre.stark.engine.command.data.parameter.ParameterType
import cc.fyre.stark.util.enchantment.EnchantmentWrapper
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class EnchantmentParameterType : ParameterType<Enchantment?> {

    override fun transform(sender: CommandSender, source: String): Enchantment? {
        val enchantment = EnchantmentWrapper.parse(source)

        if (enchantment == null) {
            sender.sendMessage("${ChatColor.RED}No enchantment with the name " + source + " found.")
            return null
        }

        return enchantment.bukkitEnchantment
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        outer@ for (enchantment in EnchantmentWrapper.values()) {
            for (str in enchantment.parse) {
                if (StringUtils.startsWithIgnoreCase(str, source)) {
                    completions.add(str)
                    continue@outer
                }
            }

            if (StringUtils.startsWithIgnoreCase(enchantment.friendlyName, source)) {
                completions.add(enchantment.friendlyName.toLowerCase())
                continue
            }

            if (StringUtils.startsWithIgnoreCase(enchantment.bukkitEnchantment.name, source)) {
                completions.add(enchantment.friendlyName.toLowerCase())
            }

            continue
        }

        return completions
    }

}