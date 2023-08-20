/*
 Creative TimePlay 2023

 Утилита для работы с файлами
 */

package timeplay.creativecoding.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.world.Plot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static timeplay.creativecoding.utils.ErrorUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.PlotManager.plots;
import static timeplay.creativecoding.world.PlotManager.unloadPlot;

public class FileUtils {

    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    // Создание файла конфига для нового мира
    public static void createWorldFile(String worldname, Player player) {
        File file = new File((Bukkit.getServer().getWorldContainer() + File.separator + worldname + File.separator), "settings.yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                // Создание и заполнение параметров
                worldfile.createSection("owner");
                worldfile.set("owner", player.getName());
                worldfile.createSection("name");
                worldfile.set("name", getLocaleMessage("creating-world.default-world-name").replace("%player%", player.getName()));
                worldfile.createSection("description");
                worldfile.set("description", getLocaleMessage("creating-world.default-world-description").replace("%player%", player.getName()));
                worldfile.createSection("icon");
                worldfile.set("icon", String.valueOf(Material.DIAMOND));
                worldfile.createSection("sharing");
                worldfile.set("sharing", String.valueOf(Plot.sharing.PUBLIC));
                worldfile.createSection("category");
                worldfile.set("category", String.valueOf(Plot.category.SANDBOX));
                // Сохранение файла (обязательно)
                worldfile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Загрузка файла локализации
    public static void loadLocales() {
        try {
            File folder = new File(plugin.getDataFolder() + File.separator + "locales" + File.separator);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder.getPath() + File.separator + plugin.getConfig().get("messages.locale")+".yml");
            if (!file.exists()) {
                plugin.saveResource("locales"+File.separator+"ru.yml",false);
                Object locale = "ru";
                plugin.getConfig().set("messages.locale",locale);
                plugin.saveConfig();
            }
            Bukkit.getLogger().info("Файл локализации загружен!");
        } catch (Exception error) {
            sendCriticalErrorMessage("Ошибка при загрузке файла локализации: " + error.getMessage());
        }
    }

    // Добавление миров в базу миров
    public static boolean loadWorlds() {
        try {
            File[] worldsFolder = getWorldsFolders(true);
            if (worldsFolder.length > 0) {
                for (File worldFolder : worldsFolder) {
                    String worldName = worldFolder.getPath().replace(Bukkit.getServer().getWorldContainer() + File.separator,"").replace("unloadedWorlds" + File.separator,"");
                    try {
                        Bukkit.getLogger().info("Загрузка мира с плотом " + worldName + "...");
                        Plot plot = new Plot(worldName);
                        if (!getPlotFolder(plot).getPath().contains("unloadedWorlds")) unloadPlot(plot);
                    } catch (NullPointerException error) {
                        sendCriticalErrorMessage("Ошибка при загрузке мира с плотом " + worldName + " : " + error.getMessage());
                    }
                }
                return true;
            } else {
                Bukkit.getLogger().info("Миров в папке нет, по-этому нет чего загружать ;(");
                return true;
            }
        } catch (Exception error) {
            sendCriticalErrorMessage("Ошибка при загрузке миров: " + error.getMessage());
            return false;
        }
    }

    // Получить папку, где лежит мир
    public static File getPlotFolder(Plot plot) {
        try {
            if (plot.isLoaded) {
                return new File(Bukkit.getServer().getWorldContainer() + File.separator + plot.worldName + File.separator);
            } else {
                return new File(Bukkit.getServer().getWorldContainer() + File.separator + "unloadedWorlds" + File.separator + plot.worldName + File.separator);
            }
        } catch (NullPointerException error) {
            sendPlotErrorMessage(plot,"Папка плота не обнаружена. " + error.getMessage());
            return null;
        }
    }

    // Получить конфиг плота в виде конфига yml
    public static FileConfiguration getPlotConfig(Plot plot) {
        try {
            File file = new File(getPlotFolder(plot), "settings.yml");
            return YamlConfiguration.loadConfiguration(file);
        } catch (NullPointerException error) {
            sendPlotErrorMessage(plot,"Конфиг settings.yml плота не обнаружен. " + error.getMessage());
            return null;
        }
    }

    // Получить конфиг плота в виде файла
    public static File getPlotConfigFile(Plot plot) {
        try {
            File file = new File((getPlotFolder(plot)),  "settings.yml");
            return file;
        } catch (NullPointerException error) {
            sendPlotErrorMessage(plot,"Файл settings.yml плота не обнаружен. " + error.getMessage());
            return null;
        }
    }

    // Получить ВСЕ папки миров
    public static File[] getWorldsFolders(boolean includeUnloadedWorlds) {
        ArrayList<File> worldsFolders = new ArrayList<>();
        for (File file : Bukkit.getServer().getWorldContainer().listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("plot")) worldsFolders.add(file);
        }
        if (includeUnloadedWorlds) {
            File unloadedWorldFolder = new File(Bukkit.getServer().getWorldContainer() + File.separator + "unloadedWorlds" + File.separator);
            if (!unloadedWorldFolder.exists()) unloadedWorldFolder.mkdirs();
            if (unloadedWorldFolder.listFiles().length > 0) {
                for (File file : unloadedWorldFolder.listFiles()) {
                    if (file.isDirectory() && file.getName().startsWith("plot")) {
                        worldsFolders.add(file);
                    }
                }
            }
        }
        return worldsFolders.toArray(new File[0]);
    }

