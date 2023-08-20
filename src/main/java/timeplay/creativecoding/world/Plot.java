/*
 Creative TimePlay 2023

 Плот это мир Minecraft, который имеет свои параметры
 по типу названия, описания, значка, доступности посещения.

 */

package timeplay.creativecoding.world;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.Main.teleportToLobby;
import static timeplay.creativecoding.utils.ErrorUtils.*;
import static timeplay.creativecoding.utils.FileUtils.getPlotConfig;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.utils.WorldUtils.generateWorld;
import static timeplay.creativecoding.world.PlotManager.*;

public class Plot {

    public World world;
    public String worldName;
    public String worldID;

    public String plotName;
    public String plotDescription;
    public Material plotIcon;
    public mode plotMode;
    public sharing plotSharing;
    public category plotCategory;

    public int worldSize;
    public boolean isLoaded;

    public String owner;

    public enum mode {
        PLAYING, BUILD
    }

    public enum sharing {
        PUBLIC, PRIVATE, CLOSED
    }

    public enum category {
        SANDBOX, ROLEPLAY, STRATEGY, ARCADE
    }

    // Создание мира
    public Plot(Player player) {

        owner = player.getName();

        plotName = "Мир игрока " + player.getName();
        plotDescription = "Мой крутой мир!";

        plotIcon = Material.DIAMOND;
        plotMode = mode.BUILD;
        plotCategory = category.SANDBOX;
        plotSharing = sharing.PUBLIC;

        worldSize = 256;

        create(this);
        plots.add(this);

    }

    // Загрузка в базу миров
    public Plot(String fileName) {

        worldName = fileName;
        worldID = fileName.replace("plot","");

        owner = Plot.getOwner(this);

        plotName = getPlotName(this);
        plotDescription = getPlotDescription(this);
        isLoaded = false;

        plotIcon = getPlotIcon(this);
        plotMode = mode.BUILD;
        plotSharing = getWorldSharing(this);
        worldSize = 256;

        plots.add(this);

    }

    // Создание плота
    public void create(Plot plot) {

        Player player = Bukkit.getPlayer(plot.owner);
        String worldName = "plot" + Create.generateWorldID();

        player.sendTitle(getLocaleMessage("creating-world.title"),getLocaleMessage("creating-world.subtitle"),10,300,40);
        if (!generateWorld(plot,player,worldName)) {
            player.clearTitle();
            sendPlayerErrorMessage(player,"§cПроизошла ошибка при создании мира... \n§cОбратитесь к администрации!");
        }
    }

    // Получить плот с помощью игрока
    public static Plot getPlotByPlayer(Player player) {
        for (Plot plot : plots) {
            if (plot.getPlayers(plot).contains(player)) {
                return plot;
            }
        }
        return null;
    }

    // Получить плот по миру
    public static Plot getPlotByWorld(World world) {
        for (Plot plot : plots) {
            if (plot.worldName.equals(world.getName())) {
                return plot;
            }
        }
        return null;
    }

    public static Plot getPlotByWorldName(String worldName) {
        for (Plot plot : plots) {
            if (plot.worldName.equals(worldName)) {
                return plot;
            }
        }
        return null;
    }

    // Получить доступность посещения плота
    public static sharing getWorldSharing(Plot plot) {
        if (getPlotConfig(plot).get("sharing") != null) {
            try {
                return sharing.valueOf(String.valueOf(getPlotConfig(plot).get("sharing")));
            } catch (Exception error) {
                return sharing.PRIVATE;
            }
        } else {
            return sharing.PRIVATE;
        }
    }

    // Получить название плота
    public static String getPlotName(Plot plot) {
        if (getPlotConfig(plot).get("name") != null) {
            return String.valueOf(getPlotConfig(plot).get("name"));
        } else {
            return "Неизвестное название";
        }
    }

    // Получить описание плота
    public static String getPlotDescription(Plot plot) {
        if (getPlotConfig(plot).get("description") != null) {
            return String.valueOf(getPlotConfig(plot).get("description"));
        } else {
            return "Неизвестное описание";
        }
    }

