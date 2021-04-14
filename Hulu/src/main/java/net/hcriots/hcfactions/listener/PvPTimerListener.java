/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import cc.fyre.stark.util.PlayerUtils;
import net.hcriots.hcfactions.Hulu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PvPTimerListener implements Listener {

    private final Set<Integer> droppedItems = new HashSet<>();

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (Hulu.getInstance().getPvPTimerMap().hasTimer(event.getPlayer().getUniqueId())) {
            if (droppedItems.contains(event.getItem().getEntityId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().contains("§8PVP Loot")) {
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = meta.getLore();

            lore.remove("§8PVP Loot");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            event.getEntity().setItemStack(itemStack);

            final int id = event.getEntity().getEntityId();

            droppedItems.add(id);

            new BukkitRunnable() {

                public void run() {
                    droppedItems.remove(id);
                }

            }.runTaskLater(Hulu.getInstance(), 20L * 60);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        for (ItemStack itemStack : event.getDrops()) {
            ItemMeta meta = itemStack.getItemMeta();

            List<String> lore = new ArrayList<>();

            if (meta.hasLore()) {
                lore = meta.getLore();
            }

            lore.add("§8PVP Loot");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You cannot do this while your PVPTimer is active!");
                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To remove your PvPTimer type '" + ChatColor.WHITE + "/pvp enable" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "'.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = PlayerUtils.getDamageSource(event.getDamager()); // find the player damager if one exists

        if (damager == null) {
            return;
        }

        if (Hulu.getInstance().getPvPTimerMap().hasTimer(damager.getUniqueId())) {
            damager.sendMessage(ChatColor.RED + "You cannot do this while your PVPTimer is active!");
            damager.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To remove your PvPTimer type '" + ChatColor.WHITE + "/pvp enable" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "'.");
            event.setCancelled(true);
            return;
        }

        if (Hulu.getInstance().getPvPTimerMap().hasTimer(event.getEntity().getUniqueId())) {
            damager.sendMessage(ChatColor.RED + "That player currently has their PVP Timer!");
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && (event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)) {
            Player player = (Player) event.getEntity();

            if (Hulu.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

}