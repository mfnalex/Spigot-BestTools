package de.jeff_media.BestTool;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This will probably be a separate plugin called BestTool or something
 */
public class BestToolHandler {

    Main main;
    BestToolUtils bestToolUtils;
    boolean debug = false;
    boolean verbose = true;

    static int hotbarSize = 9;
    static int inventorySize = 36;

    // Configurable Start //
    boolean hotbarOnly = false;
    boolean preventItemBreak = false; // Will not use Items that would break on this use
    static int favoriteSlot = hotbarSize-1;
    // Configurable End //

    public HashMap<Material,Tool> toolMap = new HashMap<>();
    ArrayList<Tag> usedTags = new ArrayList<>();
    final Material[] pickaxes = {
            Material.NETHERITE_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.IRON_PICKAXE,
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE};
    final Material[] axes = {
            Material.NETHERITE_AXE,
            Material.DIAMOND_AXE,
            Material.IRON_AXE,
            Material.STONE_AXE,
            Material.WOODEN_AXE};
    final Material[] shovels = {
            Material.NETHERITE_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.IRON_SHOVEL,
            Material.STONE_SHOVEL,
            Material.WOODEN_SHOVEL};

    final Material[] hoes = {
            Material.NETHERITE_HOE,
            Material.DIAMOND_HOE,
            Material.IRON_HOE,
            Material.STONE_HOE,
            Material.WOODEN_HOE};

    BestToolHandler(Main main) {
        this.main=Objects.requireNonNull(main,"Main must not be null");
        bestToolUtils = new BestToolUtils(main);
        bestToolUtils.initMap();
    }

    enum Tool {
        PICKAXE,
        SHOVEL,
        SHEARS,
        AXE,
        HOE,
        NONE
    }

    /**
     * Returns the durability
     * @param item
     * @return Durability left, or -1 if not damageable
     */
    private int getDurability(@Nullable ItemStack item) {
        if(item==null) return -1;
        if(!(item.getItemMeta() instanceof Damageable)) {
            return -1;
        }
        Damageable damageable = (Damageable) item.getItemMeta();
        return item.getType().getMaxDurability() - damageable.getDamage();
    }

    /**
     * Gets the best tool type for a material
     * @param mat The block's material
     * @return Best tool type for that material
     */
    @NotNull
    Tool getBestToolType(@NotNull Material mat) {
        Tool bestTool = toolMap.get(Objects.requireNonNull(mat,"Material must not be null"));
        if(bestTool == null) bestTool = Tool.NONE;
        //System.out.println("Best ToolType for "+mat+" is "+bestTool.name());
        return bestTool;
    }

