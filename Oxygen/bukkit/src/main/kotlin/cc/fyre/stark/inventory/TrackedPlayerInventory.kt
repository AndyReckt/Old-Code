/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.inventory

import cc.fyre.stark.Stark
import net.minecraft.server.v1_7_R4.EntityHuman
import net.minecraft.server.v1_7_R4.IInventory
import net.minecraft.server.v1_7_R4.ItemStack
import net.minecraft.server.v1_7_R4.PlayerInventory
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class TrackedPlayerInventory(private val owner: CraftPlayer) : PlayerInventory(owner.handle) {

    companion object {
        @JvmStatic
        val storage: HashMap<UUID, TrackedPlayerInventory> = HashMap()

        @JvmStatic
        val open: HashSet<UUID> = HashSet()

        @JvmStatic
        fun get(player: Player): TrackedPlayerInventory {
            return storage[player.uniqueId] ?: TrackedPlayerInventory(player as CraftPlayer)
        }
    }

    private var extra: Array<ItemStack?> = arrayOfNulls(5)
    val inventory: CraftInventory = CraftInventory(this as IInventory)
    var playerOnline: Boolean = owner.isOnline

    init {
        this.items = this.player.inventory.items
        this.armor = this.player.inventory.armor

        storage[owner.uniqueId] = this
    }

    fun getBukkitInventory(): Inventory {
        return this.inventory
    }

    fun removalCheck() {
        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
            this.owner.saveData()
        }

        if (this.transaction.isEmpty() && !this.playerOnline) {
            storage.remove(this.owner.uniqueId)
        }
    }

    fun onJoin(player: Player) {
        if (!this.playerOnline) {
            val craftPlayer: CraftPlayer = player as CraftPlayer
            craftPlayer.handle.inventory.items = this.items
            craftPlayer.handle.inventory.armor = this.armor
            this.playerOnline = true

            Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                this.owner.saveData()
            }
        }
    }

    fun onQuit() {
        this.playerOnline = false
        this.removalCheck()
    }

    override fun onClose(who: CraftHumanEntity) {
        super.onClose(who)

        if (who is Player && !this.playerOnline) {
            (who as Player).sendMessage("${ChatColor.RED}Saving inventory for offline player.")
        }

        open.remove(who.uniqueId)

        this.removalCheck()
    }

    override fun getContents(): Array<ItemStack?> {
        val contents = arrayOfNulls<ItemStack>(this.size)
        System.arraycopy(this.items, 0, contents, 0, this.items.size)
        System.arraycopy(this.items, 0, contents, this.items.size, this.armor.size)
        return contents
    }

    override fun getSize(): Int {
        return super.getSize() + 5
    }

    override fun getItem(i: Int): ItemStack? {
        var i = i
        var items: Array<ItemStack?> = this.items

        if (i >= items.size) {
            i -= items.size
            items = this.armor
        } else {
            i = this.getReversedItemSlotNum(i)
        }

        if (i >= items.size) {
            i -= items.size
            items = this.extra
        } else if (items.contentEquals(this.armor)) {
            i = this.getReversedArmorSlotNum(i)
        }

        return items[i]
    }

    override fun splitStack(i: Int, j: Int): ItemStack? {
        var i = i
        var items: Array<ItemStack?> = this.items
        if (i >= items.size) {
            i -= items.size
            items = this.armor
        } else {
            i = this.getReversedItemSlotNum(i)
        }

        if (i >= items.size) {
            i -= items.size
            items = this.extra
        } else if (items.contentEquals(this.armor)) {
            i = this.getReversedArmorSlotNum(i)
        }

        if (items[i] == null) {
            return null
        }

        if (items[i]!!.count <= j) {
            val itemStack = items[i]
            items[i] = null
            return itemStack
        }

        val itemStack = items[i]!!.a(j)
        if (items[i]!!.count == 0) {
            items[i] = null
        }
        return itemStack
    }

    override fun splitWithoutUpdate(i: Int): ItemStack? {
        var i = i
        var items: Array<ItemStack?> = this.items

        if (i >= items.size) {
            i -= items.size
            items = this.armor
        } else {
            i = this.getReversedItemSlotNum(i)
        }

        if (i >= items.size) {
            i -= items.size
            items = this.extra
        } else if (items.contentEquals(this.armor)) {
            i = this.getReversedArmorSlotNum(i)
        }

        if (items[i] != null) {
            val itemStack = items[i]
            items[i] = null
            return itemStack
        }

        return null
    }

    override fun setItem(i: Int, itemStack: ItemStack?) {
        var i = i
        var itemStack = itemStack
        var items: Array<ItemStack?> = this.items

        if (i >= items.size) {
            i -= items.size
            items = this.armor
        } else {
            i = this.getReversedItemSlotNum(i)
        }

        if (i >= items.size) {
            i -= items.size
            items = this.extra
        } else if (items.contentEquals(this.armor)) {
            i = this.getReversedArmorSlotNum(i)
        }

        if (items.contentEquals(this.extra)) {
            this.owner.handle.drop(itemStack, true)
            itemStack = null
        }

        items[i] = itemStack
        this.owner.handle.defaultContainer.b()
    }

    private fun getReversedItemSlotNum(i: Int): Int {
        if (i >= 27) {
            return i - 27
        }
        return i + 9
    }

    private fun getReversedArmorSlotNum(i: Int): Int {
        return when (i) {
            0 -> 3
            1 -> 2
            2 -> 1
            3 -> 0
            else -> i
        }
    }

    override fun getInventoryName(): String {
        return "Inventory: " + this.owner.displayName
    }

    override fun a(entityHuman: EntityHuman): Boolean {
        return true
    }

}