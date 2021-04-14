/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.upgrades;

import net.hcriots.hcfactions.pvpclasses.pvpclasses.archer.ArcherUpgrade;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 3:44 PM
 */

public class ApolloArcherUpgrade implements ArcherUpgrade {

    @Override
    public String getUpgradeName() {
        return "Apollo";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Players shot by this upgrade will receive",
                "Blindness 2 for 5 seconds and Confusion 1",
                "for 10 seconds."
        );
    }

    @Override
    public int getKillsNeeded() {
        return 100;
    }

    @Override
    public short getMaterialData() {
        return 11;
    }

    @Override
    public void onHit(Player shooter, Player victim) {
        victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0));
    }

    @Override
    public boolean applies(Player shooter) {
        for (ItemStack itemStack : shooter.getInventory().getArmorContents()) {
            if (itemStack == null || !itemStack.getType().name().contains("LEATHER_")) {
                return false;
            }

            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();

            if (meta.getColor().getRed() != 229 || meta.getColor().getGreen() != 229 || meta.getColor().getBlue() != 51) {
                return false;
            }
        }

        return true;
    }

}