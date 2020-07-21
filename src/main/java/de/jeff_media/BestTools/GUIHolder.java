package de.jeff_media.BestTools;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class GUIHolder implements InventoryHolder {

    Inventory inv;

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    void setInventory(Inventory inv) {
        this.inv=inv;
    }
}
