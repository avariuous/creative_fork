package timeplay.creativecoding.coding.menus.layouts;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import timeplay.creativecoding.coding.activators.Activator;
import timeplay.creativecoding.coding.menus.PlayerEventsMenu;
import timeplay.creativecoding.menu.Menu;

import java.util.List;
import java.util.Map;

public class MoreItemsLayout extends Menu {

    private String title;
    private Map<Integer,ItemStack> items;
    private int[] decorationSlots = {54,53,52,51,49,48,47,46};

    public MoreItemsLayout(String title, List<ItemStack> items, int parameter) {
        super(6,title);
        ItemStack decoration = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int decorationSlot : decorationSlots) {
            items.set(decorationSlot,decoration);
        }
        ItemStack parameterButton = new ItemStack(Material.SLIME_BALL);
        items.set(50,parameterButton);
        for (ItemStack item : items) {
            items.set(items.indexOf(item),item);
        }
    }

    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void openInventory(Player player) {
        player.openInventory(this.getInventory());
    }

}
