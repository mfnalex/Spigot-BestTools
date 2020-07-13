package de.jeff_media.BestTool;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PlayerSetting {

    package de.jeff_media.ChestSort;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

    public class ChestSortPlayerSetting {

        // Represents the information regarding a player
        // That includes:
        // - Does this player has sorting enabled?
        // - Did this player see the message on how to use ChestSort (message-when-using-chest in config.yml)

        // Sorting enabled for this player?
        boolean sortingEnabled;

        // Inventory sorting enabled for this player?
        boolean invSortingEnabled;

        // Hotkey settings
        boolean middleClick, shiftClick, doubleClick, shiftRightClick, leftClick, rightClick;

        Inventory guiInventory = null;

        // Did we already show the message how to activate sorting?
        boolean hasSeenMessage = false;

        // Do we have to save these settings?
        boolean changed;

        ChestSortPlayerSetting(boolean besttool) {
            this.besttool = besttool;
        }

        DoubleClickType getCurrentDoubleClick(ChestSortPlugin plugin, DoubleClickType click) {
            if(click == DoubleClickType.NONE) return DoubleClickType.NONE;
            if(currentDoubleClick == click) {
                currentDoubleClick = DoubleClickType.NONE;
                return click;
            }
            if(currentDoubleClick != click) {
                currentDoubleClick = click;
                Bukkit.getScheduler().runTaskLater(plugin, () -> currentDoubleClick = DoubleClickType.NONE, 10);
                return DoubleClickType.NONE;
            }
            return DoubleClickType.NONE;
        }



}
