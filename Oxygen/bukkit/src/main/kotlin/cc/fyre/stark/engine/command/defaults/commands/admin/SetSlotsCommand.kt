/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import net.minecraft.server.v1_7_R4.PlayerList
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_7_R4.CraftServer
import java.lang.reflect.Field

object SetSlotsCommand {

    private var maxPlayerField: Field = PlayerList::class.java.getDeclaredField("maxPlayers")

    init {
        maxPlayerField.isAccessible = true

        load()
    }

    @Command(["setslots", "setmaxslots", "setservercap", "ssc"], permission = "op", description = "Set the max slots")
    @JvmStatic
    fun setslots(sender: CommandSender, @Param(name = "slots") slots: Int) {
        if (slots < 0) {
            sender.sendMessage("${ChatColor.RED}The number of slots must be greater or equal to zero.")
            return
        }

        set(slots)
        sender.sendMessage("${ChatColor.GOLD}Slots set to ${ChatColor.WHITE}$slots${ChatColor.GOLD}.")
    }

    fun set(slots: Int) {
        try {
            maxPlayerField.set((Bukkit.getServer() as CraftServer).handle, slots)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        save()
    }

    fun save() {
        Stark.instance.config.set("slots", Bukkit.getMaxPlayers() as Any)
        Stark.instance.saveConfig()
    }

    private fun load() {
        if (Stark.instance.config.contains("slots")) {
            set(Stark.instance.config.getInt("slots"))
        } else {
            Stark.instance.config.set("slots", Bukkit.getMaxPlayers() as Any)
        }
    }

}