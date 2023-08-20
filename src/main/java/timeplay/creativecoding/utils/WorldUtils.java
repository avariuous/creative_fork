/*
 Creative TimePlay 2023

 Утилита для работы с мирами
 */

package timeplay.creativecoding.utils;

import org.bukkit.*;
import org.bukkit.entity.*;
import timeplay.creativecoding.world.Plot;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.Create.setGamerule;

public class WorldUtils {

    // Создать мир
    public static boolean generateWorld(Plot plot, Player player, String worldName) {

        try {
            Bukkit.createWorld(new WorldCreator(worldName).type(WorldType.FLAT).generateStructures(false));
            Bukkit.getWorld(worldName).getWorldBorder().setSize(plot.worldSize);

            setGamerule(worldName, GameRule.DO_MOB_SPAWNING,false);
            setGamerule(worldName,GameRule.DO_DAYLIGHT_CYCLE,false);
            setGamerule(worldName,GameRule.MOB_GRIEFING,false);
            setGamerule(worldName,GameRule.DO_WEATHER_CYCLE,false);
            setGamerule(worldName,GameRule.SHOW_DEATH_MESSAGES,true);
            setGamerule(worldName,GameRule.DO_IMMEDIATE_RESPAWN,true);

            Bukkit.getWorld(worldName).setTime(0);
            Bukkit.getWorld(worldName).setSpawnLocation(0,5,0);
            createWorldFile(worldName, player);

            plot.world = Bukkit.getWorld(worldName);
            plot.worldName = worldName;
            plot.worldID = worldName.replace("plot","");
            plot.isLoaded = true;

            // Для игрока сообщение и телепортация
            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
            player.sendTitle(getLocaleMessage("creating-world.welcome-title",player),getLocaleMessage("creating-world.welcome-subtitle",player));
            player.playSound(player.getLocation(), Sound.valueOf("UI_TOAST_CHALLENGE_COMPLETE"),100,2);
            player.sendMessage(getLocaleMessage("creating-world.welcome"));
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
            return true;
        } catch (Exception error) {
            sendCriticalErrorMessage("При создании мира " + worldName + " произошла ошибка: " + error.getMessage());
            return false;
        }

    }

}
