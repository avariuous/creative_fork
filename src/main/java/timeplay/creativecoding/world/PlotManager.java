/*
 Creative TimePlay 2023

 Менеджер для плотов
 */

package timeplay.creativecoding.world;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlotManager {

    public static PlotManager plotManager;
    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    public PlotManager() {
        plotManager = this;
    }

    public static List<Plot> plots = new ArrayList<>();

    public static void loadPlot(Plot plot) {
        loadWorldFolder(plot.worldName,true);
        Bukkit.createWorld(new WorldCreator(plot.worldName));
        plot.isLoaded = true;
    }

    public static void loadPlot(String worldName) {
        loadWorldFolder(worldName,true);
        Bukkit.createWorld(new WorldCreator(worldName));
        Plot.getPlotByWorld(Bukkit.getWorld(worldName)).world = Bukkit.getWorld(worldName);
        Plot.getPlotByWorld(Bukkit.getWorld(worldName)).isLoaded = true;
    }

    public static void unloadPlot(Plot plot) {
        plot.isLoaded = false;
        Bukkit.unloadWorld(plot.worldName,true);
        unloadWorldFolder(plot.worldName,true);
    }

    public static List<Plot> getPlayerPlots(Player player) {
        List<Plot> playerPlots = new ArrayList<>();
        for (Plot plot : plots) {
            if (plot.owner.equalsIgnoreCase(player.getName())) {
                playerPlots.add(plot);
            }
        }
        return playerPlots;
    }

    public static void deletePlot(Plot plot) {

        try {
            // Телепортирует всех игроков в мире на спавн
            for (Player p : Plot.getPlayers(plot)) {
                Main.teleportToLobby(p);
            }
            // Удаляет папку мира
            plot.plotSharing = Plot.sharing.CLOSED;
            plots.remove(plot);
            deleteWorld(getPlotFolder(plot));
            // После 3 секунд удаления мир отгружается полностью
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                Bukkit.unloadWorld(plot.worldName,false);
            }, 60);
        } catch (NullPointerException error) {
            sendCriticalErrorMessage("При удалении мира возникла ошибка: " + error.getMessage());
        }
    }

    public static void deletePlot(Plot plot, Player player) {

        try {
            // Телепортирует всех игроков в мире на спавн
            for (Player p : Plot.getPlayers(plot)) {
                Main.teleportToLobby(p);
            }
            // Удаляет папку мира
            plot.plotSharing = Plot.sharing.CLOSED;
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

}
