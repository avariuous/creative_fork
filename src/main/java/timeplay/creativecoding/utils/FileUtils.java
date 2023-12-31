/*
 Creative TimePlay 2023

 Утилита для работы с файлами
 */

package timeplay.creativecoding.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import timeplay.creativecoding.plots.DevPlot;
import timeplay.creativecoding.plots.Plot;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static timeplay.creativecoding.Main.teleportToLobby;
import static timeplay.creativecoding.utils.ErrorUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.plots.PlotManager.plots;

public class FileUtils {

    @NotNull
    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    // Создание файла конфига для нового мира
    public static void createWorldFile(String worldName, Player player) {
        File file = new File((Bukkit.getServer().getWorldContainer() + File.separator + worldName + File.separator), "settings.yml");
        final FileConfiguration worldFile = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                if (!file.createNewFile()) sendCriticalErrorMessage("Файл конфига плота " + worldName + " уже существует, возможно это дубликат мира?");
                // Создание и заполнение параметров
                worldFile.createSection("owner");
                worldFile.set("owner", player.getName());
                worldFile.createSection("owner-group");
                worldFile.set("owner-group",PlayerUtils.getGroup(player));
                worldFile.createSection("world");
                worldFile.set("world",worldName);
                worldFile.createSection("creation-time");
                worldFile.set("creation-time",System.currentTimeMillis());
                worldFile.createSection("last-activity-time");
                worldFile.set("last-activity-time",System.currentTimeMillis());
                worldFile.createSection("name");
                worldFile.set("name", getLocaleMessage("creating-world.default-world-name").replace("%player%", player.getName()));
                worldFile.createSection("description");
                worldFile.set("description", getLocaleMessage("creating-world.default-world-description").replace("%player%", player.getName()));
                worldFile.createSection("icon");
                worldFile.set("icon", String.valueOf(Material.DIAMOND));
                worldFile.createSection("sharing");
                worldFile.set("sharing", String.valueOf(Plot.Sharing.PUBLIC));
                worldFile.createSection("category");
                worldFile.set("category", String.valueOf(Plot.Category.SANDBOX));
                worldFile.createSection("customID");
                worldFile.set("customID",worldName.replace("plot",""));
                // Trusted - доверенные игроки, могут строить или кодить когда нет владельца
                // Not-trusted - не доверенные игроки, могут строить или кодить только когда владелец в мире.
                worldFile.createSection("players.unique");
                worldFile.set("players.unique", new ArrayList<String>());
                worldFile.createSection("players.liked");
                worldFile.set("players.liked", new ArrayList<String>());
                worldFile.createSection("players.builders.trusted");
                worldFile.set("players.builders.trusted", new ArrayList<String>());
                worldFile.createSection("players.builders.not-trusted");
                worldFile.set("players.builders.not-trusted", new ArrayList<String>());
                worldFile.createSection("players.developers.trusted");
                worldFile.set("players.developers.trusted", new ArrayList<String>());
                worldFile.createSection("players.developers.not-trusted");
                worldFile.set("players.developers.not-trusted", new ArrayList<String>());
                worldFile.createSection("players.whitelist");
                worldFile.set("players.whitelist", new ArrayList<String>());
                worldFile.createSection("players.blacklist");
                worldFile.set("players.blacklist", new ArrayList<String>());
                // Флаги плота
                worldFile.createSection("flags");
                Map<String,Integer> flags = new HashMap<>();
                flags.put("player-damage",1);
                flags.put("join-messages",1);
                flags.put("day-cycle",1);
                flags.put("fire-spread",1);
                worldFile.set("flags",flags);
                // Сохранение файла (обязательно)
                worldFile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createCodeScript(String path, String worldName) {

        File file = new File(path + File.separator, "codeScript.yml");
        final FileConfiguration worldFile = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                if (!file.createNewFile()) sendCriticalErrorMessage("Файл кодинга плота " + worldName + " уже существует, возможно это дубликат мира?");
                worldFile.createSection("world");
                worldFile.set("world",worldName);
                worldFile.createSection("creation-time");
                worldFile.set("creation-time",System.currentTimeMillis());
                worldFile.createSection("last-activity-time");
                worldFile.set("last-activity-time",System.currentTimeMillis());
                worldFile.createSection("code");
                worldFile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Загрузка файла локализации
    public static void loadLocales() {
        Bukkit.getLogger().info("Loading Creative localization file...");
        try {
            File folder = new File(plugin.getDataFolder() + File.separator + "locales" + File.separator);
            if (!folder.exists()) if (!folder.mkdirs()) sendCriticalErrorMessage("Directory " + folder.getPath() + " could not be created");
            File file = new File(folder.getPath() + File.separator + plugin.getConfig().get("messages.locale")+".yml");
            if (!file.exists()) {
                plugin.saveResource("locales" + File.separator + "en.yml",false);
                plugin.saveResource("locales" + File.separator + "ru.yml",false);
                Object locale = "en";
                plugin.getConfig().set("messages.locale",locale);
                plugin.saveConfig();
            }
            Bukkit.getLogger().info("Loaded Creative localization file...");
        } catch (Exception error) {
            sendCriticalErrorMessage("Error while loading Creative localization file : " + error.getMessage());
        }
    }

    // Сброс файла локализации
    public static void resetLocales() {
        Bukkit.getLogger().info("Resetting Creative localization file...");
        try {
            File folder = new File(plugin.getDataFolder() + File.separator + "locales" + File.separator);
            if (!folder.exists()) if (!folder.mkdirs()) sendCriticalErrorMessage("Directory " + folder.getPath() + " could not be created");
            File file = new File(folder.getPath() + File.separator + plugin.getConfig().get("messages.locale")+".yml");
            if (file.exists()) {
                if (file.delete()) plugin.saveResource("locales"+File.separator+"en.yml",false);
                Object locale = "en";
                plugin.getConfig().set("messages.locale",locale);
                plugin.saveConfig();
            }
            Bukkit.getLogger().info("Reset Creative localization file!");
        } catch (Exception error) {
            sendCriticalErrorMessage("Error while loading Creative localization file : " + error.getMessage());
        }
    }

    // Загрузка шаблонов миров
    /*public static void loadTemplates() {
        Bukkit.getLogger().info("Loading Creative world templates...");
        try {
            File folder = new File(plugin.getDataFolder() + File.separator + "templates" + File.separator);
            if (!folder.exists()) if (!folder.mkdirs()) sendCriticalErrorMessage("Directory " + folder.getPath() + " could not be created");
            File devWorldTemplate = new File(folder,"devWorld.zip");
            if (!devWorldTemplate.exists()) {
                plugin.saveResource("templates" + File.separator + "devWorld.zip",true);
                byte[] buffer = new byte[1024];
                try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(devWorldTemplate.toPath()))) {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    while (zipEntry != null) {
                        String entryPath = folder + File.separator + zipEntry.getName();
                        if (zipEntry.isDirectory()) {
                            new File(entryPath).mkdirs();
                        } else {
                            try (FileOutputStream outputStream = new FileOutputStream(entryPath)) {
                                int bytesRead;
                                while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                            }
                        }
                        zipEntry = zipInputStream.getNextEntry();
                    }
                    System.out.println("Extraction complete.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Bukkit.getLogger().info("Loaded Creative world templates...");
        } catch (Exception error) {
            sendCriticalErrorMessage("Error while loading Creative world templates: " + error.getMessage());
        }
    }*/

    // Добавление плотов в базу плотов
    public static boolean loadPlots() {
        try {
            File[] plotsFolders = getWorldsFolders(true);
            // Если папка миров существует
            if (plotsFolders.length > 0) {
                for (File plotFolder : plotsFolders) {
                    String worldName = plotFolder.getPath().replace(Bukkit.getServer().getWorldContainer() + File.separator,"").replace("unloadedWorlds" + File.separator,"");
                    // Отгруженные миры добавляются в базу
                    if (plotFolder.getPath().contains("unloadedWorlds")) {
                        Bukkit.getLogger().info("Adding unloaded world " + worldName + " to base...");
                        if (!worldName.endsWith("dev")) new Plot(worldName);
                    // Если мир находился в директории сервера, то его
                    // переносят в папку отгруженных миров и добавляют в базу
                    } else {
                        Bukkit.getLogger().info("Moving loaded world " + worldName + " to unloadedWorlds folder...");
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            for (Player player : world.getPlayers()) {
                                teleportToLobby(player);
                            }
                        }
                        unloadWorldFolder(worldName,true);
                        Bukkit.getLogger().info("Adding unloaded world " + worldName + " to base...");
                        if (!worldName.endsWith("dev")) new Plot(worldName);
                    }
                }
            } else {
                Bukkit.getLogger().info("No loaded worlds been detected ;(");
            }
            return true;
        } catch (Exception error) {
            sendCriticalErrorMessage("An error has occurred while loading worlds... " + error.getMessage());
            return false;
        }
    }

    // Получить папку, где лежит плот
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

    public static File getDevPlotFolder(DevPlot devPlot) {
        try {
            if (devPlot.isLoaded) {
                return new File(Bukkit.getServer().getWorldContainer() + File.separator + devPlot.worldName + File.separator);
            } else {
                return new File(Bukkit.getServer().getWorldContainer() + File.separator + "unloadedWorlds" + File.separator + devPlot.worldName + File.separator);
            }
        } catch (NullPointerException error) {
            sendPlotErrorMessage(devPlot.linkedPlot,"Папка плота разработчика не обнаружена. " + error.getMessage());
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
            return new File((getPlotFolder(plot)),"settings.yml");
        } catch (NullPointerException error) {
            sendPlotErrorMessage(plot,"Файл settings.yml плота не обнаружен. " + error.getMessage());
            return null;
        }
    }

    public static File getPlotScriptFile(Plot plot) {
        File scriptFile = new File((getPlotFolder(plot)),"codeScript.yml");
        if (scriptFile.exists()) return scriptFile;
        else {
            createCodeScript(getPlotFolder(plot).getPath(), plot.worldName);
            return getPlotScriptFile(plot);
        }
    }

    public static YamlConfiguration getPlotScriptFileConfig(Plot plot) {
        File scriptFile = new File((getPlotFolder(plot)),"codeScript.yml");
        if (scriptFile.exists()) return YamlConfiguration.loadConfiguration(scriptFile);
        else {
            createCodeScript(getPlotFolder(plot).getPath(), plot.worldName);
            return getPlotScriptFileConfig(plot);
        }
    }


    // Получить папки миров
    public static File[] getWorldsFolders(boolean includeUnloadedWorlds) {

        ArrayList<File> worldsFolders = new ArrayList<>();
        File serverDirectory = Bukkit.getServer().getWorldContainer();
        File[] serverDirectoryFiles = serverDirectory.listFiles();

        // Получаем загруженные миры
        if (serverDirectoryFiles != null) {
            for (File file : serverDirectoryFiles) {
                if (file.isDirectory() && file.getName().startsWith("plot")) worldsFolders.add(file);
            }
        } else {
            sendCriticalErrorMessage("При попытке получить файлы с директории сервера они оказались null.");
        }

        // Получаем отгруженные миры
        if (includeUnloadedWorlds) {
            File unloadedWorldsFolder = new File(serverDirectory + File.separator + "unloadedWorlds" + File.separator);
            File[] unloadedWorlds = unloadedWorldsFolder.listFiles();

            if (unloadedWorlds != null) {
                File unloadedWorldFolder = new File(serverDirectory + File.separator + "unloadedWorlds" + File.separator);
                if (!unloadedWorldFolder.exists()) if (!unloadedWorldFolder.mkdirs()) sendCriticalErrorMessage("Не получилось создать директорию " + unloadedWorldFolder.getPath());
                for (File file : unloadedWorlds) {
                    if (file.isDirectory() && file.getName().startsWith("plot")) {
                        worldsFolders.add(file);
                    }
                }
            } else {
                sendCriticalErrorMessage("При попытке получить папки отгруженных миров они оказались null.");
            }
        }
        return worldsFolders.toArray(new File[0]);
    }

    /**
     Отгрузить миры плотов в папку отгруженных плотов.
     */
    public static void unloadPlots() {
        try {
            File[] worldsFiles = getWorldsFolders(false);
            if (worldsFiles.length > 0) {
                for (File file : worldsFiles) {
                    String worldName = file.getPath().replace(Bukkit.getServer().getWorldContainer() + File.separator,"");
                        try {
                            Bukkit.getLogger().info("Unloading Creative world " + worldName + "...");
                            Bukkit.unloadWorld(worldName,true);
                            unloadWorldFolder(worldName,true);
                        } catch (Exception error) {
                            Bukkit.getLogger().severe("An error has occurred when unloading world " + worldName + ": " + error.getMessage());
                        }
                }
            } else {
                Bukkit.getLogger().info("No worlds been detected ;(");
            }
            plots.clear();
        } catch (NullPointerException error) {
            sendCriticalErrorMessage("Error while unloading worls: " + error.getMessage());
        }
    }

    /**
     Загрузить мир в папку миров. Возвращает true если мир успешно загружен, false
     */
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
    public static void unloadWorldFolder(String worldName, boolean removeWorldFolder) {
        Path serverPath = Bukkit.getServer().getWorldContainer().toPath();
        File worldFolder = new File(serverPath + File.separator + worldName);
        File unloadedWorldsFolder = new File(serverPath + File.separator + "unloadedWorlds" + File.separator + worldName);

        if (copyFilesToDirectory(worldFolder,unloadedWorldsFolder)) {
            if (!removeWorldFolder) return;
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(worldFolder);
            } catch (IOException error) {
                sendCriticalErrorMessage("При попытке удалить папку мира произошла ошибка " + error.getMessage());
            }
        }
    }

