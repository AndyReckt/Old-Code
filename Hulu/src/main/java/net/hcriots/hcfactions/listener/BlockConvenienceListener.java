/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.listener;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.pvpclasses.PvPClass;
import net.hcriots.hcfactions.pvpclasses.PvPClassHandler;
import net.hcriots.hcfactions.pvpclasses.pvpclasses.MinerClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemsEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockConvenienceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExpBreak(BlockBreakEvent event) {
        event.getPlayer().giveExp(event.getExpToDrop());
        event.setExpToDrop(0);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null || event.getEntity() instanceof Player) {
            return;
        }

        killer.giveExp(event.getDroppedExp());
        event.setDroppedExp(0);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockDropItemsEvent e) {
        Player player = e.getPlayer();
        if (player != null && player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
            List<Item> toDrop = e.getToDrop();
            for (Item drop : toDrop) {
                ItemStack stack = drop.getItemStack();
                Material type = stack.getType();
                if (hasMinerClass(player) && (type == Material.COBBLESTONE) && !Hulu.getInstance().getCobblePickupMap().isCobblePickup(player.getUniqueId()))
                    continue;
                player.getInventory().addItem(autoSmelt(player, stack));
            }

            toDrop.clear();
        }
    }

    private ItemStack autoSmelt(Player player, ItemStack stack) {
        boolean hasSilkTouch = player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH);
        if (hasSilkTouch) return stack;
        Material type = stack.getType();
        if (type == Material.IRON_ORE) {
            stack.setType(Material.IRON_INGOT);
        } else if (type == Material.GOLD_ORE) {
            stack.setType(Material.GOLD_INGOT);
        }

        return stack;
    }

    private boolean hasMinerClass(Player player) {
        PvPClass pvpClass = PvPClassHandler.getPvPClass(player);
        return pvpClass != null && pvpClass.getClass() == MinerClass.class;
    }
}
