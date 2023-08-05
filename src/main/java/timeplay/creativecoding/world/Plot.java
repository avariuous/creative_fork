/*
 Creative TimePlay 2023

 Плот и его создание, параметры, получение информации
 */

package timeplay.creativecoding.world;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.utils.ErrorUtils.*;
import static timeplay.creativecoding.utils.FileUtils.getPlotConfig;
import static timeplay.creativecoding.world.Create.createFile;
import static timeplay.creativecoding.world.Create.setGamerule;
import static timeplay.creativecoding.world.PlotManager.loadedPlots;

public class Plot {

    public World world;
    public String worldName;
    public String worldDescription;
    public Material worldIcon;
    public mode worldMode;

    public int worldSize;
    public boolean isLoaded;
    public boolean isPublic;

    public Player owner;

    /* public Player[] whitelisted;
    public Player[] builders;
    public Player[] developers;*/

    public enum mode {
        PLAYING, BUILD, CODING
    }

    public Plot(Player player) {

        owner = player;

        worldName = "Мир игрока " + player.getName();
        worldDescription = "Мой крутой мир!";

        worldIcon = Material.DIAMOND;
        worldMode = mode.BUILD;
        worldSize = 256;

        loadedPlots.add(this);
        create(this);

    }

    public Plot(String fileName) {

        WorldCreator load = new WorldCreator(fileName);
        Bukkit.createWorld(load);
        this.world = Bukkit.getWorld(fileName);

        owner = Bukkit.getPlayer(getOwner(this));

        worldName = String.valueOf(getWorldName(this));
        worldDescription = getWorldDescription(this);

        worldIcon = Material.DIAMOND;
        worldMode = mode.BUILD;
        worldSize = 256;

        loadedPlots.add(this);

    }

    public void create(Plot plot) {

        Player player = plot.owner;
        String worldName = String.valueOf(Create.generateWorldID());

        try {
            player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",10,100,40);
            // Генерация и параметры мира
            Bukkit.createWorld(new WorldCreator(worldName).type(WorldType.FLAT).generateStructures(false));
            Bukkit.getWorld(worldName).getWorldBorder().setSize(plot.worldSize);
            setGamerule(worldName, GameRule.DO_MOB_SPAWNING,false);
            setGamerule(worldName,GameRule.DO_DAYLIGHT_CYCLE,false);
            setGamerule(worldName,GameRule.MOB_GRIEFING,false);
            setGamerule(worldName,GameRule.DO_WEATHER_CYCLE,false);
            setGamerule(worldName,GameRule.SHOW_DEATH_MESSAGES,true);
            setGamerule(worldName,GameRule.DO_IMMEDIATE_RESPAWN,true);
            Bukkit.getWorld(worldName).setTime(0);
            createFile(worldName, player);
            plot.world = Bukkit.getWorld(worldName);
            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
            player.sendTitle("§6§lДОБРО ПОЖАЛОВАТЬ","§7Это §nтвой§7 мир, делай что хочешь");
            player.playSound(player.getLocation(), Sound.valueOf("UI_TOAST_CHALLENGE_COMPLETE"),100,2);
            player.sendMessage("");
            player.sendMessage("§6§lДОБРО ПОЖАЛОВАТЬ В ТВОЙ МИР!");
            player.sendMessage("§7Здесь ты сможешь сотворить что угодно.");
            player.sendMessage("");
            player.sendMessage("§7 Паркуры, PvP-режимы, прятки и многие другие режимы...");
            player.sendMessage("§7 Ограничение лишь размер мира и возможности :(");
            player.sendMessage("§a Цель: Создай свой уникальный режим и собери 1000 посетителей");
            player.sendMessage("");
            player.getInventory().clear();
            player.setGameMode(GameMode.CREATIVE);
            // Попытка удалить все сущности с мира
            for (Entity entity : Bukkit.getWorld(worldName).getEntities()) {
                if (entity instanceof Zombie) {
                    entity.remove();
                }
                if (entity instanceof Spider) {
                    entity.remove();
                }
                if (entity instanceof Creeper) {
                    entity.remove();
                }
                if (entity instanceof Skeleton) {
                    entity.remove();
                }
            }
        } catch(Exception error) {
            sendPlayerErrorMessage(player,error.getMessage());
        }
    }

    public static Plot getPlotByPlayer(Player player) {
        for (Plot plot : loadedPlots) {
            if (plot.getPlayers(plot).contains(player)) {
                return plot;
            }
        }
        return null;
    }

    public static Plot getPlotByWorld(World world) {
        for (Plot plot : loadedPlots) {
            if (plot.world == world) {
                return plot;
            }
        }
        return null;
    }

    public static String getWorldName(Plot plot) {
        if (getPlotConfig(plot).get("title") != null) {
            return String.valueOf(getPlotConfig(plot).get("title"));
        } else {
            return "Неизвестное название";
        }
    }

    public static String getWorldDescription(Plot plot) {
        if (getPlotConfig(plot).get("description") != null) {
            return String.valueOf(getPlotConfig(plot).get("description"));
        } else {
            return "Неизвестное описание";
        }
    }

    public static Material getWorldIcon(Plot plot) {
        if (getPlotConfig(plot).get("icon") != null) {
            if (String.valueOf(getPlotConfig(plot).get("icon")).contains("AIR")) return Material.DIAMOND;
            return Material.valueOf(String.valueOf(getPlotConfig(plot).get("icon")));
        } else {
            return Material.REDSTONE;
        }
    }

    public static List<Player> getPlayers(Plot plot) {
        List<Player> playerList = new ArrayList<>();
        try {
            for (Player player : Bukkit.getWorld(plot.world.getName()).getPlayers()) {
                playerList.add(player);
            }
        } catch (NullPointerException error) {
            sendCriticalErrorMessage(error.getMessage());
        }
        return playerList;
    }

    public static String getOwner(Plot plot) {
        if (getPlotConfig(plot).get("owner") != null) {
            return String.valueOf(getPlotConfig(plot).get("owner"));
        } else {
            return "Неизвестный владелец";
        }
    }

    /*public void setWorldDescription(String worldDescription) {
        this.worldDescription = worldDescription;
    }

    public void setWorldIcon(Material worldIcon) {
        this.worldIcon = worldIcon;
    }

    public void setWorldMode(mode worldMode) {
        this.worldMode = worldMode;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }*/

    public void setPublic(boolean _public) {
        isPublic = _public;
    }
}
