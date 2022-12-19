/*
 Creative- TimePlay 2022

 Создать мир
 */

package timeplay.creativecoding.World;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.World.Delete.plugin;

public class Create {
    public static void CreateWorld(Player player) {
        String nickname = player.getName();
        String worldname = String.valueOf(randomID());
        try {
            ArrayList<String> worlds = new ArrayList<String>();
            player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",10,100,40);
            worlds.add(nickname + "1");
            // Генерация и параметры мира
            Bukkit.createWorld(new WorldCreator(worldname).type(WorldType.FLAT).generateStructures(false));
            Bukkit.getWorld(worldname).getWorldBorder().setSize(100);
            setGamerule(worldname,GameRule.DO_MOB_SPAWNING,false);
            setGamerule(worldname,GameRule.DO_DAYLIGHT_CYCLE,false);
            setGamerule(worldname,GameRule.MOB_GRIEFING,false);
            setGamerule(worldname,GameRule.DO_WEATHER_CYCLE,false);
            setGamerule(worldname,GameRule.SHOW_DEATH_MESSAGES,true);
            setGamerule(worldname,GameRule.DO_IMMEDIATE_RESPAWN,true);
            Bukkit.getWorld(worldname).setTime(0);
            createFile(worldname, player);
            player.teleport(Bukkit.getWorld(worldname).getSpawnLocation());
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
            for (int i = 0; i < worlds.size(); i++) {
                player.sendMessage(worlds.get(i));
            }
            // Попытка удалить все сущности с мира
            for (Entity entity : Bukkit.getWorld(worldname).getEntities()) {
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
        } catch(NullPointerException error) {
            player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
            player.sendMessage("");
            player.sendMessage("§cПроизошла критическая ошибка :(");
            player.sendMessage("§cКод ошибки: §lNullPointerException");
            player.sendMessage("");
        }
    }

    // Установка параметра мира Gamerule
    public static void setGamerule(String worldname, GameRule gameRule, boolean bool) {
        Bukkit.getWorld(worldname).setGameRule(gameRule,bool);
    }

    // Создать файл plugins\CreativeCoding\worlds\...yml
    public static void createFile(String worldname, Player player) {
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                // Создание и заполнение параметров
                worldfile.createSection("owner");
                worldfile.set("owner", player.getName());
                worldfile.createSection("title");
                worldfile.set("title", "§7Мир игрока §f" + player.getName());
                worldfile.createSection("description");
                worldfile.set("description", "§7Мой чудесный мир!");
                worldfile.createSection("icon");
                worldfile.set("icon", String.valueOf(Material.DIAMOND));
                // Сохранение файла (обязательное)
                worldfile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    // Генератор ID мира
    // Нужно сделать нормальную проверку!!!
    public static int randomID() {
        int min = 1;
        int max = 10000;
        int result = (int)(Math.random()*(max-min+1)+min);
        File folder = new File(plugin.getDataFolder() + "\\worlds\\");
        List<String> ids;
        for (File file : folder.listFiles()) {
         // Скорее всего проверка ни о чем, надо бы заменить?
            if (file.isFile()) {
                String ID = file.getName().replace(".yml","");
                if (String.valueOf(result).equals(ID)) {
                    randomID();
                }
            }
        }
        return result;
    }
}

