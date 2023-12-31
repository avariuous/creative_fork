/*
 Creative TimePlay 2023

 Утилита для работы с мирами
 */

package timeplay.creativecoding.utils;

import org.bukkit.*;
import org.bukkit.entity.*;
import timeplay.creativecoding.plots.Plot;

import java.io.File;

import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class WorldUtils {

    // Создание мира для плота
    public static boolean generateWorld(Plot plot, Player player, String worldName) {

        World world = Bukkit.createWorld(new WorldCreator(worldName).type(WorldType.FLAT).generateStructures(false));

        if (world != null) {
            world.getWorldBorder().setSize(plot.worldSize);

            world.setGameRule(GameRule.DO_MOB_SPAWNING,false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
            world.setGameRule(GameRule.MOB_GRIEFING,false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES,true);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);

            world.setTime(0);
            world.setSpawnLocation(0,5,0);
            createWorldFile(worldName, player);

            plot.world = Bukkit.getWorld(worldName);
            plot.worldName = worldName;
            plot.worldID = worldName.replace("plot","");
            plot.plotCustomID = plot.worldID;
            plot.isLoaded = true;

            // Для игрока сообщение и телепортация
            player.teleport(world.getSpawnLocation());
            clearPlayer(player);
            player.sendTitle(getLocaleMessage("creating-world.welcome-title",player),getLocaleMessage("creating-world.welcome-subtitle",player),15,180,45);
            player.playSound(player.getLocation(), Sound.valueOf("UI_TOAST_CHALLENGE_COMPLETE"),100,2);
            player.sendMessage(getLocaleMessage("creating-world.welcome"));
            player.setGameMode(GameMode.CREATIVE);

            // Попытка удалить все сущности с мира
            for (Entity entity : world.getEntities()) {
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
            return true;

        } else {
            sendCriticalErrorMessage("При попытке создать мир для плота " + worldName + ", мир оказался null");
            return false;
        }

    }

    // Генерация ID для мира
    public static int generateWorldID() {
        int newWorldID;
        try {
            newWorldID = plugin.getConfig().getInt("last-world-id");
        } catch (NullPointerException e) {
            newWorldID = 1;
            plugin.getConfig().set("last-world-id",newWorldID);
            plugin.saveConfig();
        }

        while (true) {
            // Будет добавлять по единице и проверять существует ли файл с таким названием
            newWorldID++;
            boolean exists = false;
            for (File folder : getWorldsFolders(true)) {
                if (folder.getName().equalsIgnoreCase("plot"+newWorldID)) exists = true;
            }
            if (!exists) {
                plugin.getConfig().set("last-world-id",newWorldID);
                plugin.saveConfig();
                return newWorldID;
            }
        }
    }

}
