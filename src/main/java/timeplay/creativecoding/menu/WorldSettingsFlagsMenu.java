/*
 Creative TimePlay 2023

 Меню флагов мира
 */
package timeplay.creativecoding.menu;

import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.menu.buttons.RadioButton;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static timeplay.creativecoding.utils.FileUtils.setPlotConfigParameter;
import static timeplay.creativecoding.utils.MessageUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;

public class WorldSettingsFlagsMenu extends Menu {

    static String turnedOn = getLocaleMessage("menus.world-settings-flags.turned-on",false);
    static String turnedOff = getLocaleMessage("menus.world-settings-flags.turned-off",false);

    public WorldSettingsFlagsMenu(Player player) {

        super(6, getLocaleMessage("menus.world-settings.title"));

        Map<Integer, ItemStack> items = new HashMap<>();


        Plot plot = PlotManager.getPlotByPlayer(player);
        items.put(10,getPlayerDamageFlagButton(plot).getButtonItem());
        items.put(11,getDayAndNightFlagButton(plot).getButtonItem());
        items.put(12,getWeatherFlagButton(plot).getButtonItem());
        items.put(13,getJoinQuitMessagesFlagButton(plot).getButtonItem());
        items.put(15,getBlockInteractFlagButton(plot).getButtonItem());

        RadioButton fireSpreadButton = FireSpreadButton(plot);

        items.put(14,fireSpreadButton.getButtonItem());
        items.put(46,getBackButton());

        this.setItems(items);

    }

    public static void openInventory(Player player) {
        player.closeInventory();
        player.openInventory(new WorldSettingsFlagsMenu(player).getInventory());
    }