    // Перенести папку готового мира в директорию сервера
    /*public static boolean copyTemplateToWorlds(String templateName, String worldName) {
        File templateWorldFolder = new File(plugin.getDataFolder() + File.separator + "worlds" + File.separator + templateName);
        File worldsFolder = new File(Bukkit.getServer().getWorldContainer().toPath() + File.separator + worldName);
        return copyFilesToDirectory(templateWorldFolder,worldsFolder);
    }*/

    /**
     Копирование файлов в директорию.
     */
    public static boolean copyFilesToDirectory(File input, File output) {
        try {
            File[] inputFiles = input.listFiles();
            if (!output.exists()) {
                if (!output.mkdirs()) sendCriticalErrorMessage("Не удалось создать директорию " + output.getPath());
            }
            if (inputFiles != null) {
                for (File worldFile : inputFiles) {
                    if (worldFile.isDirectory()) org.apache.commons.io.FileUtils.copyDirectoryToDirectory(worldFile,output);
                    else org.apache.commons.io.FileUtils.copyFileToDirectory(worldFile,output);
                }
            }
            return true;
        } catch (IOException error) {
            Bukkit.getLogger().severe("При попытке скопировать файл произошла ошибка IOException. Файл откуда: " + input.getPath() + " Место куда: " + output.getPath() + " Ошибка: " + error.getMessage());
            return false;
        }
    }

