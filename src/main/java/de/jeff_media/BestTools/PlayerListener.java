package de.jeff_media.BestTools;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    Main main;

    PlayerListener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(!main.playerSettings.containsKey(p.getUniqueId())) return;
        PlayerSetting setting = main.getPlayerSetting(p);
        setting.save(main.getPlayerDataFile(p.getUniqueId()),main);
        main.playerSettings.remove(p.getUniqueId());
    }

}
