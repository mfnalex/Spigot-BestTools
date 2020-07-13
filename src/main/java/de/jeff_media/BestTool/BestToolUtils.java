package de.jeff_media.BestTool;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import de.jeff_media.BestTool.BestToolHandler.Tool;

import java.util.HashMap;
import java.util.Objects;

public class BestToolUtils {

    Main main;

    public BestToolUtils(@NotNull Main main) {

        this.main = Objects.requireNonNull(main,"Main must not be null");
        //this.handler = Objects.requireNonNull(main.toolHandler,"BestToolHandler must not be null");
    }

    private void tagToMap(@NotNull Tag<Material> tag, @NotNull Tool tool) {
        tagToMap(Objects.requireNonNull(tag,"Tag must not be null"),
                Objects.requireNonNull(tool,"Tool must not be null"),
                null);
    }

    private void tagToMap(@NotNull Tag<Material> tag, @NotNull Tool tool, @Nullable String match) {
        Objects.requireNonNull(tag,"Tag must not be null");
        Objects.requireNonNull(tool,"Tool must not be null");
        for(Material mat : tag.getValues()) {
            if(match==null) {
                addToMap(mat,tool);
            } else {
                if (mat.name().contains(match)) {
                    addToMap(mat,tool);
                }
            }
        }
        //usedTags.add(tag);
    }

    private void printMap(HashMap<Material, Tool> toolMap) {
        toolMap.forEach((mat, tool) -> System.out.println(String.format("%0$30s -> %s", mat.name(), tool.name())));
    }

    private void addToMap(@NotNull Material mat, @NotNull Tool tool) {
        Objects.requireNonNull(Objects.requireNonNull(main.toolHandler,"ToolHandler must not be null").
                toolMap,"ToolMap must not be null")
                .put(Objects.requireNonNull(mat,"Material must not be null"),
                Objects.requireNonNull(tool,"Tool must not be null"));
    }

