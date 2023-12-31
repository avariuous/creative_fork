/*
 Creative TimePlay 2023

 Меню Настройки мира
 Показывает доступные для измененения параметры мира
 */

package timeplay.creativecoding.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static timeplay.creativecoding.utils.MessageUtils.*;

public class WorldSettingsMenu extends Menu {

    // Открытие меню

    public WorldSettingsMenu(Player player) {

        super(6,getLocaleMessage("menus.world-settings.title"));

        Map<Integer,ItemStack> items = new HashMap<>();

        Plot plot = PlotManager.getPlotByPlayer(player);
        items.put(40,getPlotExampleButton(plot));
        items.put(10,getPlotNameButton());
        items.put(11,getPlotDescriptionButton());
        items.put(12,getPlotIconButton());
        items.put(16,getPlotSpawnButton());
        items.put(15,getPlotSharingButton(plot));
        items.put(19,getPlotCustomIDButton());
        items.put(24,getPlotFlagsButton());
        items.put(25,getPlotPlayersButton());
        items.put(14,getPlotCategoryButton());

        this.setItems(items);

    }

    public static void openInventory(Player player) {
        player.openInventory(new WorldSettingsMenu(player).getInventory());
    }

    public static ItemStack getPlotExampleButton(Plot plot) {
        ItemStack item = new ItemStack(plot.plotIcon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.world.name").replace("%plotName%",plot.plotName));
        List<String> lore = new ArrayList<>();
        for (String loreLine : getLocaleItemDescription("menus.world-settings.items.world.lore")) {
            if (loreLine.contains("%plotDescription%")) {
                String[] newLines = plot.plotDescription.split("\\\\n");
                for (String newLine : newLines) {
                    lore.add(loreLine.replace("%plotDescription%",ChatColor.translateAlternateColorCodes('&',newLine)));
                }
            } else {
                lore.add(parsePlotLines(plot,loreLine.replace("%id%",getLocaleMessage("menus.world-settings.items.world.id",false) + plot.plotCustomID)));
            }
        }
        meta.setLore(lore);
        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotNameButton() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-name.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.change-name.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotDescriptionButton() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-description.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.change-description.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotIconButton() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-icon.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.change-icon.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotCategoryButton() {
        ItemStack item = new ItemStack(Material.BOOKSHELF);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-category.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.change-category.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotCustomIDButton() {
        ItemStack item = new ItemStack(Material.LEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-id.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.change-id.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotSpawnButton() {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-spawn.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.change-spawn.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotFlagsButton() {
        ItemStack item = new ItemStack(Material.PISTON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.plot-flags.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.plot-flags.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotPlayersButton() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.plot-players.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings.items.plot-players.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlotSharingButton(Plot plot) {
        boolean isPublic = plot.plotSharing == Plot.Sharing.PUBLIC;
        Material material = Material.OAK_DOOR;
        if (!isPublic) material = Material.IRON_DOOR;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings.items.change-sharing.name"));
        List<String> lore = new ArrayList<>();

        String sharing1 = getLocaleMessage("menus.world-settings.items.change-sharing.sharing1",false);
        String sharing2 = getLocaleMessage("menus.world-settings.items.change-sharing.sharing2",false);

        String turnedOn = getLocaleMessage("menus.world-settings.turned-on",false);
        String turnedOnNegative = getLocaleMessage("menus.world-settings.turned-on-negative",false);
        String turnedOff = getLocaleMessage("menus.world-settings.turned-off",false);

        if (isPublic) {
            sharing1 = turnedOn + sharing1;
            sharing2 = turnedOff + sharing2;
        } else {
            sharing1 = turnedOff + sharing1;
            sharing2 = turnedOnNegative + sharing2;
        }

        for (String loreLine : getLocaleItemDescription("menus.world-settings.items.change-sharing.lore")) {
            lore.add(loreLine.replace("%sharing1%",sharing1).replace("%sharing2%",sharing2));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
