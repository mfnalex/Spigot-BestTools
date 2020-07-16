package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This will probably be a separate plugin called BestTool or something
 */
public class BestToolsHandler {

    Main main;
    boolean debug = false;
    boolean verbose = true;

    static int hotbarSize = 9;
    static int inventorySize = 36;

    // Configurable Start //
    boolean preventItemBreak = false; // Will not use Items that would break on this use
    int favoriteSlot;
    // Configurable End //

    public HashMap<Material,Tool> toolMap = new HashMap<>();
    ArrayList<Tag<Material>> usedTags = new ArrayList<>();

    final LinkedList<Material> pickaxes = new LinkedList<>();
    final LinkedList<Material> axes = new LinkedList<>();
    final LinkedList<Material> hoes = new LinkedList<>();
    final LinkedList<Material> shovels = new LinkedList<>();

    BestToolsHandler(Main main) {

        this.main=Objects.requireNonNull(main,"Main must not be null");
        this.favoriteSlot=main.getConfig().getInt("favorite-slot");
    }

    enum Tool {
        PICKAXE,
        SHOVEL,
        SHEARS,
        AXE,
        HOE,
        SWORD,
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
        Tool bestTool = toolMap.get(mat);
        if(bestTool == null) bestTool = Tool.NONE;
        main.debug("Best ToolType for "+mat+" is "+bestTool.name());
        return bestTool;
    }

    // TODO: Optimize all of this by caching valid Materials instead of doing String checks everytime

    boolean profitsFromSilkTouch(Material mat) {
        String name = mat.name();
        if(mat==Material.GLOWSTONE) return true;
        if(mat==Material.ENDER_CHEST) return true;
        if(name.contains("GLASS")) return true;
        return false;
    }


    boolean isTool(Tool tool, ItemStack item) {
        String n = item.getType().name();
        switch(tool) {
            case PICKAXE:
                return n.endsWith("_PICKAXE");
            case AXE:
                return n.endsWith("_AXE");
            case SHOVEL:
                return n.endsWith("_SHOVEL");
            case HOE:
                return n.endsWith("_HOE");
            case SHEARS:
                return item.getType() == Material.SHEARS;
            default:
                // TODO: This might confuse the logic for NONE
                return false;
        }

    }

    static int getEmptyHotbarSlot(PlayerInventory inv) {
        for(int i = 0; i<hotbarSize;i++) {
            if(inv.getItem(i)==null) return i;
        }
        return -1;
    }

    @Nullable
    ItemStack getNonToolItemFromArray(@NotNull ItemStack[] items) {
        for(ItemStack item: items) {
            if(!isDamageable(item)) {
                return item;
            }
        }
        return null;

    }

    boolean hasSilktouch(ItemStack item) {
        if(item==null) return false;
        if(!item.hasItemMeta()) return false;
        return item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }

    @Nullable
    ItemStack getBestItemStackFromArray(@NotNull Tool tool, @NotNull ItemStack[] items, boolean trySilktouch) {

        if(tool == Tool.NONE) {
            return getNonToolItemFromArray(items);
        }

        ArrayList<ItemStack> list = new ArrayList<>();
        for(ItemStack item : items) {
            if(item==null) continue;
            // TODO: Check if durability is 1

            if(isTool(tool,item)) {
                if(!trySilktouch) {
                    list.add(item);
                } else {
                    if(hasSilktouch(item)) {
                        list.add(item);
                    }
                }
            }
        }
        if(list.size()==0) {
            if(trySilktouch) {
                return getBestItemStackFromArray(tool,items,false);
            } else {
                return null;
            }
        }
        list.sort(Comparator.comparingInt(EnchantmentUtils::getMultiplier).reversed());
        return list.get(0);
    }


    ItemStack[] inventoryToArray(Player p) {
        PlayerInventory inv = p.getInventory();
        boolean hotbarOnly = main.getPlayerSetting(p).hotbarOnly;
        ItemStack[] items = new ItemStack[(hotbarOnly ? hotbarSize : inventorySize)];
        for(int i = 0; i < (hotbarOnly ? hotbarSize : inventorySize); i++) {
            items[i] = inv.getItem(i);
        }
        return items;
    }

    /**
     * Tries to get the ItemStack that is the best for this block
     * @param mat The block's material
     * @param p Player
     * @return
     */
    @Nullable
    ItemStack getBestToolFromInventory(@NotNull Material mat, Player p) {
        PlayerInventory inv = p.getInventory();

        ItemStack[] items = inventoryToArray(p);

        Tool bestType = getBestToolType(mat);
        return getBestItemStackFromArray(bestType,items,profitsFromSilkTouch(mat));

    }

    /*@Nullable
    ItemStack getBestToomFromInventory(Entity e, Player p) {
        PlayerInventory inv = p.getPositionInInventory();
        ItemStack[] items = inventoryToArray(p);
    }*/


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
        inv.setHeldItemSlot(dest);
        if(source==dest) return;
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
        //if(!item.hasItemMeta()) return false;
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

        ItemStack item =inv.getItem(source);

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
