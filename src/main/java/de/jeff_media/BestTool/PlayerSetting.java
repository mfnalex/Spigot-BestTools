package de.jeff_media.BestTool;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;

public class PlayerSetting {

         // BestTool enabled for this player?
        boolean bestToolEnabled;

        // Automatic refill enabled for this player?
        boolean refillEnabled;

        Inventory guiInventory = null;

        // Did we already show the message how to activate sorting?
        boolean hasSeenMessage = false;

        // Do we have to save these settings?
        boolean changed = false;

        PlayerSetting(File file,Main main) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                this.bestToolEnabled = yaml.getBoolean("bestToolEnabled");
                this.hasSeenMessage = yaml.getBoolean("hasSeenMessage");
                this.refillEnabled = yaml.getBoolean("refillEnabled",false);
                main.debug("Loaded player setting from file "+file.getPath());
        }

        PlayerSetting(boolean bestToolEnabled, boolean refillEnabled, boolean hasSeenMessage) {
                this.bestToolEnabled = bestToolEnabled;
                this.refillEnabled = refillEnabled;
                this.hasSeenMessage = hasSeenMessage;
                this.changed = true;
        }

        boolean toggleBestToolEnabled() {
                bestToolEnabled=!bestToolEnabled;
                changed = true;
                return bestToolEnabled;
        }

        boolean toggleRefillEnabled() {
                refillEnabled=!refillEnabled;
                changed = true;
                return refillEnabled;
        }

        void setHasSeenMessage(boolean seen) {
                hasSeenMessage = seen;
                changed = true;
        }

        void save(File file,Main main) {
                main.debug("Saving player setting to file "+file.getPath());
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.set("bestToolEnabled",bestToolEnabled);
                yaml.set("refillEnabled",refillEnabled);
                yaml.set("hasSeenMessage",hasSeenMessage);
                try {
                        yaml.save(file);
                } catch (IOException e) {
                        main.getLogger().warning("Error while saving playerdata file "+file.getName());
                        //e.printStackTrace();
                }
        }

}
