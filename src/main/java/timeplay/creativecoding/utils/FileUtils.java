/*
 Creative TimePlay 2023

 Утилита для работы с файлами
 */

package timeplay.creativecoding.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.world.Plot;

import java.io.File;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.ErrorUtils.sendPlotErrorMessage;

public class FileUtils {

    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    public static void loadWorlds() {
        File folder = new File(plugin.getDataFolder() + File.separator + "worlds" + File.separator);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles();
        try {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.toString().replace(plugin.getDataFolder() + "\\worlds\\", "").replace(".yml", "");
                    try {
                        Bukkit.getLogger().info("Загрузка мира с плотом " + fileName);
                        new Plot(fileName);
                    } catch (NullPointerException error) {
                        Bukkit.getLogger().warning("Ошибка при загрузке мира с плотом " + fileName + " : " + error.getMessage());
                    }
                }
            }
        } catch (NullPointerException error) {
            sendCriticalErrorMessage("Ошибка при загрузке миров: " + error.getMessage());
        }
    }

    public static FileConfiguration getPlotConfig(Plot plot) {
        try {
            File file = new File((plugin.getDataFolder() + File.separator + "worlds" + File.separator), plot.world.getName() + ".yml");
            return YamlConfiguration.loadConfiguration(file);
        } catch (NullPointerException error) {
            sendPlotErrorMessage(plot,"Файл конфига плота не обнаружен. " + error.getMessage());
            return null;
        }
    }

    // Удаление папки мира
    public static boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteWorld(file);
                } else {
                    file.delete();
                }
            }
        }
        return(path.delete() );
    }

    // Удаление файла конфигурации мира plugins\CreativeCoding\worlds\...yml
    public static void deleteWorldConfig(String worldName) {
        File file = new File((plugin.getDataFolder() + File.separator + "worlds" + File.separator), worldName + ".yml");
        if(file.exists()) {
            file.delete();
        }
    }

}
