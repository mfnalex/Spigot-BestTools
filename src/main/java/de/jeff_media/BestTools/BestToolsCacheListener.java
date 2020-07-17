package de.jeff_media.BestTools;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public class BestToolsCacheListener implements @NotNull Listener {

    final Main main;

    BestToolsCacheListener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
      cacheInvalidated(e.getPlayer(),"DropItem");
    }

    @EventHandler
    public void onPlayerSwitchSlot(PlayerItemHeldEvent e) {
        cacheInvalidated(e.getPlayer(),"ItemHeldChanged");
    }

    @EventHandler
    public void onPlayerPickupTool(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(main.toolHandler.isTool(e.getItem().getItemStack())) {
            cacheInvalidated((Player) e.getEntity(),"PickupItem");
        }
    }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent e) {
        cacheInvalidated(e.getPlayer(),"ItemBreak");
    }

    void cacheInvalidated(Player p) {
        //main.wtfdebug(p.getName()+" has invalidated their best tool cache");
        main.getPlayerSetting(p).btcache.invalidated();
    }
    void cacheInvalidated(Player p,String reason) {
        //main.wtfdebug(p.getName()+" has invalidated their best tool cache because "+reason);
        main.getPlayerSetting(p).btcache.invalidated();
    }
}
