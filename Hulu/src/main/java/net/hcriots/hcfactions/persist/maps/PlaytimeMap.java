/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.persist.maps;

import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.persist.PersistMap;
import net.hcriots.hcfactions.team.Team;
import net.minecraft.util.com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PlaytimeMap extends PersistMap<Long> {

    private static final long HOUR_IN_MS = 3_600_000L;
    private final Map<UUID, Long> joinDate = new HashMap<>();
    private final Map<UUID, Long> pendingReward = Maps.newConcurrentMap();

    public PlaytimeMap() {
        super("PlayerPlaytimes", "Playtime");

        Bukkit.getScheduler().runTaskTimerAsynchronously(Hulu.getInstance(), () -> {
            List<UUID> toRemove = new ArrayList<>();
            List<UUID> toUpdate = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!(pendingReward.containsKey(player.getUniqueId()))) {
                    pendingReward.put(player.getUniqueId(), calculateNextRewardTime(player.getUniqueId()));
                }
            }

            for (UUID uuid : pendingReward.keySet()) {
                long time = pendingReward.get(uuid);

                if (Bukkit.getPlayer(uuid) == null) {
                    toRemove.add(uuid);
                    continue;
                }

                if (System.currentTimeMillis() >= time) {
                    toUpdate.add(uuid);
                }
            }

            for (UUID uuid : toRemove) {
                pendingReward.remove(uuid);
            }

            for (UUID uuid : toUpdate) {
                Player player = Bukkit.getPlayer(uuid);


                if (player != null) {
                    Team team = Hulu.getInstance().getTeamHandler().getTeam(player);

                    if (team != null) {
                        team.addPlaytimePoints(5);
                        team.sendMessage(ChatColor.GREEN + "Your team received 5 points thanks to " + ChatColor.AQUA + ChatColor.BOLD + player.getName() + ChatColor.GREEN + "'s play-time.");
                    }

                    pendingReward.put(uuid, System.currentTimeMillis() + calculateNextRewardTime(player.getUniqueId()));
                }
            }
        }, 0, 20L);
    }

    @Override
    public String getRedisValue(Long time) {
        return (String.valueOf(time));
    }

    @Override
    public Long getJavaObject(String str) {
        return (Long.parseLong(str));
    }

    @Override
    public void setCredits(int i) {

    }

    @Override
    public Object getMongoValue(Long time) {
        return (time.intValue());
    }

    public void playerJoined(UUID update) {
        joinDate.put(update, System.currentTimeMillis());

        if (!contains(update)) {
            updateValueAsync(update, 0L);
        }
    }

    public void playerQuit(UUID update, boolean async) {
        if (async) {
            updateValueAsync(update, getPlaytime(update) + (System.currentTimeMillis() - joinDate.get(update)) / 1000);
        } else {
            updateValueSync(update, getPlaytime(update) + (System.currentTimeMillis() - joinDate.get(update)) / 1000);
        }
    }

    public long getCurrentSession(UUID check) {
        if (joinDate.containsKey(check)) {
            return (System.currentTimeMillis() - joinDate.get(check));
        }

        return (0L);
    }

    public long getPlaytime(UUID check) {
        return (contains(check) ? getValue(check) : 0L);
    }

    public boolean hasPlayed(UUID check) {
        return (contains(check));
    }

    public void setPlaytime(UUID update, long playtime) {
        updateValueSync(update, playtime);
    }

    private long calculateNextRewardTime(UUID uuid) {
        return System.currentTimeMillis() + ((HOUR_IN_MS * 2) - (((getPlaytime(uuid) * 1000L) + getCurrentSession(uuid)) % (HOUR_IN_MS * 2)));
    }

}