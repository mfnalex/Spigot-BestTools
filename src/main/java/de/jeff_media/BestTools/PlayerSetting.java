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
                //System.out.println("New PlayerSetting player, file");
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

                getPDCValues(player);
        }

        private void getPDCValues(Player player) {
                //System.out.println("getPDCValues player");
                if(player.getPersistentDataContainer().has(DATA, DataType.FILE_CONFIGURATION)) {
                        //System.out.println("Player has saved data, loading...");
                        FileConfiguration conf = player.getPersistentDataContainer().get(DATA, DataType.FILE_CONFIGURATION);
                        this.bestToolsEnabled = conf.getBoolean("bestToolsEnabled");
                        //System.out.println("Enabled: " + bestToolsEnabled);
                        this.hasSeenBestToolsMessage = conf.getBoolean("hasSeenBestToolsMessage");
                        this.hasSeenRefillMessage = conf.getBoolean("hasSeenRefillMessage");
                        this.refillEnabled = conf.getBoolean("refillEnabled");
                        this.hotbarOnly = conf.getBoolean("hotbarOnly");
                        //this.swordOnMobs = conf.getBoolean("swordOnMobs");
                        //this.favoriteSlot = conf.getInt("favoriteSlot");
                }
        }

        private static <T,Z> Z getPdc(Player player, NamespacedKey key, PersistentDataType<T,Z> type, Z defaultValue) {
                return player.getPersistentDataContainer().getOrDefault(key,type,defaultValue);
        }

        private void save() {
                //System.out.println("Saving to PDC...");
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
                this.favoriteSlot = favoriteSlot;
                getPDCValues(player);
                this.save();
        }

        Blacklist getBlacklist() {
                return blacklist;
        }

        boolean toggleBestToolsEnabled() {
                bestToolsEnabled =!bestToolsEnabled;
                save();
                return bestToolsEnabled;
        }

        boolean toggleRefillEnabled() {
                refillEnabled=!refillEnabled;
                save();
                return refillEnabled;
        }

        boolean toggleHotbarOnly() {
                hotbarOnly=!hotbarOnly;
                save();
                return hotbarOnly;
        }

        void setHasSeenBestToolsMessage(boolean seen) {
                if(seen== hasSeenBestToolsMessage) return;
                hasSeenBestToolsMessage = seen;
                save();
        }

        void setHasSeenRefillMessage(boolean seen) {
                if(seen== hasSeenRefillMessage) return;
                hasSeenRefillMessage = seen;
                save();
        }

        void setFavoriteSlot(int favoriteSlot) {
                this.favoriteSlot = favoriteSlot;
                save();
        }

}
