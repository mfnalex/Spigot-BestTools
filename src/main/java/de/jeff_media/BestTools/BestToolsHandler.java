package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This will probably be a separate plugin called BestTool or something
 */
public class BestToolsHandler {

    final Main main;
    boolean debug = false;
    boolean verbose = true;

    static final int hotbarSize = 9;
    static final int inventorySize = 36;

    // Configurable Start //
    boolean preventItemBreak = false; // Will not use Items that would break on this use
    //int favoriteSlot;
    // Configurable End //

    final HashMap<Material,Tool> toolMap = new HashMap<>();
    final HashSet<Material> globalBlacklist = new HashSet<>();
    ArrayList<Tag<Material>> usedTags = new ArrayList<>();

    // TODO: Cache valid tool materials here
    final ArrayList<Material> pickaxes = new ArrayList<>();
    final ArrayList<Material> axes = new ArrayList<>();
    final ArrayList<Material> hoes = new ArrayList<>();
    final ArrayList<Material> shovels = new ArrayList<>();
    final ArrayList<Material> swords = new ArrayList<>();

    final ArrayList<Material> allTools = new ArrayList<>();
    final ArrayList<Material> instaBreakableByHand = new ArrayList<>();

    final EnumSet<Material> leaves = EnumSet.noneOf(Material.class);


    final ArrayList<Material> weapons = new ArrayList<>();

    BestToolsHandler(Main main) {

        this.main=Objects.requireNonNull(main,"Main must not be null");
        //this.getFavoriteSlot()=main.getConfig().getInt("favorite-slot");

        for(String name : main.getConfig().getStringList("global-block-blacklist")) {
            Material mat = Material.valueOf(name.toUpperCase());
            if(mat==null) {
                main.getLogger().warning("Invalid material on global-block-blacklist: "+name);
                continue;
            }
            main.debug("Adding to global block blacklist: " + mat.name());
            globalBlacklist.add(mat);
        }

        Arrays.stream(Material.values()).forEach(material -> {
            if(material.name().endsWith("_LEAVES")) {
                leaves.add(material);
            }
        });


    }

    boolean isWeapon(ItemStack itemInMainHand) {
        for(Material mat : weapons) {
            if(itemInMainHand.getType()==mat) return true;
        }
        return false;
    }

    boolean isTool(ItemStack i) {
        return allTools.contains(i.getType());
    }
    boolean isToolOrRoscoe(ItemStack i) {
        return allTools.contains(i.getType()) || swords.contains(i.getType());
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
        // TODO: Delete? Its unused
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
        switch(mat) {
            case GLOWSTONE:
            case ENDER_CHEST:
            case QUARTZ:
            case SPAWNER:
            case SEA_LANTERN:
                return true;
        }
        if(name.equals("NETHER_GOLD_ORE")) return true; // Fortune also improves this, but according to wiki even fortune 3 on avg only gives 8.8 nuggets which is less than 1 ingot
        if(name.contains("GLASS")) return true;
        return false;
    }