    // Удаление папки мира
    public static void deleteWorld(File path) {
        if (path.exists()) {
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(path);
            } catch (Exception error) {
                sendCriticalErrorMessage("Невозможно удалить папку мира " + path.getPath());
            }
        }
    }

    // Установка параметра в конфиге плота
    public static void setPlotConfigParameter(Plot plot, String parameterPath, long parameterValue) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        File plotConfigFile = getPlotConfigFile(plot);
        if (plotConfig != null && plotConfigFile != null) {
            plotConfig.createSection(parameterPath);
            plotConfig.set(parameterPath,String.valueOf(parameterValue));
            try {
                plotConfig.save(plotConfigFile);
            } catch (IOException error) {
                sendCriticalErrorMessage("При сохранении конфига плота в файл произошла ошибка " + error.getMessage());
            }
        } else {
            sendPlotErrorMessage(plot,"Не удалось получить файл конфига плота либо сам конфиг");
        }
    }

    public static void setPlotConfigParameter(Plot plot, String parameterPath, int parameterValue) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        File plotConfigFile = getPlotConfigFile(plot);
        if (plotConfig != null && plotConfigFile != null) {
            plotConfig.createSection(parameterPath);
            plotConfig.set(parameterPath,String.valueOf(parameterValue));
            try {
                plotConfig.save(plotConfigFile);
            } catch (IOException error) {
                sendCriticalErrorMessage("При сохранении конфига плота в файл произошла ошибка " + error.getMessage());
            }
        } else {
            sendPlotErrorMessage(plot,"Не удалось получить файл конфига плота либо сам конфиг");
        }
    }

    public static void setPlotConfigParameter(Plot plot, String parameterPath, Object parameterValue) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        File plotConfigFile = getPlotConfigFile(plot);
        if (plotConfig != null && plotConfigFile != null) {
            plotConfig.createSection(parameterPath);
            plotConfig.set(parameterPath,String.valueOf(parameterValue));
            try {
                plotConfig.save(plotConfigFile);
            } catch (IOException error) {
                sendCriticalErrorMessage("При сохранении конфига плота в файл произошла ошибка " + error.getMessage());
            }
        } else {
            sendPlotErrorMessage(plot,"Не удалось получить файл конфига плота либо сам конфиг");
        }
    }

    public static void setPlotConfigParameter(Plot plot, String parameterPath, String parameterValue) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        File plotConfigFile = getPlotConfigFile(plot);
        if (plotConfig != null && plotConfigFile != null) {
            plotConfig.createSection(parameterPath);
            plotConfig.set(parameterPath,parameterValue);
            try {
                plotConfig.save(plotConfigFile);
            } catch (IOException error) {
                sendCriticalErrorMessage("При сохранении конфига плота в файл произошла ошибка " + error.getMessage());
            }
        } else {
            sendPlotErrorMessage(plot,"Не удалось получить файл конфига плота либо сам конфиг");
        }
    }

    public static void setPlotConfigParameter(Plot plot, String parameterPath, List<String> parameterValue) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        File plotConfigFile = getPlotConfigFile(plot);
        if (plotConfig != null && plotConfigFile != null) {
            plotConfig.createSection(parameterPath);
            plotConfig.set(parameterPath,parameterValue);
            try {
                plotConfig.save(plotConfigFile);
            } catch (IOException error) {
                sendCriticalErrorMessage("При сохранении конфига плота в файл произошла ошибка " + error.getMessage());
            }
        } else {
            sendPlotErrorMessage(plot,"Не удалось получить файл конфига плота либо сам конфиг");
        }
    }



    public static void setPlotConfigParameter(Plot plot, String parameterPath, Map<String, String> parameterValue) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        File plotConfigFile = getPlotConfigFile(plot);
        if (plotConfig != null && plotConfigFile != null) {
            plotConfig.createSection(parameterPath);
            plotConfig.set(parameterPath,parameterValue);
            try {
                plotConfig.save(plotConfigFile);
            } catch (IOException error) {
                sendCriticalErrorMessage("При сохранении конфига плота в файл произошла ошибка " + error.getMessage());
            }
        } else {
            sendPlotErrorMessage(plot,"Не удалось получить файл конфига плота либо сам конфиг");
        }
    }

    // Получить список никнеймов игроков с конфига плота
    public static List<String> getPlayersFromPlotConfig(Plot plot, Plot.PlayersType type) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        if (plotConfig != null) {
            return new ArrayList<>(plotConfig.getStringList(type.getPath()));
        } else {
            sendCriticalErrorMessage("При попытке получить список игроков из файла конфига плота " + plot.worldName + " произошла ошибка. Тип: " + type.toString() + " Конфиг плота оказался null.");
            return new ArrayList<>();
        }
    }

    // Получить флаги плота из конфига плота
    public static Map<String, Integer> getPlotFlagsFromPlotConfig(Plot plot) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        Map<String, Integer> flags = new HashMap<>();
        if (plotConfig != null) {
            ConfigurationSection plotConfigFlagKeys = plotConfig.getConfigurationSection("flags");
            if (plotConfigFlagKeys != null) {
                plotConfigFlagKeys.getKeys(false).forEach(flag -> {
                    String configFlagValue = plotConfig.getString("flags." + flag);
                    if (configFlagValue == null) return;
                    int flagValue = Integer.parseInt(configFlagValue);
                    flags.put(flag, flagValue);
                });
            }
            return flags;
        } else {
            sendCriticalErrorMessage("При попытке получить список игроков из файла конфига плота " + plot.worldName + " произошла ошибка. Конфиг плота оказался null.");
            flags.put("No flags detected", 0);
            return flags;
        }
    }

    // Добавить в список из конфига плота никнейм игрока
    public static boolean addPlayerToListInPlotConfig(Plot plot, String nickname, Plot.PlayersType type) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        if (plotConfig != null) {
            List<String> newPlayersPlotConfigList = plotConfig.getStringList(type.getPath());
            newPlayersPlotConfigList.add(nickname);
            setPlotConfigParameter(plot,type.getPath(),newPlayersPlotConfigList);
            return true;
        } else {
            sendCriticalErrorMessage("При попытке добавить игрока в список файла конфига плота " + plot.worldName + " произошла ошибка. Никнейм: " + nickname + " Тип: " + type.toString() + " Конфиг плота оказался null.");
            return false;
        }
    }

    // Удалить из списка конфига плота никнейм игрока
    public static boolean removePlayerFromListInPlotConfig(Plot plot, String nickname, Plot.PlayersType type) {
        FileConfiguration plotConfig = getPlotConfig(plot);
        if (plotConfig != null) {
            List<String> newPlayersPlotConfigList = plotConfig.getStringList(type.getPath());
            newPlayersPlotConfigList.remove(nickname);
            setPlotConfigParameter(plot,type.getPath(),newPlayersPlotConfigList);
            return true;
        } else {
            sendCriticalErrorMessage("При попытке убрать игрока из списка файла конфига плота " + plot.worldName + " произошла ошибка. Никнейм: " + nickname + " Тип: " + type.toString() + " Конфиг плота оказался null.");
            return false;
        }
    }

}
