/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.serialization

import cc.fyre.stark.Stark
import com.mongodb.BasicDBObject
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

object PlayerInventorySerializer {

    @JvmStatic
    fun serialize(player: Player): String {
        return Stark.plainGson.toJson(PlayerInventoryWrapper(player) as Any)
    }

    @JvmStatic
    fun deserialize(json: String): PlayerInventoryWrapper {
        return Stark.plainGson.fromJson(json, PlayerInventoryWrapper::class.java)
    }

    @JvmStatic
    fun getInsertableObject(player: Player): BasicDBObject {
        return BasicDBObject.parse(serialize(player))
    }

    class PlayerInventoryWrapper(player: Player) {
        val effects: Array<PotionEffect>
        val contents: Array<ItemStack?> = player.inventory.contents
        val armor: Array<ItemStack?>
        val health: Double
        val hunger: Int

        init {
            for (i in this.contents.indices) {
                val stack = this.contents[i]
                if (stack == null) {
                    this.contents[i] = ItemStack(Material.AIR, 0, 0.toShort())
                }
            }

            this.armor = player.inventory.armorContents

            for (i in this.armor.indices) {
                val stack = this.armor[i]
                if (stack == null) {
                    this.armor[i] = ItemStack(Material.AIR, 0, 0.toShort())
                }
            }

            this.effects = player.activePotionEffects.toTypedArray()
            this.health = player.health
            this.hunger = player.foodLevel
        }

        fun apply(player: Player) {
            player.inventory.contents = this.contents
            player.inventory.armorContents = this.armor

            for (effect in player.activePotionEffects) {
                player.removePotionEffect(effect.type)
            }

            for (effect2 in this.effects) {
                player.addPotionEffect(effect2)
            }
        }
    }

}