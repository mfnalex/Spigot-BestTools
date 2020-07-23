package de.jeff_media.BestTools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class SwordUtils {

    static boolean isAnthropod(EntityType enemy) {
        switch(enemy) {
            case SPIDER:
            case CAVE_SPIDER:
            case BEE:
            case SILVERFISH:
            case ENDERMITE:
                return true;
            default:
                return false;
        }
    }

    static boolean isUndead(EntityType enemy) {
        switch(enemy) {
            case SKELETON:
            case STRAY:
            case WITHER_SKELETON:
            case ZOMBIE:
            case DROWNED:
            case HUSK:
            case ZOMBIFIED_PIGLIN:
            case ZOMBIE_VILLAGER:
            case PHANTOM:
            case WITHER:
            case SKELETON_HORSE:
            case ZOMBIE_HORSE:
            case ZOGLIN:
                return true;
            default:
                return false;
        }
    }

    static double getDamage(ItemStack is,EntityType enemy) {
        if(is==null) return 0;
        double base = getBaseDamage(is.getType());
        if(base==0) return 0;
        return base + getBonus(is,enemy);
    }

    static double getBaseDamage(Material mat) {
        switch (mat) {
            case IRON_AXE:
            case STONE_AXE:
            case DIAMOND_AXE:
                return 9;
            case DIAMOND_SWORD:
            case WOODEN_AXE:
            case GOLDEN_AXE:
                return 7;
            case IRON_SWORD:
                return 6;
            case STONE_SWORD:
                return 5;
            case GOLDEN_SWORD:
            case WOODEN_SWORD:
                return 4;

        }
        if(mat.name().equals("NETHERITE_SWORD")) {
            return 8;
        }
        if(mat.name().equals("NETHERITE_AXE")) {
            return 10;
        }
        return 0;
    }

    static double getBonus(ItemStack is, EntityType enemy) {
        if(!is.hasItemMeta()) return 0;
        ItemMeta im = is.getItemMeta();
        if(!im.hasEnchants()) return 0;
        double bonus = 0;
        for(Map.Entry<Enchantment,Integer> entry : im.getEnchants().entrySet()) {
            Enchantment e = entry.getKey();
            int l = entry.getValue();
            // Sharpness
            if (e.equals(Enchantment.DAMAGE_ALL)) {
                bonus += (0.5 * l + 0.5);
            }
            // Bane of Anthropods
            else if(e.equals(Enchantment.DAMAGE_ARTHROPODS)) {
                if(isAnthropod(enemy)) {
                    bonus += (2.5 * l);
                }
            }
            // Smite
            else if(e.equals(Enchantment.DAMAGE_UNDEAD)) {
                if(isUndead(enemy)) {
                    bonus += (2.5*l);
                }
            }
        }
        return bonus;
    }
}
