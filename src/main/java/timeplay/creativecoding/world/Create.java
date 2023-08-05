/*
 Creative TimePlay 2023

 Создание мира
 */

package timeplay.creativecoding.world;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;

import java.io.File;
import java.io.IOException;

import static timeplay.creativecoding.world.Delete.plugin;

public class Create {
    public static void CreateWorld(Player player, Plot plot) {

        String nickname = player.getName();
        String worldname = String.valueOf(generateWorldID());

    }

    // Установка параметра мира Gamerule
    public static void setGamerule(String worldname, GameRule gameRule, boolean bool) {
        Bukkit.getWorld(worldname).setGameRule(gameRule,bool);
    }

    // Создать файл plugins\CreativeCoding\worlds\...yml
    public static void createFile(String worldname, Player player) {
        File file = new File((plugin.getDataFolder() + File.separator + "worlds" + File.separator), worldname + ".yml");
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

    // Генерация айдишника для мира
    public static int generateWorldID() {
        File folder = new File(plugin.getDataFolder() + File.separator + "worlds" + File.separator);
        String[] files = folder.list();

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
            File file = new File(plugin.getDataFolder() + File.separator + "worlds" + File.separator + newWorldID + ".yml");
            if (!file.exists()) {
                plugin.getConfig().set("last-world-id",newWorldID);
                plugin.saveConfig();
                return newWorldID;
            }
        }
    }
}