    boolean isTool(Tool tool, ItemStack item) {
        Material m = item.getType();
        switch(tool) {
            case PICKAXE:
                return pickaxes.contains(m);
            case AXE:
                return axes.contains(m);
            case SHOVEL:
                return shovels.contains(m);
            case HOE:
                return hoes.contains(m);
            case SHEARS:
                return item.getType() == Material.SHEARS;
            case NONE:
                return !isDamageable(item);
            case SWORD:
                return swords.contains(m);
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
    ItemStack getNonToolItemFromArray(@NotNull ItemStack[] items,ItemStack currentItem, Material target) {

        // Note: InstaBreaks dont cause damage except on hoes
        // TODO: Take this into account: https://minecraft.gamepedia.com/Item_durability
        // TODO: itemMeta instanceof Damageable may also mean the tool is unused!
        if(instaBreakableByHand.contains(target) && !hoes.contains(currentItem.getType()) ||
            !isToolOrRoscoe(currentItem))
            return currentItem;

        for(ItemStack item: items) {
            if(item==null || !isDamageable(item)) {
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
    ItemStack getBestItemStackFromArray(@NotNull Tool tool, @NotNull ItemStack[] items, boolean trySilktouch, ItemStack currentItem, Material target) {

        if(tool == Tool.NONE) {
            main.debug("getNonToolItemFromArray");
            return getNonToolItemFromArray(items,currentItem,target);
        }

        List<ItemStack> list = new ArrayList<>();
        for(ItemStack item : items) {
             if(item==null) continue; // IntelliJ says this is always false
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
                return getBestItemStackFromArray(tool,items,false,currentItem,target);
            } else {
                return null;
            }
        }
        list.sort(Comparator.comparingInt(EnchantmentUtils::getMultiplier).reversed());
        if(target.name().endsWith("DIAMOND_ORE")) {
            list = putIronPlusBeforeGoldPickaxes(list);
        }
        return list.get(0);
    }

    private List<ItemStack> putIronPlusBeforeGoldPickaxes(List<ItemStack> list) {
        if(list == null || list.isEmpty()) return list;
        if(main.toolHandler.isTool(Tool.PICKAXE,list.get(0))) {
            List<ItemStack> newList = list.stream().filter(itemStack -> {
                switch (itemStack.getType()) {
                    case WOODEN_PICKAXE:
                    case STONE_PICKAXE:
                    case GOLDEN_PICKAXE:
                        return false;
                    default: return true;
                }
            }).collect(Collectors.toList());
            if(!newList.isEmpty()) return newList;
        }
        return list;
    }

    @Nullable
    ItemStack getBestRoscoeFromArray(@NotNull ItemStack[] items, ItemStack currentItem, EntityType enemy, boolean useAxe) {

        ArrayList<ItemStack> list = new ArrayList<>();
        for(ItemStack item : items) {
            if(item==null) continue; // IntelliJ says this is always false
            // TODO: Check if durability is 1

            if(isRoscoe(item,useAxe)) {
                list.add(item);
            }
        }
        if(list.size()==0) {
            return null;
        }
        list.sort((o1, o2) -> SwordUtils.getDamage(o1,enemy) < SwordUtils.getDamage(o2,enemy) ? 1 : -1);
        return list.get(0);
    }

    // Roscoes are only axes and swords, weapons are roscoes + bow, crossbow, etc
    private boolean isRoscoe(ItemStack item, boolean useAxe) {
         return useAxe ?
                 swords.contains(item.getType())
                         || axes.contains(item.getType())
                 : swords.contains(item.getType());
    }


    ItemStack[] inventoryToArray(Player p,boolean hotbarOnly) {
        PlayerInventory inv = p.getInventory();
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
    ItemStack getBestToolFromInventory(@NotNull Material mat, Player p, boolean hotbarOnly,ItemStack currentItem) {
        ItemStack[] items = inventoryToArray(p,hotbarOnly);

        Tool bestType;
        if(!LeavesUtils.isLeaves(mat) && mat != Material.COBWEB) {
            bestType=getBestToolType(mat);
        } else {
            if(LeavesUtils.hasShears(hotbarOnly, p.getInventory().getStorageContents())) {
                bestType = Tool.SHEARS;
            }  else if(LeavesUtils.hasHoe(hotbarOnly,p.getInventory().getStorageContents()) && mat != Material.COBWEB) {
                bestType = Tool.HOE;
            } else if(((main.getConfig().getBoolean("consider-swords-for-cobwebs") && mat == Material.COBWEB)||(mat != Material.COBWEB && main.getConfig().getBoolean("consider-swords-for-leaves"))) && LeavesUtils.hasSword(hotbarOnly, p.getInventory().getStorageContents())) {
                bestType = Tool.SWORD;
            } else {
                bestType = Tool.NONE;
            }
        }
        ItemStack bestStack = getBestItemStackFromArray(bestType,items,profitsFromSilkTouch(mat),currentItem,mat);
        if(bestStack==null) {
            main.debug("bestStack is null");
            return getNonToolItemFromArray(items,currentItem,mat);
        }
        main.debug("bestStack is "+bestStack.toString());
        return bestStack;

    }

    /**
     * Tries to get the roscoe that is the best for this block
     * @param p Player
     * @return
     */
    @Nullable
    ItemStack getBestRoscoeFromInventory(@NotNull EntityType enemy, Player p, boolean hotbarOnly, ItemStack currentItem, boolean useAxe) {
        ItemStack[] items = inventoryToArray(p,hotbarOnly);

        ItemStack bestRoscoe = getBestRoscoeFromArray(items,currentItem,enemy,useAxe);
        //if(bestRoscoe==null) {
        //    bestRoscoe = getNonToolItemFromArray(items,currentItem,mat);
        //}
        return bestRoscoe;

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
    void moveToolToSlot(int source, int dest, @NotNull PlayerInventory inv) {
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
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if( meta instanceof Damageable) {
            main.debug(item.getType().name() + " is damageable");
            return true;
        } else {
            main.debug(item.getType().name() + " is NOT damageable");
            return false;
        }
    }

    /**
     * Tries to free the slot if it is occupied with a damageable item
     * @param source Slot to free
     * @param inv Player's inventory
     */
    void freeSlot(int source, @NotNull PlayerInventory inv) {

        if(inv.getItemInMainHand()==null) return; // IntelliJ says this is always false

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