    // Получить значок плота
    public static Material getPlotIcon (Plot plot) {
        if (getPlotConfig(plot).get("icon") != null) {
            if (String.valueOf(getPlotConfig(plot).get("icon")).contains("AIR")) return Material.DIAMOND;
            return Material.valueOf(String.valueOf(getPlotConfig(plot).get("icon")));
        } else {
            return Material.REDSTONE;
        }
    }

    public static int getOnline(Plot plot) {
        List<Player> playersList = getPlayers(plot);
        return playersList.size();
    }

    public static List<Player> getPlayers(Plot plot) {
        List<Player> playerList = new ArrayList<>();
        try {
            if (Bukkit.getWorld(plot.worldName) == null) return playerList;
            playerList.addAll(Bukkit.getWorld(plot.worldName).getPlayers());
            return playerList;
        } catch (Exception error) {
            return playerList;
        }
    }

    public static String getOwner(Plot plot) {
        if (getPlotConfig(plot).get("owner") != null) {
            return String.valueOf(getPlotConfig(plot).get("owner"));
        } else {
            return "Неизвестный владелец";
        }
    }

    public static int getPlayerPlotsLimit(Player player) {
        ConfigurationSection groupsSection = plugin.getConfig().getConfigurationSection("groups");
        if (groupsSection != null) {
            int playerPlotsLimit = 0;
            try {
                playerPlotsLimit = plugin.getConfig().getInt("groups.default.creating-world.limit");
                for (String group : groupsSection.getKeys(false)) {
                    if (!(group.equals("default"))) {
                        String permission = plugin.getConfig().getString("groups." + group + ".permission");
                        if (player.hasPermission(permission)) {
                            playerPlotsLimit = plugin.getConfig().getInt("groups." + group + ".creating-world.limit");
                        }
                    }
                }
            } catch (Exception error) {
                sendPlayerErrorMessage(player,"Невозможно получить количество миров, которое может создать игрок. " + error.getMessage());
            }
            return playerPlotsLimit;
        } else {
            return 0;
        }

    }

    // Телепортировать игрока на плот
    public static void teleportToPlot(Player player, Plot plot) {
        if (!(Plot.getWorldSharing(plot) == sharing.PUBLIC) && (!plot.owner.equalsIgnoreCase(player.getName()) || !player.hasPermission("creative.worlds.private"))) {
            player.closeInventory();
            player.sendMessage(getLocaleMessage("private-plot"));
            return;
        }
        player.sendTitle(getLocaleMessage("teleporting-to-world.title"),getLocaleMessage("teleporting-to-world.subtitle"),15,9999,15);
        if (!plot.isLoaded) {
            loadPlot(plot);
        }
        clearPlayer(player);
        player.teleport(plot.world.getSpawnLocation());
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,100,2);
        clearPlayer(player);
    }

    // Телепортировать игрока на плот (по названию мира)
    public static void teleportToPlot(Player player, String worldName) {
        Plot plot = Plot.getPlotByWorldName(worldName);
        if (!(plot.plotSharing == sharing.PUBLIC)) {
            if (!plot.owner.equalsIgnoreCase(player.getName()) && !player.hasPermission("creative.private.bypass")) {
                player.sendMessage(getLocaleMessage("private-plot", player));
                return;
            }
        }
        player.sendTitle(getLocaleMessage("teleporting-to-world.title"),getLocaleMessage("teleporting-to-world.subtitle"),15,9999,15);
        if (!plot.isLoaded) {
            loadPlot(worldName);
        }
        clearPlayer(player);
        player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
        player.clearTitle();
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,100,2);
        clearPlayer(player);
        if (plot.owner.equalsIgnoreCase(player.getName())) {
            player.sendMessage(getLocaleMessage("teleporting-to-world.owner-help",player));
            if (plot.plotMode == mode.BUILD) {
                player.setGameMode(GameMode.CREATIVE);
            }
        }
    }
}
