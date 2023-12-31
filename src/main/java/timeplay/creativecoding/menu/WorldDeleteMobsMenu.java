/*
 Creative TimePlay 2023

 Меню удаления мобов
 */
package timeplay.creativecoding.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.utils.MessageUtils.*;

public class WorldDeleteMobsMenu extends Menu {

    public WorldDeleteMobsMenu(Player player) {
        super(3,getLocaleMessage("menus.delete-mobs.title",false));

        Map<Integer, ItemStack> items = new HashMap<>();

        items.put(10,getDeleteItemsButton());
        items.put(13,getDeleteEntitiesButton());
        items.put(16,getDeleteMobsButton());

        this.setItems(items);
    }

    public static void openInventory(Player player) {
        player.openInventory(new WorldDeleteMobsMenu(player).getInventory());
    }

    public static ItemStack getDeleteItemsButton() {
        ItemStack item = new ItemStack(Material.POTATO);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.delete-mobs.items.items.name"));
        meta.setLore(getLocaleItemDescription("menus.delete-mobs.items.items.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getDeleteEntitiesButton() {
        ItemStack item = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.delete-mobs.items.entities.name"));
        meta.setLore(getLocaleItemDescription("menus.delete-mobs.items.entities.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getDeleteMobsButton() {
        ItemStack item = new ItemStack(Material.PIG_SPAWN_EGG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.delete-mobs.items.mobs.name"));
        meta.setLore(getLocaleItemDescription("menus.delete-mobs.items.mobs.lore"));
        item.setItemMeta(meta);
        return item;
    }


}
