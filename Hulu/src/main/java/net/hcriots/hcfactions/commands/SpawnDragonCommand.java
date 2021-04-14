/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnDragonCommand {
    @Command(names = {"spawndragon", "spawnenderdragon"}, permission = "op")
    public static void spawnDragon(Player sender) {

        if (sender.getWorld().getEntitiesByClass(EnderDragon.class).size() != 0) {
            sender.sendMessage(ChatColor.RED + "There's already an enderdragon, jackass!");
            return;
        }

        if (sender.getWorld().getEnvironment() == World.Environment.THE_END) {
            sender.getWorld().spawnEntity(sender.getLocation(), EntityType.ENDER_DRAGON);
            sender.sendMessage(ChatColor.GREEN + "You have unleashed the beast.");
        } else {
            sender.sendMessage(ChatColor.RED + "You must be in the end to spawn the Enderdragon!");
        }
    }
}

