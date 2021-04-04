package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RefillListener implements Listener {

    final Main main;

    RefillListener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onSnackDigestion(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        attemptRefill(p);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        attemptRefill(p);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        attemptRefill(p);
    }

    private void attemptRefill(Player p) {
        attemptRefill(p,true);
        attemptRefill(p,false);
    }

    private void attemptRefill(Player p, boolean offHand) {
        if(!PlayerUtils.isAllowedGamemode(p,main.getConfig().getBoolean("allow-in-adventure-mode"))) {
            return;
        }
        PlayerInventory inv = p.getInventory();
        PlayerSetting playerSetting = main.getPlayerSetting(p);
        ItemStack item = offHand ? inv.getItemInOffHand() : inv.getItemInMainHand();
        Material mat = item.getType();
        int currentSlot = inv.getHeldItemSlot();

        if (item.getAmount() != 1) return;

        main.debug("Attempting to refill "+mat.name());

        if (!p.hasPermission("besttools.refill")) return;
        if (!playerSetting.refillEnabled) {
            if (!playerSetting.hasSeenRefillMessage) {
                Messages.sendMessage(p,main.messages.MSG_REFILL_USAGE);
                playerSetting.setHasSeenRefillMessage(true);
            }
            main.debug("ABORTING");
            return;
        }

        int refillSlot = RefillUtils.getMatchingStackPosition(inv, mat, offHand ? 45 : inv.getHeldItemSlot());
        if (refillSlot != -1) {
            main.refillUtils.refillStack(inv, refillSlot, offHand ? 40 : currentSlot, inv.getItem(refillSlot));
        }
    }
}
