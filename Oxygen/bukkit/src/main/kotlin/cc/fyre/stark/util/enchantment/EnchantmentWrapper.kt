/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.enchantment

import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack

enum class EnchantmentWrapper constructor(val friendlyName: String, parse: Array<String>) {

    PROTECTION_ENVIRONMENTAL("Protection", arrayOf<String>("p", "prot", "protect")),
    PROTECTION_FIRE("Fire Protection", arrayOf<String>("fp", "fprot", "fireprot", "fireprotection", "firep")),
    PROTECTION_FALL("Feather Falling", arrayOf<String>("ff", "featherf", "ffalling")),
    PROTECTION_EXPLOSIONS("Blast Protection", arrayOf<String>("explosionsprotection", "explosionprotection", "bprotection", "bprotect", "blastprotect", "pe", "bp")),
    PROTECTION_PROJECTILE("Projectile Protection", arrayOf<String>("pp", "projprot", "projprotection", "projp", "pprot")),
    THORNS("Thorns", arrayOf()),
    DURABILITY("Unbreaking", arrayOf<String>("unbr", "unb", "dur", "dura")),
    DAMAGE_ALL("Sharpness", arrayOf<String>("s", "sharp")),
    DAMAGE_UNDEAD("Smite", arrayOf<String>("du", "dz")),
    DAMAGE_ARTHROPODS("Bane of Arthropods", arrayOf<String>("bane", "ardmg", "baneofarthropod", "arthropod", "dar", "dspider")),
    KNOCKBACK("Knockback", arrayOf<String>("k", "knock", "kb")),
    FIRE_ASPECT("Fire Aspect", arrayOf<String>("fire", "fa")),
    OXYGEN("Respiration", arrayOf<String>("oxygen", "breathing", "o", "breath")),
    WATER_WORKER("Aqua Affinity", arrayOf<String>("aa")),
    LOOT_BONUS_MOBS("Looting", arrayOf<String>("moblooting", "ml", "loot")),
    DIG_SPEED("Efficiency", arrayOf<String>("e", "eff", "digspeed", "ds")),
    SILK_TOUCH("Silk Touch", arrayOf<String>("silk", "st")),
    LOOT_BONUS_BLOCKS("Fortune", arrayOf<String>("fort", "lbm")),
    ARROW_DAMAGE("Power", arrayOf<String>("apower", "adamage", "admg")),
    ARROW_KNOCKBACK("Punch", arrayOf<String>("akb", "arrowkb", "arrowknockback", "aknockback")),
    ARROW_FIRE("Fire", arrayOf<String>("afire", "arrowfire")),
    ARROW_INFINITE("Infinity", arrayOf<String>("infinitearrows", "infinite", "inf", "infarrows", "unlimitedarrows", "ai", "uarrows", "unlimited")),
    LUCK("Luck of the Sea", arrayOf<String>("rodluck", "luckofsea", "los")),
    LURE("Lure", arrayOf<String>("rodlure"));

    var parse: Array<String>
        internal set

    val maxLevel: Int
        get() = this.bukkitEnchantment.maxLevel

    val startLevel: Int
        get() = this.bukkitEnchantment.startLevel

    val itemTarget: EnchantmentTarget
        get() = this.bukkitEnchantment.itemTarget

    val bukkitEnchantment: Enchantment
        get() = Enchantment.getByName(this.name)

    init {
        this.parse = parse
    }

    fun enchant(item: ItemStack, level: Int) {
        item.addUnsafeEnchantment(this.bukkitEnchantment, level)
    }

    fun conflictsWith(enchantment: Enchantment): Boolean {
        return this.bukkitEnchantment.conflictsWith(enchantment)
    }

    fun canEnchantItem(item: ItemStack): Boolean {
        return this.bukkitEnchantment.canEnchantItem(item)
    }

    override fun toString(): String {
        return this.bukkitEnchantment.toString()
    }

    companion object {

        @JvmStatic
        fun parse(input: String): EnchantmentWrapper? {
            for (enchantment in values()) {
                for (str in enchantment.parse) {
                    if (str.equals(input, ignoreCase = true)) {
                        return enchantment
                    }
                }

                if (enchantment.bukkitEnchantment.name.replace("_", "").equals(input, ignoreCase = true)) {
                    return enchantment
                }

                if (enchantment.bukkitEnchantment.name.equals(input, ignoreCase = true)) {
                    return enchantment
                }

                if (enchantment.friendlyName.equals(input, ignoreCase = true)) {
                    return enchantment
                }
            }

            return null
        }

        @JvmStatic
        fun parse(enchantment: Enchantment): EnchantmentWrapper {
            for (possible in values()) {
                if (possible.bukkitEnchantment === enchantment) {
                    return possible
                }
            }
            throw IllegalArgumentException("Invalid enchantment given for parsing.")
        }
    }

}