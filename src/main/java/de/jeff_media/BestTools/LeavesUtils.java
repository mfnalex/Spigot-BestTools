package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LeavesUtils {

    public static boolean hasShears(boolean hotbarOnly, ItemStack[] inventory) {
        int maxSlot = hotbarOnly ? 9 : inventory.length;
        for(int i = 0; i < maxSlot; i++) {
            if(inventory[i] == null) continue;
            if(inventory[i].getType() == Material.SHEARS) return true;
        }
        return false;
    }

    public static boolean hasSword(boolean hotbarOnly, ItemStack[] inventory) {
        int maxSlot = hotbarOnly ? 9 : inventory.length;
        for(int i = 0; i < maxSlot; i++) {
            if(inventory[i] == null) continue;
            if(inventory[i].getType().name().endsWith("_SWORD")) return true;
        }
        return false;
    }

    public static boolean hasHoe(boolean hotbarOnly, ItemStack[] inventory) {
        int maxSlot = hotbarOnly ? 9 : inventory.length;
        for(int i = 0; i < maxSlot; i++) {
            if(inventory[i] == null) continue;
            if(inventory[i].getType().name().endsWith("_HOE")) return true;
        }
        return false;
    }

    public static boolean isLeaves(Material mat) {
        return mat.name().endsWith("_LEAVES");
    }

}
