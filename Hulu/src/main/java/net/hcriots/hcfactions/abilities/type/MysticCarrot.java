package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.util.chatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MysticCarrot extends Ability {

    //mystic carrot (golden carrot) upon right clicking this item, the player is granted Res 3, Fire resistance, and Slowness for 15 seconds

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }


    @Override
    public Material getMaterial() {
        return Material.GOLDEN_CARROT;
    }

    @Override
    public String getDisplayName() {
        return chatUtil.chat("&a&lMystic Carrot");
    }

    @Override
    public List<String> getLore() {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add(ChatColor.translateAlternateColorCodes(
                '&', "&7&oUpon right clicking you'll receive res3, fire resm and slowness for 15 sec."));
        return toReturn;
    }

    @Override
    public long getCooldown() {
        return (long) (60.0 * 1000L);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PotionEffectType res = PotionEffectType.DAMAGE_RESISTANCE;
        PotionEffectType fireres = PotionEffectType.FIRE_RESISTANCE;
        PotionEffectType slowness = PotionEffectType.SLOW;

        if(!event.getAction().name().equals("RIGHT")) {
            return;
        }

        if (this.hasCooldown(player)) {
            this.sendCooldownMessage(player);
            event.setCancelled(true);
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        this.applyCooldown(player);
        player.addPotionEffect(new PotionEffect(res, 15, 4));
        player.addPotionEffect(new PotionEffect(fireres, 15, 0));
        player.addPotionEffect(new PotionEffect(slowness, 15, 0));
        ItemStack itemStack = player.getItemInHand();
        itemStack.setAmount(itemStack.getAmount() - 1);

        player.sendMessage(new String[]{
                ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"),
                ChatColor.GRAY + "You have used your " + this.getDisplayName(),
                "",
                ChatColor.GRAY + "You have been given: ",
                ChatColor.YELLOW + "Resistance III: " + ChatColor.WHITE + "15 seconds",
                ChatColor.YELLOW + "Fire Resistance: " + ChatColor.WHITE + "15 seconds",
                ChatColor.YELLOW + "Slowness: " + ChatColor.WHITE + "15 seconds",
                ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"),

        });


    }

}
