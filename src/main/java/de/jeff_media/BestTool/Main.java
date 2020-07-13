package de.jeff_media.BestTool;

import de.jeff_media.PluginUpdateChecker.PluginUpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin {

    PluginUpdateChecker updateChecker;
    BestToolHandler toolHandler;
    BestToolUtils toolUtils;
    PlayerInteractListener playerInteractListener;
    Messages messages;

    HashMap<UUID,PlayerSetting> playerSettings;
    boolean verbose = true;

    @Override
    public void onEnable() {
        load(false);
    }

    PlayerSetting getPlayerSetting(Player player) {

        System.out.println("Getting player setting");

        if(Objects.requireNonNull(playerSettings,"PlayerSettings must not be null").containsKey(player.getUniqueId())) {
            return playerSettings.get(player.getUniqueId());
        }

        // TODO: Load PlayerSetting from file

        PlayerSetting setting = new PlayerSetting(getConfig().getBoolean("enabled-by-default"),false);
        playerSettings.put(player.getUniqueId(),setting);
        return setting;
    }

    void load(boolean reload) {
        if(reload) {
            if(updateChecker!=null) {
                updateChecker.stop();
            }
            reloadConfig();
        }

        loadDefaultValues();

        updateChecker = new PluginUpdateChecker(this,"","","","");
        toolHandler = new BestToolHandler(this);
        playerInteractListener  = new PlayerInteractListener(this);
        messages = new Messages(this);

        getServer().getPluginManager().registerEvents(playerInteractListener,this);
        // TODO: Start update Checker


    }

    private void loadDefaultValues() {
        getConfig().addDefault("use-gold-tools",false);
        getConfig().addDefault("enabled-by-default",false);
        getConfig().addDefault("hotbar-only", true);
        getConfig().addDefault("prevent-item-break",true);

        verbose = getConfig().getBoolean("verbose",true);

    }
}
