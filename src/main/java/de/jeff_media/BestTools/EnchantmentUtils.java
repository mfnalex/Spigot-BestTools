package de.jeff_media.BestTools;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentUtils {

    static int getMultiplier(ItemStack item) {
        int base = getBaseMultiplier(item);
        if(!item.hasItemMeta()) return base;
        ItemMeta meta = item.getItemMeta();
        Enchantment efficiency = EnchantmentUtils.getEnchantment("efficiency");
        if(!meta.hasEnchant(efficiency)) return base;
        int efficiencyLevel = meta.getEnchantLevel(efficiency);
        return base + (efficiencyLevel * efficiencyLevel) + 1;
    }

    static int getBaseMultiplier(ItemStack item) {
        String n = item.getType().name();
        // Ordered by what I think is used more often for performance
        if(n.startsWith("DIAMOND")) return 8;
        if(n.startsWith("IRON")) return 6;
        if(n.startsWith("NETHERITE")) return 9;
        if(n.startsWith("STONE")) return 4;
        if(n.startsWith("WOOD")) return 2;
        if(n.startsWith("GOLD")) return 12;
        return 1;
    }

    static Enchantment getEnchantment(String enchantmentKey) {
        return Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantmentKey));
    }
}