    public static ItemStack getBackButton() {
        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getLocaleItemName("menus.world-settings-categories.items.back.name"));
        meta.setLore(getLocaleItemDescription("menus.world-settings-categories.items.back.lore"));
        item.setItemMeta(meta);
        return item;
    }

    public static RadioButton getPlayerDamageFlagButton(Plot plot) {

        List<Runnable> choicesActions = new ArrayList<>();
        choicesActions.add(() -> {
            plot.playerDamageFlag = 1;
            setPlotConfigParameter(plot,"flags.day-cycle",1);

        });
        choicesActions.add(() -> {
            plot.playerDamageFlag = 2;
            setPlotConfigParameter(plot,"flags.day-cycle",2);

        });
        choicesActions.add(() -> {
            plot.playerDamageFlag = 3;
            setPlotConfigParameter(plot,"flags.day-cycle",3);

        });
        choicesActions.add(() -> {
            plot.playerDamageFlag = 4;
            setPlotConfigParameter(plot,"flags.day-cycle",4);

        });
        choicesActions.add(() -> {
            plot.playerDamageFlag = 5;
            setPlotConfigParameter(plot,"flags.day-cycle",5);
        });

        return new RadioButton(Material.TOTEM_OF_UNDYING,getLocaleItemName("menus.world-settings-flags.items.player-damage.name"),
                getLocaleItemDescription("menus.world-settings-flags.items.player-damage.lore"),
                plot.playerDamageFlag,5,choicesActions,"menus.world-settings-flags.items.player-damage.choices",
                "menus.world-settings-flags");
    }

    public static RadioButton getDayAndNightFlagButton(Plot plot) {

        List<Runnable> choicesActions = new ArrayList<>();
        choicesActions.add(() -> {
            plot.world.setTime(1000);
            plot.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            plot.dayCycleFlag = 1;
            setPlotConfigParameter(plot,"flags.day-cycle",1);

        });
        choicesActions.add(() -> {
            plot.world.setTime(12500);
            plot.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            plot.dayCycleFlag = 2;
            setPlotConfigParameter(plot,"flags.day-cycle",2);
        });
        choicesActions.add(() -> {
            plot.world.setTime(15000);
            plot.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            plot.dayCycleFlag = 3;
            setPlotConfigParameter(plot,"flags.day-cycle",3);
        });
        choicesActions.add(() -> {
            plot.world.setTime(1000);
            plot.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            plot.dayCycleFlag = 4;
            setPlotConfigParameter(plot,"flags.day-cycle",4);

        });

        return new RadioButton(Material.CLOCK,getLocaleItemName("menus.world-settings-flags.items.day-and-night.name"),
                getLocaleItemDescription("menus.world-settings-flags.items.day-and-night.lore"),
                plot.dayCycleFlag,4,choicesActions,"menus.world-settings-flags.items.day-and-night.choices",
                "menus.world-settings-flags");
    }

    public static RadioButton getWeatherFlagButton(Plot plot) {

        List<Runnable> choicesActions = new ArrayList<>();
        choicesActions.add(() -> {
            plot.world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            plot.world.setStorm(false);
            plot.weatherFlag = 1;
            setPlotConfigParameter(plot,"flags.weather",1);

        });
        choicesActions.add(() -> {
            plot.world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            plot.world.setStorm(true);
            plot.weatherFlag = 2;
            setPlotConfigParameter(plot,"flags.weather",2);
        });
        choicesActions.add(() -> {
            plot.world.setStorm(false);
            plot.world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
            plot.weatherFlag = 3;
            setPlotConfigParameter(plot,"flags.weather",3);
        });

        return new RadioButton(Material.WATER_BUCKET,getLocaleItemName("menus.world-settings-flags.items.weather.name"),
                getLocaleItemDescription("menus.world-settings-flags.items.weather.lore"),
                plot.weatherFlag,3,choicesActions,"menus.world-settings-flags.items.weather.choices",
                "menus.world-settings-flags");
    }

    public static RadioButton FireSpreadButton(Plot plot) {

        List<Runnable> choicesActions = new ArrayList<>();
        choicesActions.add(() -> {
            plot.world.setGameRule(GameRule.DO_FIRE_TICK,true);
            plot.fireSpreadFlag = 1;
            setPlotConfigParameter(plot,"flags.fire-spread",1);
        });
        choicesActions.add(() -> {
            plot.world.setGameRule(GameRule.DO_FIRE_TICK,false);
            plot.fireSpreadFlag = 2;
            setPlotConfigParameter(plot,"flags.fire-spread",2);
        });

        return new RadioButton(Material.CAMPFIRE,getLocaleItemName("menus.world-settings-flags.items.fire-spread.name"),
                getLocaleItemDescription("menus.world-settings-flags.items.fire-spread.lore"),
                plot.fireSpreadFlag,2,choicesActions,"menus.world-settings-flags.items.fire-spread.choices",
                "menus.world-settings-flags");
    }

    public static RadioButton getJoinQuitMessagesFlagButton(Plot plot) {

        List<Runnable> choicesActions = new ArrayList<>();
        choicesActions.add(() -> {
            plot.joinMesssagesFlag = 1;
            setPlotConfigParameter(plot,"flags.join-messages",1);
        });
        choicesActions.add(() -> {
            plot.joinMesssagesFlag = 2;
            setPlotConfigParameter(plot,"flags.join-messages",2);
        });

        return new RadioButton(Material.OAK_SIGN,getLocaleItemName("menus.world-settings-flags.items.join-messages.name"),
                getLocaleItemDescription("menus.world-settings-flags.items.join-messages.lore"),
                plot.joinMesssagesFlag,2,choicesActions,"menus.world-settings-flags.items.join-messages.choices",
                "menus.world-settings-flags");
    }

    public static RadioButton getBlockInteractFlagButton(Plot plot) {

        List<Runnable> choicesActions = new ArrayList<>();
        choicesActions.add(() -> {
            plot.blockInteractFlag = 1;
            setPlotConfigParameter(plot,"flags.block-interact",1);
        });
        choicesActions.add(() -> {
            plot.blockInteractFlag = 2;
            setPlotConfigParameter(plot,"flags.block-interact",2);
        });
        choicesActions.add(() -> {
            plot.blockInteractFlag = 3;
            setPlotConfigParameter(plot,"flags.block-interact",3);
        });
        choicesActions.add(() -> {
            plot.blockInteractFlag = 4;
            setPlotConfigParameter(plot,"flags.block-interact",4);
        });
        choicesActions.add(() -> {
            plot.blockInteractFlag = 5;
            setPlotConfigParameter(plot,"flags.block-interact",5);
        });

        return new RadioButton(Material.CHEST,getLocaleItemName("menus.world-settings-flags.items.block-interact.name"),
                getLocaleItemDescription("menus.world-settings-flags.items.block-interact.lore"),
                plot.blockInteractFlag,5,choicesActions,"menus.world-settings-flags.items.block-interact.choices",
                "menus.world-settings-flags");
    }
}