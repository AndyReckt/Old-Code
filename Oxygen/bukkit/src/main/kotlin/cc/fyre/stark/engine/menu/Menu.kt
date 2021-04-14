/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.util.Callback
import net.minecraft.server.v1_7_R4.EntityPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.scheduler.BukkitRunnable
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

abstract class Menu {

    var buttons: ConcurrentHashMap<Int, Button> = ConcurrentHashMap()
    var autoUpdate: Boolean = false
    var updateAfterClick: Boolean = false
    var placeholder: Boolean = false
    var noncancellingInventory: Boolean = false
    var async: Boolean = false
    private var staticTitle: String

    constructor() {
        staticTitle = " "
    }

    constructor(title: String) {
        staticTitle = title
    }

    abstract fun getButtons(player: Player): Map<Int, Button>

    open fun getTitle(player: Player): String {
        return staticTitle
    }

    open fun onOpen(player: Player) {}

    open fun onClose(player: Player) {}

    private fun createInventory(player: Player): Inventory {
        val invButtons = getButtons(player)
        val inv = Bukkit.createInventory(player as InventoryHolder, size(invButtons), ChatColor.translateAlternateColorCodes('&', getTitle(player)))

        for (buttonEntry in invButtons.entries) {
            buttons[buttonEntry.key] = buttonEntry.value
            inv.setItem(buttonEntry.key, buttonEntry.value.getButtonItem(player))
        }

        if (placeholder) {
            val placeholder = Button.placeholder(Material.STAINED_GLASS_PANE, 15.toByte(), " ")

            for (index in 0 until size(invButtons)) {
                if (invButtons[index] == null) {
                    buttons[index] = placeholder
                    inv.setItem(index, placeholder.getButtonItem(player))
                }
            }
        }

        return inv
    }

    fun openMenu(player: Player) {
        val entityPlayer = (player as CraftPlayer).handle

        if (async) {
            Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                try {
                    asyncLoadResources(object : Callback<Boolean> {
                        override fun callback(successfulLoad: Boolean) {
                            if (successfulLoad) {
                                val inv = createInventory(player)

                                try {
                                    openInventoryMethod!!.invoke(player, inv, entityPlayer, 0)
                                    update(player)
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            } else {
                                player.sendMessage("${ChatColor.RED}Couldn't load menu...")
                            }
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    player.sendMessage("${ChatColor.RED}Couldn't load menu...")
                }
            }
        } else {
            val inv = createInventory(player)

            try {
                openInventoryMethod!!.invoke(player, inv, entityPlayer, 0)
                update(player)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun update(player: Player) {
        // cancel check
        cancelCheck(player)

        // set open menu reference to this menu
        currentlyOpenedMenus[player.name] = this

        // call abstract onOpen
        onOpen(player)

        val runnable = object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline) {
                    cancelCheck(player)
                    currentlyOpenedMenus.remove(player.name)
                }

                if (this@Menu.autoUpdate) {
                    player.openInventory.topInventory.contents = this@Menu.createInventory(player).contents
                }
            }
        }

        runnable.runTaskTimer(Stark.instance, 10L, 10L)

        checkTasks[player.name] = runnable
    }

    open fun size(buttons: Map<Int, Button>): Int {
        var highest = 0
        for (buttonValue in buttons.keys) {
            if (buttonValue > highest) {
                highest = buttonValue
            }
        }
        return (Math.ceil((highest + 1) / 9.0) * 9.0).toInt()
    }

    fun getSlot(x: Int, y: Int): Int {
        return 9 * y + x
    }

    open fun asyncLoadResources(callback: Callback<Boolean>) {}

    companion object {
        private val openInventoryMethod: Method? = CraftHumanEntity::class.java.getDeclaredMethod("openCustomInventory", Inventory::class.java, EntityPlayer::class.java, Integer.TYPE)

        @JvmStatic
        var currentlyOpenedMenus: HashMap<String, Menu> = hashMapOf()

        @JvmStatic
        var checkTasks: HashMap<String, BukkitRunnable> = hashMapOf()

        init {
            openInventoryMethod?.isAccessible = true
        }

        fun cancelCheck(player: Player) {
            if (checkTasks.containsKey(player.name)) {
                checkTasks.remove(player.name)!!.cancel()
            }
        }
    }

}