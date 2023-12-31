/*
 Creative TimePlay 2023

 Меню
 */
package timeplay.creativecoding.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

abstract public class Menu implements InventoryHolder {

    private final int rows;
    private String title;
    private Map<Integer,ItemStack> items;


    public Menu (int rows, String title) {

        this.rows = rows;
        this.title = title;

    }

    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        for (Map.Entry<Integer,ItemStack> item : items.entrySet()) {
            inventory.setItem(item.getKey(),item.getValue());
        }
        return inventory;
    }
}
