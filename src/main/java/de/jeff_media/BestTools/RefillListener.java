package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RefillListener implements Listener {

    Main main;

    RefillListener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onSnackDigestion(PlayerItemConsumeEvent event) {
        main.debug("SnackDigestion");
        refill(event.getPlayer());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        main.debug("BlockPlace");
        refill(event.getPlayer());
    }

    private void refill(Player p) {
        PlayerInventory inv = p.getInventory();
        PlayerSetting playerSetting = main.getPlayerSetting(p);
        ItemStack item = inv.getItemInMainHand();
        Material mat = item.getType();
        int currentSlot = inv.getHeldItemSlot();

        if(item.getAmount() != 1) return;

        if(!p.hasPermission("besttools.refill")) return;
        if(!playerSetting.refillEnabled) {
            showMessage(p, playerSetting);
            main.debug("ABORTING");
            return;
        }

        int refillSlot = RefillUtils.getMatchingStackPosition(inv,mat,inv.getHeldItemSlot());
        if(refillSlot != -1) {
            main.refillUtils.refillStack(inv,refillSlot,currentSlot,inv.getItem(refillSlot));
        }
    }

    private void showMessage(Player p, PlayerSetting playerSetting) {
        if(!playerSetting.hasSeenRefillMessage) {
            p.sendMessage(main.messages.MSG_REFILL_USAGE);
            playerSetting.setHasSeenRefillMessage(true);
        }
    }
}
