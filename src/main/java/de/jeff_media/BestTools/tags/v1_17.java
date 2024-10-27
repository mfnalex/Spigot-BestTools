package de.jeff_media.BestTools.tags;

import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.*;

public class v1_17 {

    public static Collection<Material> getPickaxeMaterials() {
        //org.bukkit.Tag
        List<Tag<Material>> tags = Arrays.asList(
                Tag.STONE_BRICKS, Tag.STONE_PRESSURE_PLATES,
                Tag.BASE_STONE_NETHER, Tag.BASE_STONE_OVERWORLD,
                Tag.REDSTONE_ORES, Tag.COAL_ORES, Tag.COPPER_ORES,
                Tag.DIAMOND_ORES, Tag.EMERALD_ORES, Tag.GOLD_ORES,
                Tag.IRON_ORES, Tag.LAPIS_ORES
        );

        Set<Material> list = new HashSet<>();

        for(Tag<Material> tag : tags) {
            for(Material mat : Material.values()) {
                if(tag.isTagged(mat)) {
                    list.add(mat);
                }
            }
        }

        for(Material mat : Material.values()) {
            if(mat.name().toLowerCase(Locale.ROOT).contains("cobbled")) {
                list.add(mat);
            } else if(mat.name().toLowerCase(Locale.ROOT).contains("copper")) {
                list.add(mat);
            }
        }

        list.addAll(Arrays.asList(
                Material.RAW_IRON_BLOCK,
                Material.RAW_COPPER_BLOCK,
                Material.RAW_GOLD_BLOCK));

        return list;


    }

}
