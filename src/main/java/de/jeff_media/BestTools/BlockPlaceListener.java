package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockPlaceListener implements Listener {

    Main main;

    BlockPlaceListener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        main.debug("BlockPlace");
        Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
        PlayerSetting setting = main.getPlayerSetting(p);
        ItemStack item = inv.getItemInMainHand();
        Material mat = item.getType();
        int currentSlot = inv.getHeldItemSlot();

        if(!p.hasPermission("besttool.refill")) return;
        if(!setting.refillEnabled) return;
        if(item.getAmount() != 1) return;

        int refillSlot = RefillUtils.getMatchingStackPosition(inv,mat,inv.getHeldItemSlot());
        if(refillSlot != -1) {
            main.refillUtils.refillStack(inv,refillSlot,currentSlot,inv.getItem(refillSlot));
        }
    }
}
