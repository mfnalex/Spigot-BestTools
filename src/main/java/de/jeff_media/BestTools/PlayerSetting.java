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

        boolean hasSeenBestToolsMessage = false;
        boolean hasSeenRefillMessage = false;

        // Do we have to save these settings?
        boolean changed = false;

        final BestToolsCache btcache = new BestToolsCache();

        boolean invChanged = true;

        PlayerSetting(File file,Main main) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                this.bestToolsEnabled = yaml.getBoolean("bestToolsEnabled",false);
                this.hasSeenBestToolsMessage = yaml.getBoolean("hasSeenBestToolsMessage",false);
                this.hasSeenRefillMessage = yaml.getBoolean("hasSeenRefillMessage",false);
                this.refillEnabled = yaml.getBoolean("refillEnabled",false);
                this.hotbarOnly = yaml.getBoolean("hotbarOnly",true);
                main.debug("Loaded player setting from file "+file.getPath());
        }

        PlayerSetting(boolean bestToolsEnabled, boolean refillEnabled, boolean hotbarOnly) {
                this.bestToolsEnabled = bestToolsEnabled;
                this.refillEnabled = refillEnabled;
                this.hasSeenBestToolsMessage = false;
                this.hasSeenRefillMessage = false;
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

        void setHasSeenBestToolsMessage(boolean seen) {
                if(seen== hasSeenBestToolsMessage) return;
                hasSeenBestToolsMessage = seen;
                changed = true;
        }

        void setHasSeenRefillMessage(boolean seen) {
                if(seen== hasSeenRefillMessage) return;
                hasSeenRefillMessage = seen;
                changed = true;
        }

        void save(File file,Main main) {
                main.debug("Saving player setting to file "+file.getPath());
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.set("bestToolsEnabled", bestToolsEnabled);
                yaml.set("hotbarOnly",hotbarOnly);
                yaml.set("refillEnabled",refillEnabled);
                yaml.set("hasSeenBestToolsMessage", hasSeenBestToolsMessage);
                yaml.set("hasSeenRefillMessage", hasSeenRefillMessage);
                try {
                        yaml.save(file);
                } catch (IOException e) {
                        main.getLogger().warning("Error while saving playerdata file "+file.getName());
                        //e.printStackTrace();
                }
        }

}
