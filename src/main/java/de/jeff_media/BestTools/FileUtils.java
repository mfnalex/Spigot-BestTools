package de.jeff_media.BestTools;

import org.bukkit.Material;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {
    Main main;

    FileUtils(Main main) {
        this.main=main;
    }

    void dumpFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        boolean debug = main.getConfig().getBoolean("debug");
        main.getConfig().set("debug",false);
        for(Material mat: Material.values()) {
            if(!mat.isBlock()) continue;
            printWriter.printf("%s,%s\n",mat.name(),main.toolHandler.getBestToolType(mat).name());
        }
        main.getConfig().set("debug",debug);

        printWriter.close();
    }

    public static void renameFileInPluginDir(Main plugin,String oldName, String newName) {
        File oldFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + oldName);
        File newFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + newName);
        oldFile.getAbsoluteFile().renameTo(newFile.getAbsoluteFile());
    }


}