    // Отгрузить миры
    public static boolean unloadWorlds() {
        try {
            File[] worldsFiles = getWorldsFolders(false);
            if (worldsFiles.length > 0) {
                for (File file : worldsFiles) {
                    String worldName = file.getPath().replace(Bukkit.getServer().getWorldContainer() + File.separator,"");
                        try {
                            Bukkit.getLogger().info("Отгрузка мира с плотом " + worldName + "...");
                            Bukkit.unloadWorld(worldName,true);
                            unloadWorldFolder(worldName,true);
                            for (Plot plot : plots) {
                                plots.remove(plot);
                            }
                        } catch (Exception error) {
                            Bukkit.getLogger().severe("Не удалось отгрузить мир " + worldName + ": " + error.getMessage());
                        }
                }
            } else {
                Bukkit.getLogger().info("Миров в папке нет, по-этому нет чего отгружать ;(");
            }
            return true;
        } catch (NullPointerException error) {
            sendCriticalErrorMessage("Ошибка при отгрузке миров: " + error.getMessage());
            return false;
        }
    }

    // Загрузить папку мира в директорию самого сервера
    public static boolean loadWorldFolder(String worldName, boolean removeUnloadedFolder) {
        Path serverPath = Bukkit.getServer().getWorldContainer().toPath();
        File unloadedWorldFolder = new File(serverPath + File.separator + "unloadedWorlds" + File.separator + worldName);
        File worldFolder = new File(serverPath + File.separator + worldName);

        if (copyFilesToDirectory(unloadedWorldFolder,worldFolder)) {
            if (!removeUnloadedFolder) return true;
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(unloadedWorldFolder);
                return true;
            } catch (IOException error) {
                return false;
            }
        } else {
            return false;
        }
    }

    // Отгрузить папку мира в директорию отгруженных миров
    public static boolean unloadWorldFolder(String worldName, boolean removeWorldFolder) {
        Path serverPath = Bukkit.getServer().getWorldContainer().toPath();
        File worldFolder = new File(serverPath + File.separator + worldName);
        File unloadedWorldsFolder = new File(serverPath + File.separator + "unloadedWorlds" + File.separator + worldName);

        if (copyFilesToDirectory(worldFolder,unloadedWorldsFolder)) {
            if (!removeWorldFolder) return true;
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(worldFolder);
                return true;
            } catch (IOException error) {
                return false;
            }
        } else {
            return false;
        }
    }

    // Перенести папку готового мира в директорию сервера
    public static boolean copyTemplateToWorlds(String templateName, String worldName) {
        File templateWorldFolder = new File(Bukkit.getPluginManager().getPlugin("ConfigEvents").getDataFolder() + File.separator + "worlds" + File.separator + templateName);
        File worldsFolder = new File(Bukkit.getServer().getWorldContainer().toPath() + File.separator + worldName);
        return copyFilesToDirectory(templateWorldFolder,worldsFolder);
    }

    // Перенести содержимое из одной папки в другую
    public static boolean copyFilesToDirectory(File input, File output) {
        try {
            output.mkdirs();
            for (File worldFile : input.listFiles()) {
                if (worldFile.isDirectory()) org.apache.commons.io.FileUtils.copyDirectoryToDirectory(worldFile,output);
                else org.apache.commons.io.FileUtils.copyFileToDirectory(worldFile,output);
            }
            return true;
        } catch (IOException error) {
            Bukkit.getLogger().severe("При попытке скопировать файл произошла ошибка IOException. Файл откуда: " + input.getPath() + " Место куда: " + output.getPath() + " Ошибка: " + error.getMessage());
            return false;
        }
    }

    // Удаление папки мира
    public static boolean deleteWorld(File path) {
        if (path.exists()) {
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(path);
                return true;
            } catch (Exception error) {
                sendCriticalErrorMessage("Невозможно удалить папку мира " + path.getPath());
                return false;
            }
        } else {
            return false;
        }
    }

}
