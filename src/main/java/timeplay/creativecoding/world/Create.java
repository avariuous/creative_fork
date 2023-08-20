/*
 Creative TimePlay 2023

 Создание мира
 */

package timeplay.creativecoding.world;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;

import java.io.File;

import static timeplay.creativecoding.utils.FileUtils.getWorldsFolders;

public class Create {

    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    // Установка параметра мира Gamerule
    public static void setGamerule(String worldname, GameRule gameRule, boolean bool) {
        Bukkit.getWorld(worldname).setGameRule(gameRule,bool);
    }

    // Генерация айдишника для мира
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

