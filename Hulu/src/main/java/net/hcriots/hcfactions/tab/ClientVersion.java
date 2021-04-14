package net.hcriots.hcfactions.tab;

import net.hcriots.hcfactions.tab.reflection.ReflectionConstants;
import org.bukkit.entity.Player;
public enum ClientVersion {
    v1_7, 
    v1_8;
    public static ClientVersion getVersion(Player player) {
        Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke(player, new Object[0]);
        Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
        Object manager = ReflectionConstants.NETWORK_MANAGER.get(connection);
        Object version = ReflectionConstants.VERSION_METHOD.invoke(manager, new Object[0]);
        if (version instanceof Integer) {
            return ((int)version > 5) ? ClientVersion.v1_8 : ClientVersion.v1_7;
        }
        return ClientVersion.v1_7;
    }
}
