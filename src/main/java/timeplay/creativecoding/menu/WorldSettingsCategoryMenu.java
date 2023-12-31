/*
 Creative TimePlay 2023

 Меню выбора категорий мира
 */
package timeplay.creativecoding.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.utils.MessageUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;

public class WorldSettingsCategoryMenu extends Menu {

    public WorldSettingsCategoryMenu(Player player) {

        super(6,getLocaleMessage("menus.world-settings.title"));

        Map<Integer, ItemStack> items = new HashMap<>();

        Plot plot = PlotManager.getPlotByPlayer(player);
        items.put(10,getCategorySandboxButton());
        items.put(11,getCategoryAdventureButton());
        items.put(12,getCategoryStrategyButton());
        items.put(13,getCategoryArcadeButton());
        items.put(14,getCategoryRolePlayButton());
        items.put(15,getCategoryStoryButton());
        items.put(16,getCategoryExperimentButton());
        items.put(19,getCategorySimulatorButton());
        items.put(46,getBackButton());

        this.setItems(items);

    }

    public static void openInventory(Player player) {
        player.closeInventory();
        player.openInventory(new WorldSettingsCategoryMenu(player).getInventory());
    }

    public static ItemStack getBackButton() {
        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.back.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.back.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategorySandboxButton() {
        ItemStack item = new ItemStack(Material.BRICKS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.sandbox.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.sandbox.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryAdventureButton() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.adventure.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.adventure.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryStrategyButton() {
        ItemStack item = new ItemStack(Material.SHIELD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.strategy.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.strategy.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryArcadeButton() {
        ItemStack item = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.arcade.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.arcade.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryRolePlayButton() {
        ItemStack item = new ItemStack(Material.CHEST_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.roleplay.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.roleplay.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryStoryButton() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.story.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.story.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategoryExperimentButton() {
        ItemStack item = new ItemStack(Material.TNT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.experiment.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.experiment.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCategorySimulatorButton() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.simulator.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.simulator.lore"));
        item.setItemMeta(meta);
        return item;
    }

}