    /**
     * Searches through and array and returns the ItemStack that matches this material
     * @param mat Material to look for
     * @param items Player's items (whole inventory or hotbar)
     * @return Matching ItemStack
     */
    @Nullable
    ItemStack getItemStackFromArray(@NotNull Material mat, @NotNull ItemStack[] items) {
        for(ItemStack item : Objects.requireNonNull(items,"Items must not be null")) {
            if(item==null) continue;
            if(item.getType()==Objects.requireNonNull(mat,"Material must not be null")) {
                if(getDurability(item)!=1) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Searches the player's inventory for the best matching tool and returns its ItemStack
     * @param type Tool type
     * @param items Player's items (whole inventory or hotbar)
     * @return
     */
    @Nullable
    ItemStack typeToItem(Tool type, ItemStack[] items) {

        switch(Objects.requireNonNull(type,"Tool must not be null")) {

            case PICKAXE:
                for(Material pickaxe : pickaxes) {
                    ItemStack itemStack = getItemStackFromArray(pickaxe, items);
                    if(itemStack != null) return itemStack;
                }
                return null;

            case AXE:
                for(Material axe : axes) {
                    ItemStack itemStack = getItemStackFromArray(axe, items);
                    if(itemStack != null) return itemStack;
                }
                return null;

            case SHOVEL:
                for(Material shovel : shovels) {
                    ItemStack itemStack = getItemStackFromArray(shovel, items);
                    if(itemStack != null) return itemStack;
                }
                //System.out.println("typeToItem -> shovel -> null");
                return null;

            case HOE:
                for(Material hoe : hoes) {
                    ItemStack itemStack = getItemStackFromArray(hoe, items);
                    if(itemStack != null) return itemStack;
                }
                return null;

            case SHEARS:
                return getItemStackFromArray(Material.SHEARS, items);

            default:
                return null;
        }
    }

    /**
     * Tries to get the ItemStack that is the best for this block
     * @param mat The block's material
     * @param inv Player's inventory
     * @return
     */
    @Nullable
    ItemStack getBestToolFromInventory(@NotNull Material mat, @NotNull PlayerInventory inv) {
        ItemStack[] hotbar = new ItemStack[(hotbarOnly ? hotbarSize : inventorySize)];
        Tool bestType = getBestToolType(Objects.requireNonNull(mat,"Material must not be null"));
        for(int i = 0; i < (hotbarOnly ? hotbarSize : inventorySize); i++) {
            hotbar[i] = Objects.requireNonNull(inv,"Inventory must not be null").getItem(i);
        }
        return typeToItem(Objects.requireNonNull(bestType,"Tool must not be null"),hotbar);
        //if(debug == null) System.out.println("debug == null");
    }


    /**
     * Gets the slot number of a given ItemStack
     * @param item ItemStack that we need the slot number of
     * @param inv Player's inventory
     * @return
     */
    @NotNull
    int getPositionInInventory(@NotNull ItemStack item, @NotNull PlayerInventory inv) {
        for(int i = 0; i < Objects.requireNonNull(inv,"Inventory must not be null").getSize(); i++) {
            ItemStack currentItem = inv.getItem(i);
            if(currentItem==null) continue;
            if(currentItem.equals(Objects.requireNonNull(item,"Item must not be null"))) {
                //System.out.println(String.format("Found perfect tool %s at slot %d",currentItem.getType().name(),i));
                return i;
            }
        }
        return 0;
    }

    /**
     * Moves a tool to the given slot
     * @param source Slot where the tool is
     * @param dest Slot where the tool should be
     * @param inv Player's inventory
     */
    void moveToolToSlot(@NotNull int source, @NotNull int dest, @NotNull PlayerInventory inv) {
        //System.out.println(String.format("Moving item from slot %d to %d",source,dest));
        Objects.requireNonNull(inv,"Inventory must not be null")
                .setHeldItemSlot(Objects.requireNonNull(dest,"Destination must not be null"));
        if(Objects.requireNonNull(source,"Source must not be null")==dest) return;
        ItemStack sourceItem = inv.getItem(source);
        ItemStack destItem = inv.getItem(dest);
        if(source < hotbarSize) {
            inv.setHeldItemSlot(source);
            return;
        }
        if(destItem == null) {
            inv.setItem(dest,sourceItem);
            inv.setItem(source,null);
        } else {
            inv.setItem(source, destItem);
            inv.setItem(dest, sourceItem);
        }
    }

    /**
     * Tries to free the slot if it is occupied with a damageable item
     * @param source Slot to free
     * @param inv Player's inventory
     */
    void freeSlot(@NotNull int source, @NotNull PlayerInventory inv) {
        //System.out.println(String.format("Trying to free slot %d",source));
        ItemStack item = Objects.requireNonNull(inv,"Inventory must not be null")
                .getItem(Objects.requireNonNull(source,"Source must not be null"));

        // If current slot is empty, we don't have to change it
        if(item == null) return;

        // If the item is not damageable, we don't have to move it
        ItemMeta meta = item.getItemMeta();
        if(!(meta instanceof Damageable)) return;

        // Try to combine the item with existing stacks
        inv.setItem(source, null);
        inv.addItem(item);

        // If the item was moved to the same slot, we have to move it somewhere else
        if(inv.getItem(source)==null) return;
        for(int i = source; i < inventorySize; i++) {
            if(inv.getItem(i)==null) {
                inv.setItem(i,item);
                inv.setItem(source,null);
                return;
            }
        }
        // TODO: If all of that didn't work, change to some block that is not damageable
    }

}
