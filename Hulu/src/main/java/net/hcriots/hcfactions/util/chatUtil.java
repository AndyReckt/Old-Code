package net.hcriots.hcfactions.util;

import lombok.Getter;
import org.bukkit.*;

import java.util.*;

public class chatUtil {


    public static String BORDER_LINE_SCOREBOARD;
    public static String UNICODE_VERTICAL_LINE;
    public static String UNICODE_CHECK_MARK;

    public static String getBorderLine() {
        return "§8§m-------------------------------";
    }

    public static String getGrayBorderLine() {
        return "§7§m-------------------------------";
    }

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String strip(String s) {
        return ChatColor.stripColor(s);
    }

    public static String stripWithoutCodes(String s) {
        return ChatColor.stripColor(s).replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "").replace("&f", "").replace("&1", "").replace("&2", "").replace("&3", "").replace("&4", "").replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "").replace("&9", "").replace("&0", "").replace("&l", "").replace("&o", "").replace("&m", "").replace("&n", "").replace("&k", "");
    }

    public static List<String> colorLines(List<String> lore) {
        List<String> color = new ArrayList<String>();
        for (String s : lore) {
            color.add(chat(s));
        }
        return color;
    }

    public static List<String> list(List<String> s) {
        List<String> strings = new ArrayList<>();
        s.forEach(str -> strings.add(ChatColor.translateAlternateColorCodes('&', str)));
        return strings;
    }

    public static String line(String color) {
        return color + "§m-----------------------------------------------------";
    }

    public static String line() {
        return "§m-----------------------------------------------------";
    }

    static {
        chatUtil.BORDER_LINE_SCOREBOARD = "§7§m-----------------";
        chatUtil.UNICODE_VERTICAL_LINE = "\u2503";
        chatUtil.UNICODE_CHECK_MARK = "\u221a";
    }
}
