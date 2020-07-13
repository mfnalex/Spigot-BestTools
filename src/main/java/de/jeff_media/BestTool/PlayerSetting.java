package de.jeff_media.BestTool;

import org.bukkit.inventory.Inventory;

public class PlayerSetting {

         // BestTool enabled for this player?
        boolean bestToolEnabled;

        Inventory guiInventory = null;

        // Did we already show the message how to activate sorting?
        boolean hasSeenMessage = false;

        // Do we have to save these settings?
        boolean changed;

        PlayerSetting(boolean bestToolEnabled, boolean hasSeenMessage) {
                this.bestToolEnabled = bestToolEnabled;
                this.hasSeenMessage = hasSeenMessage;
                this.changed = false;
        }

        boolean toggleBestToolEnabled() {
                bestToolEnabled=!bestToolEnabled;
                changed = true;
                return bestToolEnabled;
        }

}
