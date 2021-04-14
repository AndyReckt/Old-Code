/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.events.citadel;

import cc.fyre.stark.Stark;
import cc.fyre.stark.util.serialization.ItemStackSerializer;
import cc.fyre.stark.util.serialization.LocationSerializer;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import lombok.Getter;
import lombok.Setter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.events.citadel.events.CitadelCapturedEvent;
import net.hcriots.hcfactions.events.citadel.listeners.CitadelListener;
import net.hcriots.hcfactions.events.citadel.tasks.CitadelSaveTask;
import net.hcriots.hcfactions.team.Team;
import net.hcriots.hcfactions.team.claims.Claim;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import net.hcriots.hcfactions.util.CuboidRegion;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CitadelHandler {

    public static final String PREFIX = ChatColor.DARK_PURPLE + "[Citadel]";

    private final File citadelInfo;
    @Getter
    private final Set<Location> citadelChests = new HashSet<>();
    @Getter
    private final List<ItemStack> citadelLoot = new ArrayList<>();
    @Getter
    private Set<ObjectId> cappers;
    @Getter
    private Date lootable;

    @Getter
    @Setter
    private long citadelCapTime = 0;

    public CitadelHandler() {
        citadelInfo = new File(Hulu.getInstance().getDataFolder(), "citadelInfo.json");

        loadCitadelInfo();
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new CitadelListener(), Hulu.getInstance());

        (new CitadelSaveTask()).runTaskTimerAsynchronously(Hulu.getInstance(), 0L, 20 * 60 * 5);
    }

    public void loadCitadelInfo() {
        try {
            if (!citadelInfo.exists() && citadelInfo.createNewFile()) {
                BasicDBObject dbo = new BasicDBObject();

                dbo.put("cappers", new HashSet<>());
                dbo.put("lootable", new Date());
                dbo.put("chests", new BasicDBList());
                dbo.put("loot", new BasicDBList());

                FileUtils.write(citadelInfo, Stark.getGson().toJson(new JsonParser().parse(dbo.toString())));
            }

            BasicDBObject dbo = (BasicDBObject) JSON.parse(FileUtils.readFileToString(citadelInfo));

            if (dbo != null) {
                this.cappers = new HashSet<>();

                // Conversion
                if (dbo.containsField("capper")) {
                    cappers.add(new ObjectId(dbo.getString("capper")));
                }

                for (String capper : (List<String>) dbo.get("cappers")) {
                    cappers.add(new ObjectId(capper));
                }

                this.lootable = dbo.getDate("lootable");

                BasicDBList chests = (BasicDBList) dbo.get("chests");
                BasicDBList loot = (BasicDBList) dbo.get("loot");

                for (Object chestObj : chests) {
                    BasicDBObject chest = (BasicDBObject) chestObj;
                    citadelChests.add(LocationSerializer.deserialize((BasicDBObject) chest.get("location")));
                }

                for (Object lootObj : loot) {
                    citadelLoot.add(ItemStackSerializer.deserialize((BasicDBObject) lootObj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCitadelInfo() {
        try {
            BasicDBObject dbo = new BasicDBObject();

            dbo.put("cappers", cappers.stream().map(ObjectId::toString).collect(Collectors.toList()));
            dbo.put("lootable", lootable);

            BasicDBList chests = new BasicDBList();
            BasicDBList loot = new BasicDBList();

            for (Location citadelChest : citadelChests) {
                BasicDBObject chest = new BasicDBObject();
                chest.put("location", LocationSerializer.serialize(citadelChest));
                chests.add(chest);
            }

            for (ItemStack lootItem : citadelLoot) {
                loot.add(ItemStackSerializer.serialize(lootItem));
            }

            dbo.put("chests", chests);
            dbo.put("loot", loot);

            citadelInfo.delete();
            FileUtils.write(citadelInfo, Stark.getGson().toJson(new JsonParser().parse(dbo.toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCappers() {
        this.cappers.clear();
    }

    public void addCapper(ObjectId capper, Player player) {
        this.cappers.add(capper);
        this.lootable = generateLootableDate();

        Hulu.getInstance().getServer().getPluginManager().callEvent(new CitadelCapturedEvent(capper, player));
        saveCitadelInfo();
    }

    public boolean canLootCitadel(Player player) {
        Team team = Hulu.getInstance().getTeamHandler().getTeam(player);
        return ((team != null && cappers.contains(team.getUniqueId())) || System.currentTimeMillis() > lootable.getTime());
    }

    // Credit to http://stackoverflow.com/a/3465656 on StackOverflow.
    private Date generateLootableDate() {
        Calendar date = Calendar.getInstance();
        int diff = Calendar.TUESDAY - date.get(Calendar.DAY_OF_WEEK);

        if (diff <= 0) {
            diff += 7;
        }

        date.add(Calendar.DAY_OF_MONTH, diff);

        // 11:59 PM
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);

        return (date.getTime());
    }

    public void scanLoot() {
        citadelChests.clear();

        for (Team team : Hulu.getInstance().getTeamHandler().getTeams()) {
            if (team.getOwner() != null) {
                continue;
            }

            if (team.hasDTRBitmask(DTRBitmask.CITADEL)) {
                for (Claim claim : team.getClaims()) {
                    for (Location location : new CuboidRegion("Citadel", claim.getMinimumPoint(), claim.getMaximumPoint())) {
                        if (location.getBlock().getType() == Material.CHEST) {
                            citadelChests.add(location);
                        }
                    }
                }
            }
        }
    }

    public int respawnCitadelChests() {
        int respawned = 0;

        for (Location chest : citadelChests) {
            if (respawnCitadelChest(chest)) {
                respawned++;
            }
        }

        return (respawned);
    }

    public boolean respawnCitadelChest(Location location) {
        BlockState blockState = location.getBlock().getState();

        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            chest.getBlockInventory().clear();
            chest.getBlockInventory().addItem(citadelLoot.get(ThreadLocalRandom.current().nextInt(citadelLoot.size())));
            return (true);
        } else {
            Hulu.getInstance().getLogger().warning("Citadel chest defined at [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "] isn't a chest!");
            return (false);
        }
    }

}