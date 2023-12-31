 /*
 Creative TimePlay 2023

 Список всех миров
 */


package timeplay.creativecoding.coding.menus;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.coding.CodeScript;
import timeplay.creativecoding.coding.activators.*;
import timeplay.creativecoding.menu.Menu;

import java.util.*;

import static timeplay.creativecoding.utils.MessageUtils.*;

 public class PlayerEventsMenu extends Menu {

    public static Map<Player,Integer> openedPage = new HashMap<>();
    public static Map<Player,List<Activator>> currentEventsList = new HashMap<>();
    public static Map<Player,Integer> chosenCategories = new HashMap<>();
    public static Map<Player,Location> signLocation = new HashMap<>();
    public static int[] eventsSlots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};

     private List<Activator> getEvents() {
        return CodeScript.getActivators();
    }

    public PlayerEventsMenu(Player player, int page, Location signLocation) {

        super(6,getLocaleMessage("blocks.event-player.sign",false));

        int[] eventsSlots = PlayerEventsMenu.eventsSlots;
        Map<Integer,ItemStack> items = new HashMap<>();

        PlayerEventsMenu.signLocation.put(player,signLocation);

        if (getEvents().isEmpty()) {
            ItemStack noWorldsItem = getNoEventsMenu();
            items.put(23,noWorldsItem);
        } else {
            List<List<Activator>> allPages = getPagesForEvents(getEvents());
            int pageToOpen = page;

            // Сбросить если недопустимое значение
            if (page > allPages.size() || page < 1) pageToOpen = 1;
            // Кнопки навигации
            if (pageToOpen > 1) {
                items.put(47,getPreviousPageButton());
            }
            if (pageToOpen < allPages.size()) {
                items.put(53,getNextPageButton(pageToOpen+1));
            }
            // Заполняем слоты активаторами

            int slot = 0;

            for (Activator activator: allPages.get(pageToOpen-1)) {
                ItemStack item = activator.getIcon();
                items.put(eventsSlots[slot],item);
                slot++;
            }

            openedPage.put(player,pageToOpen);
            currentEventsList.put(player,getEvents());
        }
        setItems(items);
    }

    public PlayerEventsMenu(Player player, int page, List<Activator> activatorsList, Location signLoc) {

        super(6,getLocaleMessage("blocks.event-player.sign",false));

        int[] eventsSlots = PlayerEventsMenu.eventsSlots;
        Map<Integer,ItemStack> items = new HashMap<>();
        PlayerEventsMenu.signLocation.put(player,signLoc);

        // Если ивентов нет
        if (getEvents().isEmpty()) {
            ItemStack noWorldsItem = getNoEventsMenu();
            items.put(22,noWorldsItem);
            // Если миры есть
        } else {
            // Список страниц по 20 плотов каждую
            List<List<Activator>> allPages = getPagesForEvents(activatorsList);
            int pageToOpen = page;

            // Сбросить если недопустимое значение
            if (page > allPages.size() || page < 1) pageToOpen = 1;
            // Кнопки навигации
            if (pageToOpen > 1) {
                items.put(47,getPreviousPageButton());
            }
            if (pageToOpen < allPages.size()) {
                items.put(53,getNextPageButton(pageToOpen+1));
            }
            // Заполняем слоты мирами
            int slot = 0;
            for (Activator activator: allPages.get(pageToOpen-1)) {
                ItemStack item = activator.getIcon();
                items.put(eventsSlots[slot],item);
                slot++;
            }

            openedPage.put(player,pageToOpen);
            //currentEventsList.put(player,items);
        }
        setItems(items);
    }

    // Показать инвентарь игроку
    public static void openInventory(Player player, int page, Location signLoc) {
        player.openInventory(new PlayerEventsMenu(player,page, signLoc).getInventory());
    }

    public static void openInventory(Player player, int page, List<Activator> activatorsList, Location signLoc) {
        player.openInventory(new PlayerEventsMenu(player,page,activatorsList,signLoc).getInventory());
    }

    public static ItemStack getNoEventsMenu() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.no-worlds.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.no-worlds.lore"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        return item;
    }

    public static ItemStack getNextPageButton(int page) {
        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.next-page.name").replace("page",String.valueOf(page)));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.next-page.lore"));
        item.setAmount(page);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPreviousPageButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.previous-page.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.previous-page.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public List<Activator> getCurrentPlotList(Player player) {
        return currentEventsList.getOrDefault(player,getEvents());
    }

    public int getCurrentPage(Player player) {
        return openedPage.getOrDefault(player, 1);
    }

    private static List<List<Activator>> getPagesForEvents(List<Activator> plotsList) {

        List<List<Activator>> pages = new ArrayList<>();

        int pageSize = 28;
        int pageCount = (int) Math.ceil((double) plotsList.size() / pageSize);

        for (int i = 0; i < pageCount; i++) {
            int fromIndex = i * pageSize;
            int toIndex = Math.min((i + 1) * pageSize, plotsList.size());

            List<Activator> sublist = plotsList.subList(fromIndex, toIndex);
            ArrayList<Activator> page = new ArrayList<>(sublist);

            pages.add(page);
        }

        return pages;
    }
}
