package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ConfigUpdater {

    final Main main;

    public ConfigUpdater(Main main) {
        this.main = main;
    }

    // Admins hate config updates. Just relax and let BestTools update to the newest
    // config version. Don't worry! Your changes will be kept

    void updateConfig() {

        try {
            Files.deleteIfExists(new File(main.getDataFolder().getAbsolutePath() + File.separator + "config.old.yml").toPath());
        } catch (IOException ignored) {

        }

        if (main.debug)
            main.getLogger().info("rename config.yml -> config.old.yml");
        FileUtils.renameFileInPluginDir(main,"config.yml", "config.old.yml");
        if (main.debug)
            main.getLogger().info("saving new config.yml");
        main.saveDefaultConfig();

        File oldConfigFile = new File(main.getDataFolder().getAbsolutePath() + File.separator + "config.old.yml");
        FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldConfigFile);

        try {
            oldConfig.load(oldConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        Map<String, Object> oldValues = oldConfig.getValues(false);

        // Read default config to keep comments
        ArrayList<String> linesInDefaultConfig = new ArrayList<>();
        try {

            Scanner scanner = new Scanner(
                    new File(main.getDataFolder().getAbsolutePath() + File.separator + "config.yml"), "UTF-8");
            while (scanner.hasNextLine()) {
                linesInDefaultConfig.add(scanner.nextLine() + "");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<String> newLines = new ArrayList<>();
        for (String line : linesInDefaultConfig) {
            String newline = line;
            if (line.startsWith("config-version:")) {

            }

            /*else if (line.startsWith("-")) {
                continue;
            } else if (line.startsWith(" ")) {
                continue;
            }*/

            else if (line.startsWith("global-block-blacklist:")) {
                newline = null;
                newLines.add("global-block-blacklist:");
                if (main.toolHandler != null && main.toolHandler.globalBlacklist != null) {
                    for (Material disabledBlock : main.toolHandler.globalBlacklist) {
                        newLines.add("- " + disabledBlock.name());
                    }
                }
            }

            else {
                for (String node : oldValues.keySet()) {
                    if (line.startsWith(node + ":")) {

                        String quotes = "";

                        if (node.startsWith("message-")) // needs double quotes
                            quotes = "\"";

                        if (node.startsWith("gui-"))
                            quotes = "\"";

                        newline = node + ": " + quotes + oldValues.get(node).toString() + quotes;
                        if (main.debug)
                            main.getLogger().info("Updating config node " + newline);
                        break;
                    }
                }
            }
            if (newline != null) {
                newLines.add(newline);
            } else {
                main.getLogger().warning("newline == null");
            }
        }

        BufferedWriter fw;
        String[] linesArray = newLines.toArray(new String[linesInDefaultConfig.size()]);
        try {
            fw = Files.newBufferedWriter(new File(main.getDataFolder().getAbsolutePath(), "config.yml").toPath(), StandardCharsets.UTF_8);
            for (String s : linesArray) {
                fw.write(s + "\n");
            }
            fw.close();
        } catch (IOException e) {
            main.getLogger().warning("Error while updating config file!");
            e.printStackTrace();
        }

    }

}
