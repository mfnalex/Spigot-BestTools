package de.jeff_media.BestTool;

import de.jeff_media.PluginUpdateChecker.PluginUpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin {

    PluginUpdateChecker updateChecker;
    BestToolHandler toolHandler;
    BestToolUtils toolUtils;
    BlockPlaceListener blockPlaceListener;
    PlayerInteractListener playerInteractListener;
    PlayerListener playerListener;
    FileUtils fileUtils;
    RefillUtils refillUtils;
    CommandBestTool commandBestTool;
    CommandRefill commandRefill;
    Messages messages;

    HashMap<UUID,PlayerSetting> playerSettings;
    boolean verbose = true;

    @Override
    public void onEnable() {
        load(false);
    }

    @Override
    public void onDisable() {
        saveAllPlayerSettings();
    }

    void debug(String text) {
        if(getConfig().getBoolean("debug")) getLogger().info("[Debug] "+text);
    }

    PlayerSetting getPlayerSetting(Player player) {

        if(Objects.requireNonNull(playerSettings,"PlayerSettings must not be null").containsKey(player.getUniqueId())) {
            return playerSettings.get(player.getUniqueId());
        }

        PlayerSetting setting;

        // TODO: Load PlayerSetting from file
        File file = getPlayerDataFile(player.getUniqueId());
        if(file.exists()) {
            debug("Loading player setting for "+player.getName()+" from file");
            setting = new PlayerSetting(file,this);
        } else {
            debug("Creating new player setting for "+player.getName());
            setting = new PlayerSetting(
                    getConfig().getBoolean("besttool-enabled-by-default"),
                    getConfig().getBoolean("refill-enabled-by-default"),
                    false);
        }
        playerSettings.put(player.getUniqueId(),setting);
        return setting;
    }

    void saveAllPlayerSettings() {
        for(Map.Entry<UUID,PlayerSetting> entry : playerSettings.entrySet()) {
            if(entry.getValue().changed) {
                entry.getValue().save(getPlayerDataFile(entry.getKey()),this);
            }
        }
    }

    File getPlayerDataFile(UUID uuid) {
        return new File(getDataFolder()+File.separator+"playerdata"+File.separator+uuid.toString()+".yml");
    }

    void load(boolean reload) {

        getDataFolder().mkdir();
        saveDefaultConfig();
        File playerdataFolder = new File(getDataFolder()+ File.separator+"playerdata");
        playerdataFolder.mkdir();

        if(reload) {
            if(updateChecker!=null) {
                updateChecker.stop();
            }
            reloadConfig();
        }

        loadDefaultValues();

        updateChecker = new PluginUpdateChecker(this,"","","","");
        toolHandler = new BestToolHandler(this);
        toolUtils = new BestToolUtils(this);
        blockPlaceListener = new BlockPlaceListener(this);
        playerInteractListener  = new PlayerInteractListener(this);
        playerListener = new PlayerListener(this);
        commandBestTool = new CommandBestTool(this);
        commandRefill = new CommandRefill(this);
        refillUtils = new RefillUtils((this));
        messages = new Messages(this);
        fileUtils = new FileUtils(this);
        playerSettings = new HashMap<UUID,PlayerSetting>();

        toolUtils.initMap();

        getServer().getPluginManager().registerEvents(blockPlaceListener,this);
        getServer().getPluginManager().registerEvents(playerInteractListener,this);
        getServer().getPluginManager().registerEvents(playerListener, this);
        getCommand("besttool").setExecutor(commandBestTool);
        getCommand("refill").setExecutor(commandRefill);
        // TODO: Start update Checker

        if(getConfig().getBoolean("dump",false)) {
            try {
                fileUtils.dumpFile(new File(getDataFolder()+File.separator+"dump.csv"));
            } catch (IOException e) {
                getLogger().warning("Could not create dump.csv");
            }
        }

    }

    private void loadDefaultValues() {
        getConfig().addDefault("use-gold-tools",false);
        getConfig().addDefault("besttool-enabled-by-default",false);
        getConfig().addDefault("refill-enabled-by-default",false);
        getConfig().addDefault("hotbar-only", true);
        getConfig().addDefault("prevent-item-break",true);

        verbose = getConfig().getBoolean("verbose",true);

    }
}
