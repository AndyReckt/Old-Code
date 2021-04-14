package net.hcriots.hcfactions.commands;


import cc.fyre.stark.engine.command.Command;
import cc.fyre.stark.engine.command.data.parameter.Param;
import net.hcriots.hcfactions.listener.GoldenAppleListener;
import net.hcriots.hcfactions.server.EnderpearlCooldownHandler;
import net.hcriots.hcfactions.server.SpawnTagHandler;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownCommand {

    @Command(names = {"cooldown", "timer"}, permission = "foxtrot.cooldown")
    public static void cooldownHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
        sender.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Cooldown");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_AQUA + "Types:");
        sender.sendMessage("ENDERPEARL, COMBAT, CRAPEL");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "/cooldown set <player> <type> <seconds>");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
    }

    @Command(names = {"cooldown set", "timer set"}, permission = "foxtrot.cooldown.set")
    public static void cooldownSet(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "type") String type, @Param(name = "seconds") int seconds) {
        switch (type.toUpperCase().replace("_"," ")) {
            case "ENDERPEARL": {
                if (seconds <= 0)
                    EnderpearlCooldownHandler.getEnderpearlCooldown().remove(target.getName());
                else
                    EnderpearlCooldownHandler.getEnderpearlCooldown().put(target.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));

                sender.sendMessage(ChatColor.YELLOW + "Set" + ChatColor.BLUE + " Enderpearl " + ChatColor.YELLOW + "cooldown of " + target.getDisplayName() + ChatColor.YELLOW + " to " + ChatColor.GOLD + seconds + " second" + (seconds > 1 ? "s" : ""));
                break;
            }
            case "COMBAT": {
                if (seconds <= 0)
                    SpawnTagHandler.removeTag(target);
                else
                    SpawnTagHandler.addOffensiveSeconds(target, seconds);

                sender.sendMessage(ChatColor.YELLOW + "Set" + ChatColor.DARK_RED + " Combat " + ChatColor.YELLOW + "cooldown of " + target.getDisplayName() +
                        ChatColor.YELLOW + " to " + ChatColor.GOLD + seconds + " second" + (seconds > 1 ? "s" : ""));
                break;
            }
            case "CRAPEL":{
                if (seconds <= 0)
                    GoldenAppleListener.getCrappleCooldown().remove(target.getName());
                else
                    GoldenAppleListener.getCrappleCooldown().put(UUID.fromString(target.getName()), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
                sender.sendMessage(ChatColor.YELLOW + "Set" + ChatColor.GOLD + "Crapple" + target.getDisplayName() + ChatColor.YELLOW + "to" + ChatColor.GOLD +
                        seconds + " seccond" + (seconds >1 ? "s" : ""));
            }
        }
    }

}
