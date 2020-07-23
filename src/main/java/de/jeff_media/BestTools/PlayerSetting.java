package de.jeff_media.BestTools;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;

public class PlayerSetting {

        Blacklist blacklist;

         // BestTool enabled for this player?
        public boolean bestToolsEnabled;

        // Automatic refill enabled for this player?
        public boolean refillEnabled;

        // Use only tools from the hotbar?
        public boolean hotbarOnly;

        public int favoriteSlot = 0;

        boolean swordOnMobs;

        boolean hasSeenBestToolsMessage = false;
        boolean hasSeenRefillMessage = false;

        // Do we have to save these settings?
        boolean changed = false;

        final BestToolsCache btcache = new BestToolsCache();

        boolean invChanged = true;

        PlayerSetting(File file,Main main) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                blacklist = new Blacklist(yaml.getStringList("blacklist"));
                this.bestToolsEnabled = yaml.getBoolean("bestToolsEnabled",false);
                this.hasSeenBestToolsMessage = yaml.getBoolean("hasSeenBestToolsMessage",false);
                this.hasSeenRefillMessage = yaml.getBoolean("hasSeenRefillMessage",false);
                this.refillEnabled = yaml.getBoolean("refillEnabled",false);
                this.hotbarOnly = yaml.getBoolean("hotbarOnly",true);
                this.swordOnMobs = yaml.getBoolean("swordOnMobs",true);
                this.favoriteSlot = yaml.getInt("favoriteSlot",main.getConfig().getInt("favorite-slot"));
                main.debug("Loaded player setting from file "+file.getPath());
        }

        PlayerSetting(boolean bestToolsEnabled, boolean refillEnabled, boolean hotbarOnly, int favoriteSlot, boolean swordOnMobs) {
                this.blacklist = new Blacklist();
                this.bestToolsEnabled = bestToolsEnabled;
                this.refillEnabled = refillEnabled;
                this.hasSeenBestToolsMessage = false;
                this.hasSeenRefillMessage = false;
                this.hotbarOnly = hotbarOnly;
                this.swordOnMobs=swordOnMobs;
                this.changed = true;
                this.favoriteSlot = favoriteSlot;
        }

        Blacklist getBlacklist() {
                return blacklist;
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

        void setFavoriteSlot(int favoriteSlot) {
                this.favoriteSlot = favoriteSlot;
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
                yaml.set("favoriteSlot",favoriteSlot);
                yaml.set("swordOnMobs",swordOnMobs);
                yaml.set("blacklist",blacklist.toStringList());
                try {
                        yaml.save(file);
                } catch (IOException e) {
                        main.getLogger().warning("Error while saving playerdata file "+file.getName());
                        //e.printStackTrace();
                }
        }

}
