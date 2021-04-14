/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.enchantment

import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.enchantments.EnchantmentWrapper
import org.bukkit.inventory.ItemStack

/**
 * Created by DaddyDombo daddydombo@gmail.com on 8/5/2020.
 */
class GlowEnchantment(id: Int) : EnchantmentWrapper(id) {

    override fun canEnchantItem(item: ItemStack): Boolean {
        return true
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun getItemTarget(): EnchantmentTarget? {
        return null
    }

    override fun getMaxLevel(): Int {
        return 10
    }

    override fun getName(): String {
        return "Glow"
    }

    override fun getStartLevel(): Int {
        return 1
    }

    companion object {

        private var glow: Enchantment? = null

        fun getGlow(): Enchantment {
            if (glow != null) {
                return glow!!
            }

            try {
                val f = Enchantment::class.java.getDeclaredField("acceptingNew")
                f.isAccessible = true
                f.set(null, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            glow = GlowEnchantment(255)

            if (Enchantment.getByName("Glow") == null) {
                Enchantment.registerEnchantment(glow!!)
            }

            return glow!!
        }

        fun addGlow(item: ItemStack) {
            val glow =
                    getGlow()

            if (item.enchantments.isEmpty()) {
                item.addEnchantment(glow, 1)
            } else {
                return
            }
        }

    }
}