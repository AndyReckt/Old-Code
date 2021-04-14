package net.hcriots.hcfactions.tab;

import org.bukkit.entity.Player;

public interface TablistEntrySupplier
{
    net.minecraft.util.com.google.common.collect.Table<Integer, Integer, String> getEntries(final Player p0);
    
    String getHeader(final Player p0);
    
    String getFooter(final Player p0);
}
