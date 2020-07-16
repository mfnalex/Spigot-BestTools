package de.jeff_media.BestTools;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerUtils {

    static boolean isAllowedGamemode(Player p, boolean allowAdventure) {
        if(p.getGameMode()== GameMode.SURVIVAL) return true;
        return p.getGameMode() == GameMode.ADVENTURE && allowAdventure;
    }

}
