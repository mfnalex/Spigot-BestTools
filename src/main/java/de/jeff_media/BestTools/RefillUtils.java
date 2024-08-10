package de.jeff_media.BestTools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RefillUtils {

    final static int inventorySize = 36;

    final Main main;

    RefillUtils(Main main) {
        this.main=main;
    }

    boolean isBowlOrBottle(Material mat) {
        return (mat == Material.GLASS_BOTTLE || mat == Material.BOWL);
    }

    boolean moveBowlsAndBottles(Inventory inv, int slot) {
        if(!isBowlOrBottle(inv.getItem(slot).getType())) return false;
        ItemStack toBeMoved = inv.getItem(slot);
        inv.clear(slot);
        HashMap<Integer, ItemStack> leftovers = inv.addItem(toBeMoved);
        if(inv.getItem(slot)==null || inv.getItem(slot).getAmount()==0 || inv.getItem(slot).getType() == Material.AIR) {
            return true;
        }
        if(leftovers.size()>0) {
            main.debug("Possible item loss detected due to RefillUtils#moveBowlsAndBottles, dropping leftover items...");
            for(ItemStack leftover : leftovers.values()) {
                if(!(inv.getHolder() instanceof Player)) {
                    main.debug("Could not drop items because inventory has no player as holder :(");
                    return false;
                }
                Player p = (Player) inv.getHolder();
                p.getWorld().dropItem(p.getLocation(),leftover);
            }
            return false;
        }
        for(int i = 35;i>=0;i--) {
            inv.clear(slot);
            if(inv.getItem(i)==null || inv.getItem(i).getAmount()==0 || inv.getItem(i).getType()==Material.AIR) {
                inv.setItem(i,toBeMoved);
                return true;
            }
        }
        return false;
    }

    void refillStack(Inventory inv, int source, int dest, ItemStack stack) {
        Bukkit.getScheduler().runTask(main, () -> {
            if(inv.getItem(source)==null) return;
            if(!inv.getItem(source).equals(stack)) {
                main.debug("Refill failed, because source ItemStack has changed. Aborting Refill to prevent item loss.");
                return;
            }
            if(inv.getItem(dest)!=null && !moveBowlsAndBottles(inv,dest)) {
                main.debug("Refill failed, because destination slot is not empty anymore. Aborting Refill to prevent item loss.");
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
        @SuppressWarnings("unchecked")
        Entry<Integer,Integer>[] entries = entrySet.toArray(new Entry[0]);

        return entries[entries.length-1].getKey();

    }
}