    void initMap() {
        long startTime = System.nanoTime();

        initFallbackMaterials();

        tagToMap(Tag.ANVIL, Tool.PICKAXE);
        tagToMap(Tag.BAMBOO_PLANTABLE_ON, Tool.SHOVEL);
        tagToMap(Tag.BEEHIVES, Tool.AXE);
        tagToMap(Tag.CRIMSON_STEMS, Tool.AXE);
        tagToMap(Tag.CROPS, Tool.NONE);
        tagToMap(Tag.FENCES, Tool.AXE);
        tagToMap(Tag.FENCE_GATES, Tool.AXE);
        tagToMap(Tag.FLOWERS, Tool.NONE);
        tagToMap(Tag.ICE, Tool.PICKAXE);
        tagToMap(Tag.LEAVES, Tool.SHEARS);
        tagToMap(Tag.LOGS, Tool.AXE);
        tagToMap(Tag.PLANKS, Tool.AXE);
        tagToMap(Tag.RAILS, Tool.PICKAXE);
        tagToMap(Tag.SIGNS, Tool.AXE);
        tagToMap(Tag.WALLS, Tool.PICKAXE);
        tagToMap(Tag.WOOL, Tool.SHEARS);

        // WATCH OUT FOR ORDER - START //
        tagToMap(Tag.BUTTONS, Tool.AXE);
        tagToMap(Tag.BUTTONS, Tool.PICKAXE,"STONE");

        tagToMap(Tag.DOORS, Tool.AXE);
        tagToMap(Tag.DOORS, Tool.PICKAXE,"IRON");

        tagToMap(Tag.PRESSURE_PLATES, Tool.PICKAXE);
        tagToMap(Tag.WOODEN_PRESSURE_PLATES, Tool.AXE);

        tagToMap(Tag.TRAPDOORS, Tool.AXE);
        tagToMap(Tag.TRAPDOORS, Tool.PICKAXE,"IRON");

        tagToMap(Tag.SLABS,Tool.PICKAXE);
        tagToMap(Tag.WOODEN_SLABS,Tool.AXE);

        // WATCH OUT FOR ORDER - END //

        tagToMap(Tag.SAND, Tool.SHOVEL);
        tagToMap(Tag.SHULKER_BOXES, Tool.PICKAXE);
        tagToMap(Tag.STONE_BRICKS, Tool.PICKAXE);

        // Some of the following definitions are redundant because of the tags above
        // However I don't want to miss something, so they are still defined here
        // Shouldn't harm because building the map takes only take 2 ms when the
        // plugin is enabled

        // Issue #1
        addToMap(Material.BASALT,Tool.PICKAXE);
        addToMap(Material.POLISHED_BASALT,Tool.PICKAXE);
        addToMap(Material.GLOWSTONE,Tool.PICKAXE); // TODO: Prefer SilkTouch
        addToMap(Material.NETHER_GOLD_ORE,Tool.PICKAXE);
        // Issue #1 End

        // Issue #2
        tagToMap(Tag.NYLIUM,Tool.PICKAXE);
        tagToMap(Tag.STAIRS,Tool.PICKAXE);
        tagToMap(Tag.WOODEN_STAIRS,Tool.PICKAXE);
        addToMap(Material.SPONGE,Tool.HOE);
        addToMap(Material.WET_SPONGE,Tool.HOE);
        addToMap(Material.PISTON,Tool.PICKAXE);
        addToMap(Material.STICKY_PISTON,Tool.PICKAXE);
        addToMap(Material.PISTON_HEAD,Tool.PICKAXE);
        addToMap(Material.MOVING_PISTON,Tool.PICKAXE);
        addToMap(Material.CHORUS_PLANT,Tool.AXE);
        addToMap(Material.CHORUS_FLOWER,Tool.AXE);
        addToMap(Material.CARVED_PUMPKIN,Tool.AXE);
        addToMap(Material.HAY_BLOCK,Tool.HOE);
        addToMap(Material.OBSERVER,Tool.PICKAXE);
        addToMap(Material.NETHER_WART_BLOCK,Tool.HOE);
        addToMap(Material.WARPED_WART_BLOCK,Tool.HOE);
        addToMap(Material.MAGMA_BLOCK,Tool.PICKAXE);
        // Issue #2 End



        addToMap(Material.ACACIA_BUTTON, Tool.AXE);
        addToMap(Material.ACACIA_FENCE, Tool.AXE);
        addToMap(Material.ACACIA_FENCE_GATE, Tool.AXE);
        addToMap(Material.ACACIA_LEAVES, Tool.SHEARS);
        addToMap(Material.ACACIA_PRESSURE_PLATE, Tool.AXE);
        addToMap(Material.ACACIA_SLAB, Tool.AXE);
        addToMap(Material.ACACIA_STAIRS, Tool.AXE);
        addToMap(Material.ANCIENT_DEBRIS, Tool.PICKAXE);
        addToMap(Material.ANDESITE, Tool.PICKAXE);
        addToMap(Material.BAMBOO, Tool.AXE);
        addToMap(Material.BAMBOO_SAPLING, Tool.AXE);
        addToMap(Material.BASALT, Tool.PICKAXE);
        addToMap(Material.BIRCH_BUTTON, Tool.AXE);
        addToMap(Material.BIRCH_FENCE, Tool.AXE);
        addToMap(Material.BIRCH_FENCE_GATE, Tool.AXE);
        addToMap(Material.BIRCH_LEAVES, Tool.SHEARS);
        addToMap(Material.BIRCH_PRESSURE_PLATE, Tool.AXE);
        addToMap(Material.BIRCH_SLAB, Tool.AXE);
        addToMap(Material.BIRCH_STAIRS, Tool.AXE);
        addToMap(Material.BLACKSTONE, Tool.PICKAXE);
        addToMap(Material.BLACKSTONE_SLAB, Tool.PICKAXE);
        addToMap(Material.BLACKSTONE_STAIRS, Tool.PICKAXE);
        addToMap(Material.BLACK_CONCRETE, Tool.PICKAXE);
        addToMap(Material.BLACK_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.BLUE_CONCRETE, Tool.PICKAXE);
        addToMap(Material.BLUE_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.BOOKSHELF, Tool.AXE);
        addToMap(Material.BREWING_STAND, Tool.PICKAXE);
        addToMap(Material.BRICKS, Tool.PICKAXE);
        addToMap(Material.BRICK_SLAB, Tool.PICKAXE);
        addToMap(Material.BRICK_STAIRS, Tool.PICKAXE);
        addToMap(Material.BROWN_CONCRETE, Tool.PICKAXE);
        addToMap(Material.BROWN_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.BROWN_MUSHROOM_BLOCK, Tool.AXE);
        addToMap(Material.CAMPFIRE, Tool.AXE);
        addToMap(Material.CAULDRON, Tool.PICKAXE);
        addToMap(Material.CHAIN, Tool.PICKAXE);
        addToMap(Material.CHEST, Tool.AXE);
        addToMap(Material.CHISELED_RED_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.CHISELED_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.CHISELED_STONE_BRICKS, Tool.PICKAXE);
        addToMap(Material.CLAY, Tool.SHOVEL);
        addToMap(Material.COAL_BLOCK, Tool.PICKAXE);
        addToMap(Material.COAL_ORE, Tool.PICKAXE);
        addToMap(Material.COARSE_DIRT, Tool.SHOVEL);
        addToMap(Material.COBBLESTONE, Tool.PICKAXE);
        addToMap(Material.COBBLESTONE_SLAB, Tool.PICKAXE);
        addToMap(Material.COBBLESTONE_STAIRS, Tool.PICKAXE);
        addToMap(Material.COBWEB, Tool.SHEARS);
        addToMap(Material.COCOA, Tool.AXE);
        addToMap(Material.CRACKED_STONE_BRICKS, Tool.PICKAXE);
        addToMap(Material.CRAFTING_TABLE, Tool.AXE);
        addToMap(Material.CRYING_OBSIDIAN, Tool.PICKAXE);
        addToMap(Material.CUT_RED_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.CUT_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.CYAN_CONCRETE, Tool.PICKAXE);
        addToMap(Material.CYAN_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.DARK_OAK_BUTTON, Tool.AXE);
        addToMap(Material.DARK_OAK_FENCE, Tool.AXE);
        addToMap(Material.DARK_OAK_FENCE_GATE, Tool.AXE);
        addToMap(Material.DARK_OAK_LEAVES, Tool.SHEARS);
        addToMap(Material.DARK_OAK_PRESSURE_PLATE, Tool.AXE);
        addToMap(Material.DARK_OAK_SLAB, Tool.AXE);
        addToMap(Material.DARK_OAK_STAIRS, Tool.AXE);
        addToMap(Material.DARK_PRISMARINE, Tool.PICKAXE);
        addToMap(Material.DARK_PRISMARINE_SLAB, Tool.PICKAXE);
        addToMap(Material.DARK_PRISMARINE_STAIRS, Tool.PICKAXE);
        addToMap(Material.DAYLIGHT_DETECTOR, Tool.AXE);
        addToMap(Material.DIAMOND_BLOCK, Tool.PICKAXE);
        addToMap(Material.DIAMOND_ORE, Tool.PICKAXE);
        addToMap(Material.DIORITE, Tool.PICKAXE);
        addToMap(Material.DIRT, Tool.SHOVEL);
        addToMap(Material.DISPENSER, Tool.PICKAXE);
        addToMap(Material.DROPPER, Tool.PICKAXE);
        addToMap(Material.EMERALD_BLOCK, Tool.PICKAXE);
        addToMap(Material.EMERALD_ORE, Tool.PICKAXE);
        addToMap(Material.ENCHANTING_TABLE, Tool.PICKAXE);
        addToMap(Material.ENDER_CHEST, Tool.PICKAXE);
        addToMap(Material.END_STONE, Tool.PICKAXE);
        addToMap(Material.FARMLAND, Tool.SHOVEL);
        addToMap(Material.FURNACE, Tool.PICKAXE);
        addToMap(Material.GILDED_BLACKSTONE, Tool.PICKAXE);
        addToMap(Material.GOLD_BLOCK, Tool.PICKAXE);
        addToMap(Material.GOLD_ORE, Tool.PICKAXE);
        addToMap(Material.GRANITE, Tool.PICKAXE);
        addToMap(Material.GRASS_BLOCK, Tool.SHOVEL);
        addToMap(Material.GRASS_PATH, Tool.SHOVEL);
        addToMap(Material.GRAVEL, Tool.SHOVEL);
        addToMap(Material.GRAY_CONCRETE, Tool.PICKAXE);
        addToMap(Material.GRAY_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.GREEN_CONCRETE, Tool.PICKAXE);
        addToMap(Material.GREEN_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Tool.PICKAXE);
        addToMap(Material.HOPPER, Tool.PICKAXE);
        addToMap(Material.IRON_BARS, Tool.PICKAXE);
        addToMap(Material.IRON_BLOCK, Tool.PICKAXE);
        addToMap(Material.IRON_DOOR, Tool.PICKAXE);
        addToMap(Material.IRON_ORE, Tool.PICKAXE);
        addToMap(Material.IRON_TRAPDOOR, Tool.PICKAXE);
        addToMap(Material.JACK_O_LANTERN, Tool.AXE);
        addToMap(Material.JUKEBOX, Tool.AXE);
        addToMap(Material.JUNGLE_BUTTON, Tool.AXE);
        addToMap(Material.JUNGLE_FENCE, Tool.AXE);
        addToMap(Material.JUNGLE_FENCE_GATE, Tool.AXE);
        addToMap(Material.JUNGLE_LEAVES, Tool.SHEARS);
        addToMap(Material.JUNGLE_PRESSURE_PLATE, Tool.AXE);
        addToMap(Material.JUNGLE_SLAB, Tool.AXE);
        addToMap(Material.JUNGLE_STAIRS, Tool.AXE);
        addToMap(Material.LADDER, Tool.AXE);
        addToMap(Material.LANTERN, Tool.PICKAXE);
        addToMap(Material.LAPIS_BLOCK, Tool.PICKAXE);
        addToMap(Material.LAPIS_ORE, Tool.PICKAXE);
        addToMap(Material.LIGHT_BLUE_CONCRETE, Tool.PICKAXE);
        addToMap(Material.LIGHT_BLUE_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.LIGHT_GRAY_CONCRETE, Tool.PICKAXE);
        addToMap(Material.LIGHT_GRAY_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Tool.PICKAXE);
        addToMap(Material.LIME_CONCRETE, Tool.PICKAXE);
        addToMap(Material.LIME_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.LODESTONE, Tool.PICKAXE);
        addToMap(Material.MAGENTA_CONCRETE, Tool.PICKAXE);
        addToMap(Material.MAGENTA_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.MELON, Tool.AXE);
        addToMap(Material.MOSSY_COBBLESTONE, Tool.PICKAXE);
        addToMap(Material.MOSSY_STONE_BRICKS, Tool.PICKAXE);
        addToMap(Material.MUSHROOM_STEM, Tool.AXE);
        addToMap(Material.MYCELIUM, Tool.SHOVEL);
        addToMap(Material.NETHERITE_BLOCK, Tool.PICKAXE);
        addToMap(Material.NETHERRACK, Tool.PICKAXE);
        addToMap(Material.NETHER_BRICK, Tool.PICKAXE);
        addToMap(Material.NETHER_BRICK_FENCE, Tool.PICKAXE);
        addToMap(Material.NETHER_BRICK_SLAB, Tool.PICKAXE);
        addToMap(Material.NETHER_BRICK_STAIRS, Tool.PICKAXE);
        addToMap(Material.NETHER_QUARTZ_ORE, Tool.PICKAXE);
        addToMap(Material.NOTE_BLOCK, Tool.AXE);
        addToMap(Material.OAK_BUTTON, Tool.AXE);
        addToMap(Material.OAK_FENCE, Tool.AXE);
        addToMap(Material.OAK_FENCE_GATE, Tool.AXE);
        addToMap(Material.OAK_LEAVES, Tool.SHEARS);
        addToMap(Material.OAK_PRESSURE_PLATE, Tool.AXE);
        addToMap(Material.OAK_SLAB, Tool.AXE);
        addToMap(Material.OAK_STAIRS, Tool.AXE);
        addToMap(Material.OBSIDIAN, Tool.PICKAXE);
        addToMap(Material.ORANGE_CONCRETE, Tool.PICKAXE);
        addToMap(Material.ORANGE_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.PINK_CONCRETE, Tool.PICKAXE);
        addToMap(Material.PINK_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.PODZOL, Tool.SHOVEL);
        addToMap(Material.POLISHED_ANDESITE, Tool.PICKAXE);
        addToMap(Material.POLISHED_DIORITE, Tool.PICKAXE);
        addToMap(Material.POLISHED_GRANITE, Tool.PICKAXE);
        addToMap(Material.PUMPKIN, Tool.AXE);
        addToMap(Material.PURPLE_CONCRETE, Tool.PICKAXE);
        addToMap(Material.PURPLE_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.QUARTZ_BLOCK, Tool.PICKAXE);
        addToMap(Material.QUARTZ_BRICKS, Tool.PICKAXE);
        addToMap(Material.REDSTONE_BLOCK, Tool.PICKAXE);
        addToMap(Material.REDSTONE_ORE, Tool.PICKAXE);
        addToMap(Material.RED_CONCRETE, Tool.PICKAXE);
        addToMap(Material.RED_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.RED_MUSHROOM_BLOCK, Tool.AXE);
        addToMap(Material.RED_NETHER_BRICKS, Tool.PICKAXE);
        addToMap(Material.RED_NETHER_BRICK_SLAB, Tool.PICKAXE);
        addToMap(Material.RED_NETHER_BRICK_STAIRS, Tool.PICKAXE);
        addToMap(Material.RED_SAND, Tool.SHOVEL);
        addToMap(Material.RED_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.RED_SANDSTONE_SLAB, Tool.PICKAXE);
        addToMap(Material.RED_SANDSTONE_STAIRS, Tool.PICKAXE);
        addToMap(Material.RESPAWN_ANCHOR, Tool.PICKAXE);
        addToMap(Material.SAND, Tool.SHOVEL);
        addToMap(Material.SANDSTONE, Tool.PICKAXE);
        addToMap(Material.SANDSTONE_SLAB, Tool.PICKAXE);
        addToMap(Material.SANDSTONE_STAIRS, Tool.PICKAXE);
        addToMap(Material.SHULKER_BOX, Tool.PICKAXE);
        addToMap(Material.SMOOTH_QUARTZ, Tool.PICKAXE);
        addToMap(Material.SMOOTH_RED_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.SMOOTH_SANDSTONE, Tool.PICKAXE);
        addToMap(Material.SMOOTH_STONE, Tool.PICKAXE);
        addToMap(Material.SNOW, Tool.SHOVEL);
        addToMap(Material.SNOW_BLOCK, Tool.SHOVEL);
        addToMap(Material.SOUL_CAMPFIRE, Tool.AXE);
        addToMap(Material.SOUL_LANTERN, Tool.PICKAXE);
        addToMap(Material.SOUL_SAND, Tool.SHOVEL);
        addToMap(Material.SOUL_SOIL, Tool.SHOVEL);
        addToMap(Material.SPAWNER, Tool.PICKAXE);
        addToMap(Material.SPRUCE_BUTTON, Tool.AXE);
        addToMap(Material.SPRUCE_FENCE, Tool.AXE);
        addToMap(Material.SPRUCE_FENCE_GATE, Tool.AXE);
        addToMap(Material.SPRUCE_LEAVES, Tool.SHEARS);
        addToMap(Material.SPRUCE_PRESSURE_PLATE, Tool.AXE);
        addToMap(Material.SPRUCE_STAIRS, Tool.AXE);
        addToMap(Material.STONE, Tool.PICKAXE);
        addToMap(Material.STONE_BRICKS, Tool.PICKAXE);
        addToMap(Material.STONE_BRICK_SLAB, Tool.PICKAXE);
        addToMap(Material.STONE_BRICK_STAIRS, Tool.PICKAXE);
        addToMap(Material.STONE_BUTTON, Tool.PICKAXE);
        addToMap(Material.STONE_PRESSURE_PLATE, Tool.PICKAXE);
        addToMap(Material.STONE_SLAB, Tool.PICKAXE);
        addToMap(Material.TERRACOTTA, Tool.PICKAXE);
        addToMap(Material.TRAPPED_CHEST, Tool.AXE);
        addToMap(Material.VINE, Tool.SHEARS);
        addToMap(Material.WHITE_CONCRETE, Tool.PICKAXE);
        addToMap(Material.WHITE_CONCRETE_POWDER, Tool.SHOVEL);
        addToMap(Material.YELLOW_CONCRETE, Tool.PICKAXE);
        addToMap(Material.YELLOW_CONCRETE_POWDER, Tool.SHOVEL);


        long endTime = System.nanoTime();
        //printMap();
        if(main.verbose) {
            main.getLogger().info(String.format("Building the <Block,Tool> map took %d ms",(endTime-startTime)/1000000));
        }
    }

    private void initFallbackMaterials() {

        for(Material mat : Material.values()) {

            if(!mat.isBlock()) {
                continue;
            }

            String n = mat.name();

            // Issue #1
            if(n.contains("BLACKSTONE")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("NETHER_BRICK")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            // Issue #1 End

            // Issue #2
            if(n.contains("TERRACOTTA")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("PURPUR")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("INFESTED")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("ENDSTONE_BRICK")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("QUARTZ")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("CORAL_BLOCK")) {
                addToMap(mat,Tool.PICKAXE);
                continue;
            }
            if(n.contains("PRISMARINE")) {
                addToMap(mat,Tool.PICKAXE);
            }
            // Issue #2 End

        }

    }

}
