package de.jeff_media.BestTools;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;

public class PlayerSetting {

        private static final Main main = Main.getInstance();
        private static final NamespacedKey DATA = new NamespacedKey(main, "data");

        @Getter private Blacklist blacklist;

        @Getter
        private boolean bestToolsEnabled;

        @Getter private boolean refillEnabled;

        @Getter private boolean hotbarOnly;

        private int favoriteSlot = 0;

        @Getter private boolean swordOnMobs;

        @Getter private boolean hasSeenBestToolsMessage = false;
        @Getter private boolean hasSeenRefillMessage = false;

        // Do we have to save these settings?
        boolean changed = false;

        @Getter private final BestToolsCache btcache = new BestToolsCache();

        private final Player player;

        public int getFavoriteSlot() {
                if(favoriteSlot >= 0 && favoriteSlot <= 8) return favoriteSlot;
                return player.getInventory().getHeldItemSlot();
        }

        PlayerSetting(Player player, File file) {
                this.player = player;
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

        private static <T,Z> Z getPdc(Player player, NamespacedKey key, PersistentDataType<T,Z> type, Z defaultValue) {
                return player.getPersistentDataContainer().getOrDefault(key,type,defaultValue);
        }

        private void save() {
                FileConfiguration conf = new YamlConfiguration();
                conf.set("blacklist",blacklist.toStringList());
                conf.set("bestToolsEnabled",bestToolsEnabled);
                conf.set("hasSeenBestToolsMessage",hasSeenBestToolsMessage);
                conf.set("hasSeenRefillMessage",hasSeenRefillMessage);
                conf.set("refillEnabled",refillEnabled);
                conf.set("hotbarOnly",hotbarOnly);
                //conf.set("swordOnMobs",swordOnMobs);
                //conf.set("favoriteSlot",main.getConfig().getInt("favorite-slot"));
                player.getPersistentDataContainer().set(DATA,DataType.FILE_CONFIGURATION,conf);
        }

        PlayerSetting(Player player, boolean bestToolsEnabled, boolean refillEnabled, boolean hotbarOnly, int favoriteSlot, boolean swordOnMobs) {
                this.player = player;
                this.blacklist = new Blacklist();
                this.bestToolsEnabled = bestToolsEnabled;
                this.refillEnabled = refillEnabled;
                this.hasSeenBestToolsMessage = false;
                this.hasSeenRefillMessage = false;
                this.hotbarOnly = hotbarOnly;
                this.swordOnMobs= swordOnMobs;
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

        void save(Player player) {
                //main.debug("Saving player setting to file "+file.getPath());
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.set("bestToolsEnabled", bestToolsEnabled);
                yaml.set("hotbarOnly",hotbarOnly);
                yaml.set("refillEnabled",refillEnabled);
                yaml.set("hasSeenBestToolsMessage", hasSeenBestToolsMessage);
                yaml.set("hasSeenRefillMessage", hasSeenRefillMessage);
                yaml.set("favoriteSlot",favoriteSlot);
                yaml.set("swordOnMobs",swordOnMobs);
                yaml.set("blacklist",blacklist.toStringList());
                player.getPersistentDataContainer().set(DATA, DataType.FILE_CONFIGURATION,yaml);
                /*try {
                        yaml.save(file);
                } catch (IOException e) {
                        main.getLogger().warning("Error while saving playerdata file "+file.getName());
                        //e.printStackTrace();
                }*/
        }

}
