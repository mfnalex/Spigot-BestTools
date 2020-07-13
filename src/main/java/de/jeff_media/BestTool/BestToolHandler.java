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
import java.util.LinkedList;
import java.util.Objects;

/**
 * This will probably be a separate plugin called BestTool or something
 */
public class BestToolHandler {

    Main main;
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
    final String[] pickaxeNames = {
            "NETHERITE_PICKAXE",
            "DIAMOND_PICKAXE",
            "IRON_PICKAXE",
            "STONE_PICKAXE",
            "WOODEN_PICKAXE"};
    final String[] axeNames = {
            "NETHERITE_AXE",
            "DIAMOND_AXE",
            "IRON_AXE",
            "STONE_AXE",
            "WOODEN_AXE"};
    final String[] shovelNames = {
            "NETHERITE_SHOVEL",
            "DIAMOND_SHOVEL",
            "IRON_SHOVEL",
            "STONE_SHOVEL",
            "WOODEN_SHOVEL"};

    final String[] hoeNames = {
            "NETHERITE_HOE",
            "DIAMOND_HOE",
            "IRON_HOE",
            "STONE_HOE",
            "Material.WOODEN_HOE"};

    final LinkedList<Material> pickaxes = new LinkedList<>();
    final LinkedList<Material> axes = new LinkedList<>();
    final LinkedList<Material> hoes = new LinkedList<>();
    final LinkedList<Material> shovels = new LinkedList<>();

    BestToolHandler(Main main) {
        this.main=Objects.requireNonNull(main,"Main must not be null");

        for(String s : pickaxeNames) {
            if(Material.getMaterial(s)!=null) {
                pickaxes.add(Material.getMaterial(s));
            }
        }
        for(String s : axeNames) {
            if(Material.getMaterial(s)!=null) {
                axes.add(Material.getMaterial(s));
            }
        }
        for(String s : shovelNames) {
            if(Material.getMaterial(s)!=null) {
                shovels.add(Material.getMaterial(s));
            }
        }
        for(String s : hoeNames) {
            if(Material.getMaterial(s)!=null) {
                hoes.add(Material.getMaterial(s));
            }
        }
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
        main.debug("Best ToolType for "+mat+" is "+bestTool.name());
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
                main.debug("typeToItem -> shovel -> null");
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
    }


    /**
     * Gets the slot number of a given ItemStack
     * @param item ItemStack that we need the slot number of
     * @param inv Player's inventory
     * @return slot number or -1 if not found
     */
    @NotNull
    int getPositionInInventory(@NotNull ItemStack item, @NotNull PlayerInventory inv) {
        for(int i = 0; i < Objects.requireNonNull(inv,"Inventory must not be null").getSize(); i++) {
            ItemStack currentItem = inv.getItem(i);
            if(currentItem==null) continue;
            if(currentItem.equals(Objects.requireNonNull(item,"Item must not be null"))) {
                main.debug(String.format("Found perfect tool %s at slot %d",currentItem.getType().name(),i));
                return i;
            }
        }
        return -1;
    }

    /**
     * Moves a tool to the given slot
     * @param source Slot where the tool is
     * @param dest Slot where the tool should be
     * @param inv Player's inventory
     */
    void moveToolToSlot(@NotNull int source, @NotNull int dest, @NotNull PlayerInventory inv) {
        main.debug(String.format("Moving item from slot %d to %d",source,dest));
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

    boolean isDamageable(ItemStack item) {
        if(item==null) return false;
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta instanceof Damageable;
    }

    /**
     * Tries to free the slot if it is occupied with a damageable item
     * @param source Slot to free
     * @param inv Player's inventory
     */
    void freeSlot(@NotNull int source, @NotNull PlayerInventory inv) {

        if(inv.getItemInMainHand()==null) return;

        if(!isDamageable(inv.getItemInMainHand())) return;

        ItemStack item = Objects.requireNonNull(inv,"Inventory must not be null")
                .getItem(Objects.requireNonNull(source,"Source must not be null"));

        // If current slot is empty, we don't have to change it
        if(item == null) return;

        // If the item is not damageable, we don't have to move it
        if(!isDamageable(item)) return;

        main.debug(String.format("Trying to free slot %d",source));

        // Try to combine the item with existing stacks
        inv.setItem(source, null);
        inv.addItem(item);

        // If the item was moved to the same slot, we have to move it somewhere else
        if(inv.getItem(source)==null) {
            main.debug("Freed slot");
            inv.setHeldItemSlot(source);
            return;
        }
        main.debug("Could not free slot yet...");
        for(int i = source; i < inventorySize; i++) {
            if(inv.getItem(i)==null) {
                inv.setItem(i,item);
                inv.setItem(source,null);
                inv.setHeldItemSlot(source);
                main.debug("Freed slot on second try");
                return;
            }
        }

        main.debug("WARNING: COULD NOT FREE SLOT AT ALL");

        for(int i = 0; i < hotbarSize ; i++) {
            if(inv.getItem(i) == null || !isDamageable(inv.getItem(i))) {
                main.debug("Found not damageable item at slot "+i);
                inv.setHeldItemSlot(i);
            }
        }
    }

}
