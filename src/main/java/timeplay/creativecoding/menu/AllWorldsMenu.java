/*
 Creative TimePlay 2023

 Список всех миров
 */

package timeplay.creativecoding.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.plots.Plot;

import java.util.*;

import static timeplay.creativecoding.utils.MessageUtils.*;
import static timeplay.creativecoding.plots.PlotManager.plots;

public class AllWorldsMenu extends Menu {

    public static Map<Player,Integer> openedPage = new HashMap<>();
    public static Map<Player,List<Plot>> currentPlotList = new HashMap<>();
    public static Map<Player,Integer> chosenCategories = new HashMap<>();
    public static Map<Player,Integer> chosenSorts = new HashMap<>();
    public static int[] worldSlots = {12,13,14,15,16,21,22,23,24,25,30,31,32,33,34,39,40,41,42,43};

    public AllWorldsMenu(Player player, int page) {

        super(6,getLocaleMessage("menus.all-worlds.title",false));

        int[] worldSlots = AllWorldsMenu.worldSlots;
        int[] decorationSlots = {1,10,19,28,37,46};
        Map<Integer,ItemStack> items = new HashMap<>();

        items.put(0,getSearchButton());
        items.put(27,getCategoryButton(player));
        items.put(36,getSortButton(player));
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
            if (!chosenSorts.containsKey(player)) chosenSorts.put(player,1);
            List<List<Plot>> allPages = getPagesForPlots(plots,chosenSorts.get(player));
            int pageToOpen = page;

            // Сбросить если недопустимое значение
            if (page > allPages.size() || page < 1) pageToOpen = 1;
            // Кнопки навигации
            if (pageToOpen > 1) {
                items.put(47,getPreviousPageButton(pageToOpen));
            }
            if (pageToOpen < allPages.size()) {
                items.put(53,getNextPageButton(pageToOpen+1));
            }
            // Заполняем слоты мирами
            int slot = 0;
            for (Plot plot: allPages.get(pageToOpen-1)) {
                Material material = plot.plotIcon;
                if (!(plot.plotSharing == Plot.Sharing.PUBLIC)) material = Material.BARRIER;
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.world.name").replace("%plotName%",plot.plotName));
                List<String> lore = new ArrayList<>();
                for (String loreLine : getLocaleItemDescription("menus.all-worlds.items.world.lore")) {
                    if (loreLine.contains("%plotDescription%")) {
                        String[] newLines = plot.plotDescription.split("\\\\n");
                        for (String newLine : newLines) {
                            lore.add(loreLine.replace("%plotDescription%", ChatColor.translateAlternateColorCodes('&',newLine)));
                        }
                    } else {
                        lore.add(parsePlotLines(plot,loreLine.replace("%id%",getLocaleMessage("menus.all-worlds.items.world.id",false) + plot.plotCustomID)));
                    }
                }
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_DYE);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.setLore(lore);
                item.setItemMeta(meta);
                items.put(worldSlots[slot],item);
                slot++;
            }

