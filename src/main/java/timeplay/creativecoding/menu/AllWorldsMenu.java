/*
 Creative TimePlay 2023

 Список всех миров
 */

package timeplay.creativecoding.menu;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.world.Plot;

import java.util.*;

import static timeplay.creativecoding.utils.MessageUtils.*;
import static timeplay.creativecoding.world.PlotManager.plots;

public class AllWorldsMenu extends Menu {

    public static Map<Player,Integer> openedPage = new HashMap<>();
    public static int[] worldSlots = {12,13,14,15,16,21,22,23,24,25,30,31,32,33,34,39,40,41,42,43};

    public AllWorldsMenu(Player player, int page) {

        super(6,getLocaleMessage("menus.all-worlds.title",false));

        int[] worldSlots = AllWorldsMenu.worldSlots;
        int[] decorationSlots = {1,10,19,28,37,46};
        Map<Integer,ItemStack> items = new HashMap<>();

        // скоро!!!
        //items.put(0,getSearchButton());
        //items.put(18,getCategorySandboxButton());
        //items.put(27,getCategoryRolePlayButton());
        items.put(45,getOwnWorldsButton());

        for (int slot : decorationSlots) {
            ItemStack decorationItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = decorationItem.getItemMeta();
            meta.setDisplayName(" ");
            decorationItem.setItemMeta(meta);
            items.put(slot,decorationItem);
        }

        // Если миров нет
        if (plots == null || plots.size() == 0) {
            ItemStack noWorldsItem = getNoWorldsButton();
            items.put(23,noWorldsItem);
        // Если миры есть
        } else {
            // Список страниц по 20 плотов каждую
            List<List<Plot>> allPages = getPagesForPlots();
            int pageToOpen = page;

            // Сбросить если недопустимое значение
            if (page > allPages.size() || page < 1) pageToOpen = 1;
            // Кнопки навигации
            if (pageToOpen > 1) {
                items.put(47,getPreviousPageButton());
            }
            if (pageToOpen < allPages.size()) {
                items.put(53,getNextPageButton());
            }
            // Заполняем слоты мирами
            int slot = 0;
            for (Plot plot: allPages.get(pageToOpen-1)) {
                Material material = plot.plotIcon;
                if (!(plot.plotSharing == Plot.sharing.PUBLIC)) material = Material.BARRIER;
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.world.name").replace("%plotName%",plot.plotName));
                List<String> lore = new ArrayList<String>();
                lore.add("§8ID: " + plot.worldID);
                for (String loreLine : getLocaleItemDescription("menus.all-worlds.items.world.lore")) {
                    lore.add(loreLine.replace("%plotName%",plot.plotName).replace("%plotDescription%",plot.plotDescription).replace("%plotOnline%",String.valueOf(Plot.getOnline(plot))).replace("%plotOwner%",plot.owner).replace("%plotID%",plot.worldID));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
                items.put(worldSlots[slot],item);
                slot++;
            }

            if (pageToOpen > 1) setTitle(getLocaleMessage("menus.all-worlds.title-pages",false).replace("%page%",String.valueOf(pageToOpen)).replace("%pages%",String.valueOf(allPages.size())));
            openedPage.put(player,pageToOpen);
        }
        setItems(items);
    }

    // Показать инвентарь игроку
    public static void openInventory(Player player, int page) {
        player.openInventory(new AllWorldsMenu(player,page).getInventory());
    }

    public static ItemStack getOwnWorldsButton() {
        ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.own-worlds.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.own-worlds.lore"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);;
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        return item;
    }

    public static ItemStack getNoWorldsButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.no-worlds.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.no-worlds.lore"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);;
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        return item;
    }


    public static ItemStack getNextPageButton() {
        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.next-page.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.next-page.lore"));
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

    /*public static ItemStack getCategorySandboxButton() {
        ItemStack item = new ItemStack(Material.SAND);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        meta.setDisplayName("§e§lПесочница");
        lore.add("§8Категория");
        lore.add(" ");
        lore.add(" §7В этой категории находятся миры,");
        lore.add(" §7которые разрешают игрокам творить. ");
        lore.add(" ");
        lore.add("§eНажми, чтобы сменить категорию");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryRolePlayButton() {
        ItemStack item = new ItemStack(Material.MINECART);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        meta.setDisplayName("§6§lРолевые игры");
        lore.add("§8Категория");
        lore.add(" ");
        lore.add(" §7Такие игры дадут тебе возможность");
        lore.add(" §7побыть мамой, папой или котом.");
        lore.add(" ");
        lore.add("§eНажми, чтобы сменить категорию");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSearchButton() {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        meta.setDisplayName("§bНайти мир по названию");
        lore.add("§8Поиск миров по названию");
        lore.add(" ");
        lore.add(" §7В категориях не нашёл подходящего мира?");
        lore.add(" §7Не расстраивайся, ведь тебе доступен поиск");
        lore.add(" §7мира по названию, например 'PvP'. ");
        lore.add(" ");
        lore.add("§eНажми, чтобы начать поиск");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);;
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        return item;
    }*/

    private static List<List<Plot>> getPagesForPlots() {

        List<List<Plot>> pages = new ArrayList<>();

        int pageSize = 20;
        int pageCount = (int) Math.ceil((double) plots.size() / pageSize);

        Comparator<Plot> plotComparator = (plot1, plot2) -> Integer.compare(Plot.getOnline(plot2), Plot.getOnline(plot1));

        plots.sort(plotComparator);

        for (int i = 0; i < pageCount; i++) {
            int fromIndex = i * pageSize;
            int toIndex = Math.min((i + 1) * pageSize, plots.size());

            List<Plot> sublist = plots.subList(fromIndex, toIndex);
            ArrayList<Plot> page = new ArrayList<>(sublist);

            pages.add(page);
        }

        return pages;
    }
}
