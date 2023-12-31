/*
 Creative TimePlay 2023

 Меню выбора шаблона для создания мира.
 НЕ ИСПОЛЬЗУЕТСЯ В 1.4
 */
package timeplay.creativecoding.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.utils.MessageUtils.*;

public class WorldCreationMenu extends Menu {

    public WorldCreationMenu(Player player) {
        super(3,getLocaleMessage("menus.world-creation.title",false));

        Map<Integer, ItemStack> items = new HashMap<>();

        items.put(10,getFlatWorldButton());
        items.put(12,getEmptyWorldButton());
        items.put(14,getWaterWorldButton());
        items.put(16,getPlainsWorldButton());

        this.setItems(items);
    }

    public static void openInventory(Player player) {
        player.openInventory(new WorldCreationMenu(player).getInventory());
    }

    public static ItemStack getFlatWorldButton() {
        ItemStack item = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-creation.items.flat.name"));
        meta.setLore(getLocaleItemDescription("menus.world-creation.items.flat.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getEmptyWorldButton() {
        ItemStack item = new ItemStack(Material.GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-creation.items.empty.name"));
        meta.setLore(getLocaleItemDescription("menus.world-creation.items.empty.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getWaterWorldButton() {
        ItemStack item = new ItemStack(Material.WATER_BUCKET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-creation.items.water.name"));
        meta.setLore(getLocaleItemDescription("menus.world-creation.items.water.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlainsWorldButton() {
        ItemStack item = new ItemStack(Material.GRASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-creation.items.plains.name"));
        meta.setLore(getLocaleItemDescription("menus.world-creation.items.plains.lore"));
        item.setItemMeta(meta);
        return item;
    }

}
