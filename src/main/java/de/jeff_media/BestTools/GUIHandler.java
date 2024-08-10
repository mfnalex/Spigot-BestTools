package de.jeff_media.BestTools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GUIHandler implements Listener {

    Main main;
    final int[] BESTTOOLS_SLOT = new int[] {2,2};
    final int[] REFILL_SLOT = new int[] {2,8};
    final int[] HOTBAR_SLOT = new int[] {4,2};

    GUIHandler(Main main) {
        this.main = main;
    }

    /**
     * Coordinates to slot number
     * @param row rownumber where 1 is at the top and 6 is at the bottom
     * @param col colnumber where 1 is left and 9 is right
     * @return slot number
     */
    private int coords2slot(int row, int col) {
        if (col > 9 || col < 1 || row > 6 || row < 1) throw new IllegalArgumentException();
        return (9 * (row - 1)) + col - 1;
    }

    private ItemStack createGUIItem(@NotNull Material mat, @Nullable String name, @Nullable String[] lores) {
        String lore = "";
        if(lores!=null && lores.length>0) for(String line : lores) {
            lore = lore + line + "\n";
        }
        if(lore.length()>2) {lore = lore.substring(0,lore.length()-2);}
        else { lore=null; }
        return createGUIItem(mat,name,lore);
    }


    private ItemStack createGUIItem(@NotNull Material mat, @Nullable String name, @Nullable String lore) {
        String[] lores = lore == null ? null : lore.split("\\r?\\n");
        ItemStack i = new ItemStack(mat);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) meta.setLore(Arrays.asList(lores));
        i.setItemMeta(meta);
        return i;
    }

    private void addItem(Inventory gui, int row, int col, Material mat, String name, String[] lore) {
        //String[] lores = lore.split("\\r?\\n");
        gui.setItem(coords2slot(row, col), createGUIItem(mat, name, lore));
    }

    private void addItem(Inventory gui, int row, int col, Material mat, String name, String lore) {
        String[] lores = lore == null ? null : lore.split("\\r?\\n");
        gui.setItem(coords2slot(row, col), createGUIItem(mat, name, lores));
    }

    private boolean isGUIInventory(@Nullable Inventory inv) {
        return (inv != null && inv.getHolder() != null && inv.getHolder() instanceof GUIHolder);
    }

    private void frame(Inventory gui) {
        for (int col = 1; col <= 9; col++) {
            for (int row = 1; row <= 5; row += 4) {
                addItem(gui, row, col, Material.BLACK_STAINED_GLASS_PANE, row+","+col+"="+ coords2slot(row,col), (String) null);
            }
        }
        for (int row = 2; row <= 5; row++) {
            for (int col = 1; col <= 9; col += 8) {
                addItem(gui, row, col, Material.BLACK_STAINED_GLASS_PANE, row+","+col+"="+ coords2slot(row,col), (String) null);
            }
        }
    }

    /*@EventHandler
    public void onChestSortEvent(ChestSortEvent e) {
        if (isGUIInventory(e.getInventory())) e.setCancelled(true);
    }*/

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if(!isGUIInventory(e.getClickedInventory())) return;
        e.setCancelled(true);
        if(!e.getClick().isLeftClick()) return;
        if(!(e.getWhoClicked()instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        PlayerSetting ps = main.getPlayerSetting(p);

        //System.out.println(e.getSlot());

        // New Favorite slot
        if(e.getSlot()>=45&&e.getSlot()<=53) {
            ps.setFavoriteSlot(e.getSlot()-45);
        }

        // Toggle BestTools
        if(e.getSlot()==coords2slot(BESTTOOLS_SLOT[0],BESTTOOLS_SLOT[1])) {
            ps.toggleBestToolsEnabled();
        }

        // Toggle REFILL_SLOT
        if(e.getSlot()==coords2slot(REFILL_SLOT[0],REFILL_SLOT[1])) {
            ps.toggleRefillEnabled();
        }

        // Toggle BestTools hotbar
        if(e.getSlot()==coords2slot(HOTBAR_SLOT[0],HOTBAR_SLOT[1])) {
            ps.toggleHotbarOnly();
        }

        open(p);
    }

    private String getEnabledString(boolean b) {
        if(b) return ChatColor.GREEN+"Enabled";
        return ChatColor.RED+"Disabled";
    }

    private void addBestToolsButton(PlayerSetting ps, Inventory gui) {
        int slot = coords2slot(BESTTOOLS_SLOT[0],BESTTOOLS_SLOT[1]);
        Material mat = ps.isBestToolsEnabled() ? Material.GOLDEN_PICKAXE : Material.WOODEN_PICKAXE;
        ItemStack is = createGUIItem(mat,String.format("BestTools: %s",getEnabledString(ps.isBestToolsEnabled())),main.messages.GUI_BESTTOOLS_LORE);
        if(ps.isBestToolsEnabled()) {
            ItemMeta meta = is.getItemMeta();
            meta.addEnchant(Enchantment.EFFICIENCY,5,false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(meta);
        }
        gui.setItem(slot,is);

        slot = coords2slot(HOTBAR_SLOT[0],HOTBAR_SLOT[1]);
        mat = ps.isHotbarOnly() ? main.getConfig().getBoolean("puns") ? Material.BLAZE_ROD : Material.CHEST : Material.ENDER_CHEST;
        is = createGUIItem(mat,String.format("Hotbar only: %s",getEnabledString(ps.isHotbarOnly())), main.messages.GUI_HOTBAR_LORE);
        gui.setItem(slot,is);

    }

    private void addRefillButton(PlayerSetting ps, Inventory gui) {
        Material mat = ps.isRefillEnabled() ? Material.MILK_BUCKET : Material.BUCKET;
        addItem(gui,2,8,mat,String.format("Refill: %s",getEnabledString(ps.isRefillEnabled())),main.messages.GUI_REFILL_LORE);
    }

    Inventory create(Player p) {
        Inventory gui = Bukkit.createInventory(new GUIHolder(), 54, ChatColor.GOLD + "[BestTools]");
        ((GUIHolder) gui.getHolder()).setInventory(gui);
        frame(gui);
        PlayerSetting ps = main.getPlayerSetting(p);
        for (int i = 1; i <= 9; i++) {
            addItem(gui, 6, i,
                    i == ps.getFavoriteSlot()+1 ? Material.GREEN_STAINED_GLASS_PANE : Material.WHITE_STAINED_GLASS_PANE,
                    main.messages.GUI_FAVORITE_SLOT, main.messages.GUI_FAVORITE_SLOT_LORE);
        }
        addBestToolsButton(ps,gui);
        addRefillButton(ps,gui);


        return gui;
    }

    void open(Player p) {
        Inventory inv = create(p);
        p.closeInventory();
        Bukkit.getScheduler().runTask(main,() -> {
           p.openInventory(inv);
        });
    }


}
