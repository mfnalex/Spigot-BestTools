package de.jeff_media.BestTools;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;

public class PlayerSetting {

         // BestTool enabled for this player?
        boolean bestToolsEnabled;

        // Automatic refill enabled for this player?
        boolean refillEnabled;

        // Use only tools from the hotbar?
        boolean hotbarOnly;

        Inventory guiInventory = null;

        // Did we already show the message how to activate sorting?
        boolean hasSeenMessage = false;

        // Do we have to save these settings?
        boolean changed = false;

        PlayerSetting(File file,Main main) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                this.bestToolsEnabled = yaml.getBoolean("bestToolsEnabled");
                this.hasSeenMessage = yaml.getBoolean("hasSeenMessage");
                this.refillEnabled = yaml.getBoolean("refillEnabled",false);
                this.hotbarOnly = yaml.getBoolean("hotbarOnly",true);
                main.debug("Loaded player setting from file "+file.getPath());
        }

        PlayerSetting(boolean bestToolsEnabled, boolean refillEnabled, boolean hasSeenMessage, boolean hotbarOnly) {
                this.bestToolsEnabled = bestToolsEnabled;
                this.refillEnabled = refillEnabled;
                this.hasSeenMessage = hasSeenMessage;
                this.hotbarOnly = hotbarOnly;
                this.changed = true;
        }

        boolean toggleBestToolsEnabled() {
                bestToolsEnabled =!bestToolsEnabled;
                changed = true;
                return bestToolsEnabled;
        }

        boolean toggleRefillEnabled() {
                refillEnabled=!refillEnabled;
                changed = true;
                return refillEnabled;
        }

        boolean toggleHotbarOnly() {
                hotbarOnly=!hotbarOnly;
                changed = true;
                return hotbarOnly;
        }

        void setHasSeenMessage(boolean seen) {
                hasSeenMessage = seen;
                changed = true;
        }

        void save(File file,Main main) {
                main.debug("Saving player setting to file "+file.getPath());
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.set("bestToolsEnabled", bestToolsEnabled);
                yaml.set("hotbarOnly",hotbarOnly);
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
