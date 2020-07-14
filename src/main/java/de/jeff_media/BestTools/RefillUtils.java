package de.jeff_media.BestTools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class RefillUtils {

    final static int inventorySize = 36;

    Main main;

    RefillUtils(Main main) {
        this.main=main;
    }

    void refillStack(Inventory inv, int source, int dest, ItemStack stack) {
        Bukkit.getScheduler().runTask(main, () -> {
            if(inv.getItem(source)==null) return;
            if(!inv.getItem(source).equals(stack)) {
                main.getLogger().warning("Refill failed, because source ItemStack has changed. Aborting Refill to prevent item loss.");
                return;
            }
            if(inv.getItem(dest)!=null) {
                main.getLogger().warning("Refill failed, because destination slot is not empty anymore. Aborting Refill to prevent item loss.");
                return;
            }
            inv.setItem(source, null);
            inv.setItem(dest, stack);
        });
    }

    static int getMatchingStackPosition(PlayerInventory inv, Material mat, int currentSlot) {

        HashMap<Integer,Integer> slots = new HashMap<>();

        for(int i = 0; i < inventorySize; i++) {
            if(i==currentSlot) continue;
            ItemStack item = inv.getItem(i);
            if(item==null) continue;

            if(item.getType()!=mat) continue;

            if(item.getAmount()==64) return i;

            slots.put(i,item.getAmount());
        }

        if(slots.size()==0) return -1;

        Map<Integer,Integer> sortedSlots = MapUtils.sortByValue(slots);
        Set<Entry<Integer,Integer>> entrySet = sortedSlots.entrySet();
        Entry<Integer,Integer>[] entries = entrySet.toArray(new Entry[0]);

        return entries[entries.length-1].getKey();

    }
}
