package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BestToolsListener implements Listener {

    final BestToolsHandler handler;
    final Main main;

    BestToolsListener(@NotNull Main main) {
        this.main=Objects.requireNonNull(main,"Main must not be null");
        handler=Objects.requireNonNull(main.toolHandler,"ToolHandler must not be null");
    }





    @EventHandler
    public void onPlayerInteractWithBlock(PlayerInteractEvent event) {
        long st= main.measurePerformance ? System.nanoTime() : 0;

        // Check the cache as soon as possible
        PlayerSetting playerSetting = main.getPlayerSetting(event.getPlayer());
        if(playerSetting.btcache.valid
                && event.getClickedBlock()!=null
                && event.getClickedBlock().getType() == playerSetting.btcache.lastMat) {
            main.meter.add(st,true);
            //main.wtfdebug("Cache valid!");
            return;
        }
        Player p = event.getPlayer();
        if(!p.hasPermission("besttools.use")) {
            //main.meter.add(st);
            return;
        }
        if(!hasBestToolsEnabled(p, playerSetting)) {
            //main.meter.add(st);
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null) {
            //main.meter.add(st);
            return;
        }

        // Blacklist
        if(playerSetting.getBlacklist().contains(block.getType()))
            return;

       //main.wtfdebug("Cache invalid, doing onPlayerInteractWithBlock");
        if(!PlayerUtils.isAllowedGamemode(p,main.getConfig().getBoolean("allow-in-adventure-mode"))) {
            return;
        }
        PlayerInventory inv = p.getInventory();

        if(main.getConfig().getBoolean("dont-switch-during-battle") && handler.isWeapon(inv.getItemInMainHand())) {
            //main.debug("Return: It's a gun^^");
            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;




        ItemStack bestTool = handler.getBestToolFromInventory(block.getType(), p,playerSetting.hotbarOnly,inv.getItemInMainHand());

        if(bestTool==null || bestTool.equals(inv.getItemInMainHand())) {
            main.meter.add(st,false);
            playerSetting.btcache.validate(block.getType());
            return;
        }
        switchToBestTool(p, bestTool,playerSetting.hotbarOnly,block.getType(),playerSetting.favoriteSlot);
        playerSetting.btcache.validate(block.getType());
        main.meter.add(st,false);
    }

    private void switchToBestTool(Player p, ItemStack bestTool, boolean hotbarOnly, Material target,int favoriteSlot) {

        PlayerInventory inv = p.getInventory();
        if(bestTool == null) {
            ItemStack currentItem = inv.getItemInMainHand();

            if(currentItem==null) return; // IntelliJ says this is always false

            int emptyHotbarSlot = BestToolsHandler.getEmptyHotbarSlot(inv);
            if(emptyHotbarSlot!=-1) {
                inv.setHeldItemSlot(emptyHotbarSlot);
                return;
            }

            if(!main.toolHandler.isDamageable(currentItem)) return;
            bestTool = handler.getNonToolItemFromArray(handler.inventoryToArray(p,hotbarOnly),currentItem,target);
        }
        if(bestTool == null) {
            handler.freeSlot(favoriteSlot,inv);
            //main.debug("Could not find any appropiate tool");
            return;
        }
        int positionInInventory = handler.getPositionInInventory(bestTool,inv) ;
        if(positionInInventory != -1) {
            handler.moveToolToSlot(positionInInventory,favoriteSlot,inv);
            //main.debug("Found tool");
        } else {
            handler.freeSlot(favoriteSlot,inv);
            //main.debug("Use no tool");
        }

    }

    private boolean hasBestToolsEnabled(Player p, PlayerSetting playerSetting) {
        if(!playerSetting.bestToolsEnabled) {
            if (!playerSetting.hasSeenBestToolsMessage) {
                p.sendMessage(main.messages.MSG_BESTTOOL_USAGE);
                playerSetting.setHasSeenBestToolsMessage(true);
            }
            return false;
        }
        return true;
    }

}
