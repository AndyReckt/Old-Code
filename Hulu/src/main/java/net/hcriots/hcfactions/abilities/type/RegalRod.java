package net.hcriots.hcfactions.abilities.type;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.abilities.Ability;
import net.hcriots.hcfactions.util.Color;
import net.hcriots.hcfactions.util.chatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RegalRod extends Ability {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Material getMaterial() {
        return Material.BONE;
    }

    @Override
    public String getDisplayName() {
        return chatUtil.chat("&c&lRegal Rod");
    }

    @Override
    public List<String> getLore() {
        ArrayList<String> toReturn = new ArrayList();
        toReturn.add(chatUtil.chat("&cLeft click a player to make them no build!"));
        return toReturn;

    }

    @Override
    public long getCooldown() {
        return (long) (120.0 * 1000L);
    }

    public static ArrayList<Player> noBuild = new ArrayList<>();
    public static ArrayList<Player> firsthit = new ArrayList<>();
    public static ArrayList<Player> secondhit = new ArrayList<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (this.hasCooldown(player)) {
            this.sendCooldownMessage(player);
            event.setCancelled(true);
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }



        firsthit.add(target);
        player.sendMessage(chatUtil.chat("&c&lYou need to hit them &ltwo more times."));

        if(firsthit.contains(player)) {
            secondhit.add(target);
            firsthit.remove(player);
            player.sendMessage(chatUtil.chat("&c&lYou need to hit them &lone more time."));

        } else if (secondhit.contains(player)) {
            this.applyCooldown(player);
            noBuild.add(target);
            secondhit.remove(player);
            ItemStack itemStack = player.getItemInHand();
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.sendMessage(chatUtil.chat("&aYou have successfully hit &l" + target.getDisplayName() + " &awith the bone, they can no longer build for 30 seconds!"));
            target.sendMessage(chatUtil.chat("&cYou cannot build for &l30 seconds&c due to &a" + player.getDisplayName() + " &chitting you with the bone."));
        }

        event.setCancelled(true);


        new BukkitRunnable() {
            @Override
            public void run() {
                noBuild.remove(target);

            }
        }.runTaskLater(Hulu.getInstance(), 600L);

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (noBuild.contains(player)) {
            event.setCancelled(true);
            player.sendMessage(chatUtil.chat("&c&lYou currently cannot do this!"));

        }

    }
}
