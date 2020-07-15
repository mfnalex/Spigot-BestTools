package de.jeff_media.BestTools;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BestToolsListener implements Listener {

    BestToolsHandler handler;
    Main main;

    BestToolsListener(@NotNull Main main) {
        this.main=Objects.requireNonNull(main,"Main must not be null");
        handler=Objects.requireNonNull(main.toolHandler,"ToolHandler must not be null");
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
        Block block = event.getClickedBlock();
        PlayerSetting playerSetting = main.getPlayerSetting(p);

        if(!p.hasPermission("besttools.use")) return;

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (block == null) return;

        main.debug("This player has enabled: "+playerSetting.bestToolsEnabled);

        if(!playerSetting.bestToolsEnabled) {
            if(!playerSetting.hasSeenBestToolsMessage) {
                p.sendMessage(main.messages.MSG_BESTTOOL_USAGE);
                playerSetting.setHasSeenBestToolsMessage(true);
            }
            main.debug("ABORTING");
            return;
        }

        ItemStack bestTool = handler.getBestToolFromInventory(block.getType(), p);
        if(bestTool == null) {
            ItemStack currentItem = inv.getItemInMainHand();

            if(currentItem==null) return;

            int emptyHotbarSlot = BestToolsHandler.getEmptyHotbarSlot(inv);
            if(emptyHotbarSlot!=-1) {
                inv.setHeldItemSlot(emptyHotbarSlot);
                return;
            }

            if(!main.toolHandler.isDamageable(currentItem)) return;
            bestTool = handler.getNonToolItemFromArray(handler.inventoryToArray(p));
        }
        if(bestTool == null) {
            handler.freeSlot(handler.favoriteSlot,inv);
            main.debug("Could not find any appropiate tool");
            return;
        }
        int positionInInventory = handler.getPositionInInventory(bestTool,inv) ;
        if(positionInInventory != -1) {
            handler.moveToolToSlot(positionInInventory,handler.favoriteSlot,inv);
            main.debug("Found tool");
        } else {
            handler.freeSlot(handler.favoriteSlot,inv);
            main.debug("Use no tool");
        }
    }

}
