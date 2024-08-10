package de.jeff_media.BestTools;

import de.jeff_media.BestTools.events.BestToolsNotifyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BestToolsListener implements Listener {

    final BestToolsHandler handler;
    final Main main;
    boolean useAxeAsWeapon;

    BestToolsListener(@NotNull Main main) {
        this.main=Objects.requireNonNull(main,"Main must not be null");
        handler=Objects.requireNonNull(main.toolHandler,"ToolHandler must not be null");
        useAxeAsWeapon = main.getConfig().getBoolean("use-axe-as-sword");
    }


    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByEntityEvent e) {
        long st= main.measurePerformance ? System.nanoTime() : 0;
        main.debug("EntityDamageByEntity 1");

        if (!(e.getDamager() instanceof Player)) return;
        main.debug("EntityDamageByEntity 2");
        Player p = (Player) e.getDamager();
        if(!p.hasPermission("besttools.use")) return;
        main.debug("EntityDamageByEntity 3");
        PlayerSetting playerSetting = main.getPlayerSetting(p);
        if(!playerSetting.isBestToolsEnabled()) return;
        main.debug("EntityDamageByEntity 4");
        Entity enemy = e.getEntity();

        if(!PlayerUtils.isAllowedGamemode(p,main.getConfig().getBoolean("allow-in-adventure-mode"))) {
            return;
        }

        if (!
                (enemy instanceof Monster && playerSetting.isSwordOnMobs())
            // || (enemy instanceof Player && playerSetting.swordOnPlayers)
        ) return;

        main.debug("Getting the best roscoe for "+enemy.getType().name());

        PlayerInventory inv = p.getInventory();
        ItemStack bestRoscoe = handler.getBestRoscoeFromInventory(enemy.getType(), p,playerSetting.isHotbarOnly(),inv.getItemInMainHand(),useAxeAsWeapon);

        if(bestRoscoe==null || bestRoscoe.equals(inv.getItemInMainHand())) {
            main.meter.add(st,false);
            //playerSetting.getBtcache().validate(enemy.getType());
            return;
        }
        switchToBestRoscoe(p, bestRoscoe,playerSetting.isHotbarOnly(),playerSetting.getFavoriteSlot());
        //playerSetting.getBtcache().validate(enemy.getType());
        main.meter.add(st,false);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBreak(BlockBreakEvent event) {
        //System.out.println("BlockBreakEvent LISTENER");
        //System.out.println(event.getBlock());
        Bukkit.getScheduler().runTaskLater(main, () -> {
            Bukkit.getPluginManager().callEvent(new BestToolsNotifyEvent(event.getPlayer(), event.getBlock()));
            //Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(event.getPlayer(), Action.LEFT_CLICK_BLOCK, event.getPlayer().getInventory().getItemInMainHand(),event.getBlock(),BlockFace.SELF,EquipmentSlot.HAND));
        },1);
    }

    @EventHandler
    public void onNotify(BestToolsNotifyEvent event) {
        onPlayerInteractWithBlock(new PlayerInteractEvent(event.getPlayer(), Action.LEFT_CLICK_BLOCK, event.getPlayer().getInventory().getItemInMainHand(),event.getBlock(),BlockFace.SELF,EquipmentSlot.HAND));
    }

    @EventHandler
    public void onPlayerInteractWithBlock(PlayerInteractEvent event) {

        // DEBUG
        //for (RegisteredListener registeredListener : event.getHandlers().getRegisteredListeners()) {
        //    main.debug(registeredListener.getPlugin().getName()+": "+registeredListener.getListener().getClass().getName() + " @ "+registeredListener.getPriority().name());
        //}
//        if(main.debug && event.getAction() == Action.LEFT_CLICK_BLOCK) {
//            main.getLogger().warning(event.getClickedBlock().getType().name());
//        }
        //

        long st= main.measurePerformance ? System.nanoTime() : 0;

        // Check the cache as soon as possible
        PlayerSetting playerSetting = main.getPlayerSetting(event.getPlayer());
        if(playerSetting.getBtcache().valid
                && event.getClickedBlock()!=null
                && event.getClickedBlock().getType() == playerSetting.getBtcache().lastMat) {
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

        if(block.getType() == Material.AIR) {
            return;
        }

        if(main.toolHandler.globalBlacklist.contains(block.getType())) {
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
            main.debug("Return: It's a gun^^");
            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack bestTool = handler.getBestToolFromInventory(block.getType(), p,playerSetting.isHotbarOnly(),inv.getItemInMainHand());

        if(bestTool==null || bestTool.equals(inv.getItemInMainHand())) {
            main.meter.add(st,false);
            playerSetting.getBtcache().validate(block.getType());
            return;
        }
        switchToBestTool(p, bestTool,playerSetting.isHotbarOnly(),block.getType()/*,playerSetting.getFavoriteSlot()*/);
        playerSetting.getBtcache().validate(block.getType());
        main.meter.add(st,false);
    }

    private int getFavoriteSlot(Player player) {
        if(main.getConfig().getInt("favorite-slot")==-1) {
            return player.getInventory().getHeldItemSlot();
        } else {
            return main.getConfig().getInt("favorite-slot");
        }
    }

    private void switchToBestTool(Player p, ItemStack bestTool, boolean hotbarOnly, Material target) {

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
            handler.freeSlot(getFavoriteSlot(p),inv);
            main.debug("Could not find any appropiate tool");
            return;
        }
        int positionInInventory = handler.getPositionInInventory(bestTool,inv) ;
        if(positionInInventory != -1) {
            handler.moveToolToSlot(positionInInventory,getFavoriteSlot(p),inv);
            main.debug("Found tool");
        } else {
            handler.freeSlot(getFavoriteSlot(p),inv);
            main.debug("Use no tool");
        }

    }

    private void switchToBestRoscoe(Player p, ItemStack bestRoscoe, boolean hotbarOnly, int favoriteSlot) {

        PlayerInventory inv = p.getInventory();
        if(bestRoscoe == null) {
            ItemStack currentItem = inv.getItemInMainHand();

            if(currentItem==null) return; // IntelliJ says this is always false

            int emptyHotbarSlot = BestToolsHandler.getEmptyHotbarSlot(inv);
            if(emptyHotbarSlot!=-1) {
                inv.setHeldItemSlot(emptyHotbarSlot);
                return;
            }

            if(!main.toolHandler.isDamageable(currentItem)) return;
            bestRoscoe = handler.getNonToolItemFromArray(handler.inventoryToArray(p,hotbarOnly),currentItem,Material.BEDROCK);
        }
        if(bestRoscoe == null) {
            handler.freeSlot(favoriteSlot,inv);
            main.debug("Could not find any appropiate tool");
            return;
        }
        int positionInInventory = handler.getPositionInInventory(bestRoscoe,inv) ;
        if(positionInInventory != -1) {
            handler.moveToolToSlot(positionInInventory,favoriteSlot,inv);
            main.debug("Found tool");
        } else {
            handler.freeSlot(favoriteSlot,inv);
            main.debug("Use no tool");
        }

    }

    private boolean hasBestToolsEnabled(Player p, PlayerSetting playerSetting) {
        if(!playerSetting.isBestToolsEnabled()) {
            if (!playerSetting.isHasSeenBestToolsMessage()) {
                Messages.sendMessage(p,main.messages.MSG_BESTTOOL_USAGE);
                playerSetting.setHasSeenBestToolsMessage(true);
            }
            return false;
        }
        return true;
    }

}