            if (pageToOpen > 1) setTitle(getLocaleMessage("menus.all-worlds.title-pages",false).replace("%page%",String.valueOf(pageToOpen)).replace("%pages%",String.valueOf(allPages.size())));
            openedPage.put(player,pageToOpen);
            currentPlotList.put(player,plots);
        }
        setItems(items);
    }

    public AllWorldsMenu(Player player, int page, List<Plot> plotsList) {

        super(6,getLocaleMessage("menus.all-worlds.title",false));

        int[] worldSlots = AllWorldsMenu.worldSlots;
        int[] decorationSlots = {1,10,19,28,37,46};
        Map<Integer,ItemStack> items = new HashMap<>();

        items.put(0,getSearchButton());
        items.put(27,getCategoryButton(player));
        items.put(36,getSortButton(player));
        items.put(45,getOwnWorldsButton());

        for (int slot : decorationSlots) {
            ItemStack decorationItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = decorationItem.getItemMeta();
            meta.setDisplayName(" ");
            decorationItem.setItemMeta(meta);
            items.put(slot,decorationItem);
        }

        // Если миров нет
        if (plotsList == null || plotsList.size() == 0) {
            ItemStack noWorldsItem = getNoWorldsButton();
            items.put(23,noWorldsItem);
            // Если миры есть
        } else {
            // Список страниц по 20 плотов каждую
            if (!chosenSorts.containsKey(player)) chosenSorts.put(player,1);
            List<List<Plot>> allPages = getPagesForPlots(plotsList,chosenSorts.get(player));
            int pageToOpen = page;

            // Сбросить если недопустимое значение
            if (page > allPages.size() || page < 1) pageToOpen = 1;
            // Кнопки навигации
            if (pageToOpen > 1) {
                items.put(47,getPreviousPageButton(pageToOpen));
            }
            if (pageToOpen < allPages.size()) {
                items.put(53,getNextPageButton(pageToOpen+1));
            }
            // Заполняем слоты мирами
            int slot = 0;
            for (Plot plot: allPages.get(pageToOpen-1)) {
                Material material = plot.plotIcon;
                if (!(plot.plotSharing == Plot.Sharing.PUBLIC)) material = Material.BARRIER;
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.world.name").replace("%plotName%",plot.plotName));
                List<String> lore = new ArrayList<>();
                for (String loreLine : getLocaleItemDescription("menus.all-worlds.items.world.lore")) {
                    if (loreLine.contains("%plotDescription%")) {
                        String[] newLines = plot.plotDescription.split("\\\\n");
                        for (String newLine : newLines) {
                            lore.add(loreLine.replace("%plotDescription%", ChatColor.translateAlternateColorCodes('&',newLine)));
                        }
                    } else {
                        lore.add(parsePlotLines(plot,loreLine.replace("%id%",getLocaleMessage("menus.all-worlds.items.world.id",false) + plot.plotCustomID)));
                    }
                }
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_DYE);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.setLore(lore);
                item.setItemMeta(meta);
                items.put(worldSlots[slot],item);
                slot++;
            }

            if (pageToOpen > 1) setTitle(getLocaleMessage("menus.all-worlds.title-pages",false).replace("%page%",String.valueOf(pageToOpen)).replace("%pages%",String.valueOf(allPages.size())));
            openedPage.put(player,pageToOpen);
            currentPlotList.put(player,plotsList);
        }
        setItems(items);
    }

    // Показать инвентарь игроку
    public static void openInventory(Player player, int page) {
        player.openInventory(new AllWorldsMenu(player,page).getInventory());
    }

    public static void openInventory(Player player, int page, List<Plot> plotsList) {
        player.openInventory(new AllWorldsMenu(player,page,plotsList).getInventory());
    }

    public static ItemStack getOwnWorldsButton() {
        ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.own-worlds.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.own-worlds.lore"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        return item;
    }

    public static ItemStack getNoWorldsButton() {
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
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.next-page.name").replace("%page%",String.valueOf(page)));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.next-page.lore"));
        item.setAmount(page);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPreviousPageButton(int page) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.previous-page.name").replace("%page%",String.valueOf(page-1)));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.previous-page.lore"));
        item.setAmount(page-1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryButton(Player player) {
        ItemStack item = new ItemStack(Material.CHEST_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.category.name"));
        List<String> lore = new ArrayList<>();

        if (!chosenCategories.containsKey(player)) chosenCategories.put(player,1);

        String chosen = getLocaleMessage("menus.all-worlds.chosen",false);
        String notChosen = getLocaleMessage("menus.all-worlds.not-chosen",false);

        String categoryAll = notChosen + getLocaleMessage("menus.all-worlds.items.category.all",false);
        String categorySandbox = notChosen + getLocaleMessage("menus.all-worlds.items.category.sandbox",false);
        String categoryAdventure = notChosen + getLocaleMessage("menus.all-worlds.items.category.adventure",false);
        String categoryStrategy = notChosen + getLocaleMessage("menus.all-worlds.items.category.strategy",false);
        String categoryArcade = notChosen + getLocaleMessage("menus.all-worlds.items.category.arcade",false);
        String categoryRolePlay = notChosen + getLocaleMessage("menus.all-worlds.items.category.roleplay",false);
        String categoryStory = notChosen + getLocaleMessage("menus.all-worlds.items.category.story",false);
        String categoryExperiment = notChosen + getLocaleMessage("menus.all-worlds.items.category.experiment",false);
        String categorySimulator = notChosen + getLocaleMessage("menus.all-worlds.items.category.simulator",false);

        switch (chosenCategories.get(player)) {
            default:
            case 1:
                categoryAll = categoryAll.replace(notChosen,chosen);
                break;
            case 2:
                categorySandbox = categorySandbox.replace(notChosen,chosen);
                break;
            case 3:
                categoryAdventure = categoryAdventure.replace(notChosen,chosen);
                break;
            case 4:
                categoryStrategy = categoryStrategy.replace(notChosen,chosen);
                break;
            case 5:
                categoryArcade = categoryArcade.replace(notChosen,chosen);
                break;
            case 6:
                categoryRolePlay = categoryRolePlay.replace(notChosen,chosen);
                break;
            case 7:
                categoryStory = categoryStory.replace(notChosen,chosen);
                break;
            case 8:
                categoryExperiment = categoryExperiment.replace(notChosen,chosen);
                break;
            case 9:
                categorySimulator = categorySimulator.replace(notChosen,chosen);
                break;

        }

        for (String loreLine : getLocaleItemDescription("menus.all-worlds.items.category.lore")) {
            lore.add(loreLine.replace("%all%",categoryAll).replace("%sandbox%",categorySandbox).replace("%adventure%",categoryAdventure).replace("%strategy%",categoryStrategy).replace("%arcade%",categoryArcade).replace("%roleplay%",categoryRolePlay).replace("%story%",categoryStory).replace("%experiment%",categoryExperiment).replace("%simulator%",categorySimulator));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSortButton(Player player) {
        ItemStack item = new ItemStack(Material.HOPPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.sort.name"));
        List<String> lore = new ArrayList<>();

        if (!chosenSorts.containsKey(player)) chosenSorts.put(player,1);

        String online = getLocaleMessage("menus.all-worlds.items.sort.online",false);
        String likes = getLocaleMessage("menus.all-worlds.items.sort.likes",false);
        String last = getLocaleMessage("menus.all-worlds.items.sort.last",false);

        String chosen = getLocaleMessage("menus.all-worlds.chosen",false);
        String notChosen = getLocaleMessage("menus.all-worlds.not-chosen",false);

        switch (chosenSorts.get(player)) {
            default:
            case 1:
                online = chosen + online;
                likes = notChosen + likes;
                last = notChosen + last;
                break;
            case 2:
                online = notChosen + online;
                likes = chosen + likes;
                last = notChosen + last;
                break;
            case 3:
                online = notChosen + online;
                likes = notChosen + likes;
                last = chosen + last;
                break;
        }

        for (String loreLine : getLocaleItemDescription("menus.all-worlds.items.sort.lore")) {
            lore.add(loreLine.replace("%online%",online).replace("%likes%",likes).replace("%last%",last));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSearchButton() {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.all-worlds.items.search.name"));
        meta.setLore(getLocaleItemDescription("menus.all-worlds.items.search.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static Plot.Category getPlayerCategory(Player player) {
        switch (chosenCategories.get(player)) {
            default:
            case 2:
                return Plot.Category.SANDBOX;
            case 3:
                return Plot.Category.ADVENTURE;
            case 4:
                return Plot.Category.STRATEGY;
            case 5:
                return Plot.Category.ARCADE;
            case 6:
                return Plot.Category.ROLEPLAY;
            case 7:
                return Plot.Category.STORY;
            case 8:
                return Plot.Category.EXPERIMENT;
            case 9:
                return Plot.Category.SIMULATOR;
        }
    }

    public static List<Plot> getCurrentPlotList(Player player) {
        return currentPlotList.getOrDefault(player,plots);
    }

    public static int getCurrentPage(Player player) {
        return openedPage.getOrDefault(player, 1);
    }

    private static List<List<Plot>> getPagesForPlots(List<Plot> plotsList, int sortType) {

        List<List<Plot>> pages = new ArrayList<>();

        int pageSize = 20;
        int pageCount = (int) Math.ceil((double) plotsList.size() / pageSize);

        Comparator<Plot> plotComparator;

        switch (sortType) {
            default:
            case 1:
                plotComparator = (plot1, plot2) -> Integer.compare(plot2.getOnline(), plot1.getOnline());
                break;
            case 2:
                plotComparator = (plot1, plot2) -> Integer.compare(plot2.getReputation(), plot1.getReputation());
                break;
            case 3:
                plotComparator = (plot1, plot2) -> Long.compare(plot2.getCreationTime(), plot1.getCreationTime());
                break;
        }

        plotsList.sort(plotComparator);

        for (int i = 0; i < pageCount; i++) {
            int fromIndex = i * pageSize;
            int toIndex = Math.min((i + 1) * pageSize, plotsList.size());

            List<Plot> sublist = plotsList.subList(fromIndex, toIndex);
            ArrayList<Plot> page = new ArrayList<>(sublist);

            pages.add(page);
        }

        return pages;
    }
}
