/*
 Creative TimePlay 2023

 Менеджер для плотов
 */

package timeplay.creativecoding.plots;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.coding.CodeScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlotManager {

    public static PlotManager plotManager;
    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    // В этом списке содержатся все плоты, которые
    // добавлены при загрузке плагина, либо же создании плота.
    public static List<Plot> plots = new ArrayList<>();

    // Загрузка мира плота, например когда игрок пытается в него зайти
    public static void loadPlot(Plot plot) {
        loadWorldFolder(plot.worldName,true);
        Bukkit.createWorld(new WorldCreator(plot.worldName));
        plot.world = Bukkit.getWorld(plot.worldName);
        plot.isLoaded = true;
        plot.script = new CodeScript(plot,getPlotScriptFile(plot));
        setPlotConfigParameter(plot,"last-activity-time",System.currentTimeMillis());
        plot.world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
    }

    public static void loadPlotFlags(Plot plot) {

        Map<String,Integer> flags = getPlotFlagsFromPlotConfig(plot);

        int playerDamageFlag = flags.getOrDefault("player-damage",1);
        int dayCycleFlag = flags.getOrDefault("day-cycle",1);
        int joinMessagesFlag = flags.getOrDefault("join-messages",1);
        int fireSpreadFlag = flags.getOrDefault("fire-spread",1);
        int weatherFlag = flags.getOrDefault("weather",1);
        int interactFlag = flags.getOrDefault("block-interact",1);

        plot.playerDamageFlag = playerDamageFlag;
        plot.dayCycleFlag = dayCycleFlag;
        plot.joinMesssagesFlag = joinMessagesFlag;
        plot.fireSpreadFlag = fireSpreadFlag;
        plot.weatherFlag = weatherFlag;
        plot.blockInteractFlag = interactFlag;

    }

    // Отгрузка мира плота, например когда нет игроков в мире
    public static void unloadPlot(Plot plot) {
        plot.isLoaded = false;
        setPlotConfigParameter(plot,"last-activity-time",System.currentTimeMillis());
        Bukkit.unloadWorld(plot.worldName,true);
        unloadWorldFolder(plot.worldName,true);
        if (plot.devPlot.isLoaded) {
            plot.devPlot.isLoaded = false;
            Bukkit.unloadWorld(plot.devPlot.worldName,true);
            unloadWorldFolder(plot.devPlot.worldName,true);
        }

    }

    // Получить плоты, созданные игроком
    public static List<Plot> getPlayerPlots(Player player) {
        List<Plot> playerPlots = new ArrayList<>();
        for (Plot plot : plots) {
            if (plot.owner.equalsIgnoreCase(player.getName())) {
                playerPlots.add(plot);
            }
        }
        return playerPlots;
    }

    // Удаление плота
    public static void deletePlot(Plot plot) {
        try {
            // Телепортирует всех игроков в мире на спавн
            for (Player p : plot.getPlayers()) {
                Main.teleportToLobby(p);
            }
            if (plot.devPlot.exists()) {
                deleteWorld(getDevPlotFolder(plot.devPlot));
            }
            // Удаляет папку мира
            plot.plotSharing = Plot.Sharing.CLOSED;
            plots.remove(plot);
            deleteWorld(getPlotFolder(plot));

            // После 3 секунд удаления мир отгружается полностью
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                Bukkit.unloadWorld(plot.worldName,false);
                if (plot.devPlot.isLoaded) Bukkit.unloadWorld(plot.devPlot.worldName, false);
            }, 60);
        } catch (NullPointerException error) {
            sendCriticalErrorMessage("При удалении мира возникла ошибка: " + error.getMessage());
        }
    }

    public static void deletePlot(Plot plot, Player player) {
        try {
            // Телепортирует всех игроков в мире на спавн
            for (Player p : plot.getPlayers()) {
                Main.teleportToLobby(p);
            }
            // Удаляет папку мира
            plot.plotSharing = Plot.Sharing.CLOSED;
            plots.remove(plot);
            deleteWorld(getPlotFolder(plot));
            // После 3 секунд удаления мир отгружается полностью
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                Bukkit.unloadWorld(plot.worldName,false);
                player.sendMessage(getLocaleMessage("deleting-world.message"));
            }, 60);
        } catch (NullPointerException error) {
            sendCriticalErrorMessage("При удалении мира возникла ошибка: " + error.getMessage());
        }
    }

    public static List<Plot> getPlotsByPlotName(String worldName) {
        List<Plot> foundPlots = new ArrayList<>();
        for (Plot plot : plots) {
            if (plot.plotName.toLowerCase().contains(worldName.toLowerCase())) {
                foundPlots.add(plot);
            }
        }
        return foundPlots;
    }

    public static List<Plot> getPlotsByID(String worldID) {
        List<Plot> foundPlots = new ArrayList<>();
        for (Plot plot : plots) {
            if (plot.plotCustomID.toLowerCase().contains(worldID.toLowerCase())) {
                foundPlots.add(plot);
            }
        }
        return foundPlots;
    }

    public static List<Plot> getPlotsByCategory(Plot.Category category) {
        List<Plot> foundPlots = new ArrayList<>();
        for (Plot plot : plots) {
            if (plot.plotCategory == category) {
                foundPlots.add(plot);
            }
        }
        return foundPlots;
    }

    // Получить плот с помощью игрока
    public static Plot getPlotByPlayer(Player player) {
        for (Plot plot : plots) {
            if (plot.getPlayers().contains(player)) {
                return plot;
            }
        }
        return null;
    }

    public static DevPlot getDevPlot(Player player) {
        for (Plot plot : plots) {
            if (plot.devPlot.isLoaded) {
                if (plot.getPlayers().contains(player)) {
                    if (plot.devPlot.world.getPlayers().contains(player)) {
                        return plot.devPlot;
                    }
                }
            }
        }
        return null;
    }

    // Получить плот с помощью игрока
    public static Plot getPlotByWorld(World world) {
        for (Plot plot : plots) {
            if (plot.world == world) {
                return plot;
            }
        }
        return null;
    }

    // Получить плот с помощью игрока
    public static Plot getPlotByWorldName(String worldName) {
        for (Plot plot : plots) {
            if (plot.worldName.equalsIgnoreCase(worldName)) {
                return plot;
            }
        }
        return null;
    }

    // Получить плот с кастомным ID
    public static Plot getPlotByCustomID(String customID) {
        for (Plot plot : plots) {
            if (plot.plotCustomID.equalsIgnoreCase(customID)) {
                return plot;
            }
        }
        return null;
    }

}
