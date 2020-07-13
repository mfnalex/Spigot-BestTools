package de.jeff_media.BestTool;

import org.bukkit.Material;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

public class FileUtils {
    Main main;

    FileUtils(Main main) {
        this.main=main;
    }

    void dumpFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for(Material mat: Material.values()) {
            printWriter.printf("%s,%s",mat.name(),main.toolHandler.getBestToolType(mat).name());
        }

        printWriter.close();
    }


}
