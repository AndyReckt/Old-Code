package net.hylist.command;

import java.util.Arrays;
import net.hylist.knockback.CraftKnockbackProfile;
import net.hylist.knockback.KnockbackProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.spigotmc.SpigotConfig;

public class KnockbackCommand extends Command {

    public KnockbackCommand() {
        super("beefwellington");

        this.setAliases(Arrays.asList("knockback", "kb"));
        this.setUsage(StringUtils.join(new String[]{
                ChatColor.BLUE + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 35),
		        ChatColor.RED + "/kb list",
                ChatColor.RED + "/kb create <name>",
                ChatColor.RED + "/kb delete <name>",
                ChatColor.RED + "/kb update <name> <f> <h> <v> <vl> <eh> <ev>",
                ChatColor.RED + "/kb setglobal <name>",
                ChatColor.BLUE + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 35)
        }, "\n"));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Unknown command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(usageMessage);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list": {
                SpigotConfig.sendKnockbackInfo(sender);
            }
            break;
            case "create": {
                if (args.length > 1) {
                    String name = args[1];

                    for (KnockbackProfile profile : SpigotConfig.kbProfiles) {
                        if (profile.getName().equalsIgnoreCase(name)) {
                            sender.sendMessage(ChatColor.RED + "A profile with that name already exists.");
                            return true;
                        }
                    }

                    CraftKnockbackProfile profile = new CraftKnockbackProfile(name);

                    SpigotConfig.kbProfiles.add(profile);
                    SpigotConfig.saveKnockbackProfiles();

                    sender.sendMessage(ChatColor.GOLD + "New profile created.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /kb create <name>");
                }
            }
            break;
            case "delete": {
                if (args.length > 1) {
                    final String name = args[1];

                    if (SpigotConfig.globalKbProfile.getName().equalsIgnoreCase(name)) {
                        sender.sendMessage(ChatColor.RED + "You can't delete the active global knockback profile.");
                        return true;
                    } else {
                        if (SpigotConfig.kbProfiles.removeIf(profile -> profile.getName().equalsIgnoreCase(name))) {
                            SpigotConfig.saveKnockbackProfiles();
                            sender.sendMessage(ChatColor.RED + "Deleted profile.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "A profile with that name couldn't be found.");
                        }

                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /kb delete <name>");
                }
            }
            break;
            case "update": {
                if (args.length == 8) {
                    KnockbackProfile profile = SpigotConfig.getKbProfileByName(args[1]);

                    if (profile == null) {
                        sender.sendMessage(ChatColor.RED + "A profile with that name couldn't be found.");
                        return true;
                    }

                    profile.setFriction(Double.parseDouble(args[2]));
                    profile.setHorizontal(Double.parseDouble(args[3]));
                    profile.setVertical(Double.parseDouble(args[4]));
                    profile.setVerticalLimit(Double.parseDouble(args[5]));
                    profile.setExtraHorizontal(Double.parseDouble(args[6]));
                    profile.setExtraVertical(Double.parseDouble(args[7]));

                    SpigotConfig.saveKnockbackProfiles();

                    sender.sendMessage(ChatColor.GREEN + "Updated values.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Wrong syntax.");
                }
            }
            break;
            case "setglobal": {
                if (args.length > 1) {
                    KnockbackProfile profile = SpigotConfig.getKbProfileByName(args[1]);

                    if (profile == null) {
                        sender.sendMessage(ChatColor.RED + "A profile with that name couldn't be found.");
                        return true;
                    }

                    SpigotConfig.globalKbProfile = profile;
                    SpigotConfig.saveKnockbackProfiles();

                    sender.sendMessage(ChatColor.GREEN + "Global profile set to " + profile.getName() + ".");
                    return true;
                }
            }
            break;
            default: {
                sender.sendMessage(usageMessage);
            }
        }

        return true;
    }

}
