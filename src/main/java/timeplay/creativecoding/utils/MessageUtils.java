/*
 Creative TimePlay 2023

 Утилита для получения сообщения из файла локализации
 */

package timeplay.creativecoding.utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.plots.Plot;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static timeplay.creativecoding.utils.ErrorUtils.sendWarningErrorMessage;

public class MessageUtils {


    @NotNull private static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    private static String getPrefix() {
        String prefix = plugin.getConfig().getString("messages.prefix");
        if (prefix == null || prefix.equalsIgnoreCase("null")) {
            prefix = ChatColor.translateAlternateColorCodes('&',"&6 Worlds &8| &f");
            plugin.getConfig().set("messages.prefix","&6 Worlds &8| &f");
            plugin.saveConfig();
            return prefix;
        } else {
            return ChatColor.translateAlternateColorCodes('&',prefix);
        }
    }

    private static String getCreativeChatPrefix() {
        String prefix = plugin.getConfig().getString("messages.cc-prefix");
        if (prefix == null || prefix.equalsIgnoreCase("null")) {
            prefix = ChatColor.translateAlternateColorCodes('&',"&6 Creative Chat &8| &7");
            plugin.getConfig().set("messages.cc-prefix","&6 Creative Chat &8| &7");
            plugin.reloadConfig();
            return prefix;
        } else {
            return ChatColor.translateAlternateColorCodes('&',prefix);
        }
    }

    private static String getLanguage() {
        Object language = plugin.getConfig().get("messages.locale");
        if (language != null) {
            return String.valueOf(language);
        } else {
            Object defaultLanguage = "en";
            plugin.getConfig().set("messages.locale",defaultLanguage);
            plugin.saveResource("locales" + File.separator + "en.yml",false);
            plugin.saveResource("locales" + File.separator + "ru.yml",false);
            plugin.reloadConfig();
            return "en";
        }
    }

    private static File getLocalizationFile() {
        File localizationFile = new File((plugin.getDataFolder() + File.separator + "locales" + File.separator), getLanguage() + ".yml");
        if (localizationFile.exists()) {
            return localizationFile;
        } else {
            String defaultLanguage = getLanguage().equalsIgnoreCase("ru") ? "ru" : "en";
            plugin.getConfig().set("messages.locale",defaultLanguage);
            plugin.saveResource("locales" + File.separator + "en.yml",false);
            plugin.saveResource("locales" + File.separator + "ru.yml",false);
            plugin.reloadConfig();
            return new File((plugin.getDataFolder() + File.separator + "locales" + File.separator),  defaultLanguage + ".yml");
        }
    }

    private static FileConfiguration getLocalization() {
        return YamlConfiguration.loadConfiguration(getLocalizationFile());
    }

    public static String getPathFromMessage(String partOfPath, String message) {
        for (String line : getLocalization().getKeys(true)) {
            if (line.startsWith(partOfPath)) {
                if (getLocaleMessage(line).equalsIgnoreCase(message)) {
                    return line;
                }
            }
        }
        return null;
    }

    public static String getLocaleMessage(String messageID) {
        String originalMessage = getLocalization().getString(messageID);
        if (originalMessage == null || originalMessage.equalsIgnoreCase("null")) {
            sendWarningErrorMessage("Not found " + messageID + " in localization file!");
            return "§6 Error §8| §fNot found §6" + messageID + "§f! Administration of server needs to fill that line in §6locales"+File.separator+getLanguage()+".yml";
        } else {
            return ChatColor.translateAlternateColorCodes('&',originalMessage.replace("%prefix%",getPrefix()).replace("%cc-prefix%",getCreativeChatPrefix()));
        }
    }

    public static String getLocaleMessage(String messageID, Player player) {
        String originalMessage = getLocalization().getString(messageID);
        if (originalMessage == null || originalMessage.equalsIgnoreCase("null")) {
            sendWarningErrorMessage("Not found " + messageID + " in localization file!");
            return "§6 Error §8| §fNot found §6" + messageID + "§f! Administration of server needs to fill that line in §6locales"+File.separator+getLanguage()+".yml";
        } else {
            return ChatColor.translateAlternateColorCodes('&',parsePAPI(Bukkit.getOfflinePlayer(player.getName()),originalMessage.replace("%prefix%",getPrefix()).replace("%cc-prefix%",getCreativeChatPrefix()).replace("%player%",player.getName())));
        }
    }

    public static String getLocaleMessage(String messageID, boolean returnDetailedError) {
        String originalMessage = getLocalization().getString(messageID);
        if (originalMessage == null || originalMessage.equalsIgnoreCase("null")) {
            sendWarningErrorMessage("Not found " + messageID + " in localization file!");
            if (returnDetailedError) {
                return "§6 Error §8| §fNot found §6" + messageID + "§f! Administration of server needs to fill that line in §6locales"+File.separator+getLanguage()+".yml";
            } else {
                return messageID;
            }
        } else {
            return ChatColor.translateAlternateColorCodes('&',originalMessage.replace("%prefix%",getPrefix()).replace("%cc-prefix%",getCreativeChatPrefix()));
        }
    }

    public static String getLocaleItemName(String nameID) {
        String originalName = getLocalization().getString(nameID);
        if (originalName == null || originalName.equalsIgnoreCase("null")) {
            sendWarningErrorMessage("Not found item name " + nameID + " in localization file!");
            return "§fNot found: " + nameID;
        } else {
            if (originalName.length() > 50) originalName = originalName.substring(0,50);
            return ChatColor.translateAlternateColorCodes('&',originalName.replace("%prefix%",getPrefix()).replace("%cc-prefix%",getCreativeChatPrefix()));
        }
    }

    public static List<String> getLocaleItemDescription(String descriptionID) {
        List<String> originalDescription = getLocalization().getStringList(descriptionID);
        List<String> parsedDescription = new ArrayList<>();
        if (originalDescription.size() < 1) {
            sendWarningErrorMessage("Not found item description " + descriptionID);
            parsedDescription.add("§6Not found item description");
            parsedDescription.add("§6" + descriptionID);
            parsedDescription.add("§fPlease send this to server administration!");
            parsedDescription.add("§f They need to fill this line in ");
            parsedDescription.add("§f localization file: locales" + File.separator + getLanguage() + ".yml");
        } else {
            for (String descriptionLine : originalDescription) {
                parsedDescription.add(ChatColor.translateAlternateColorCodes('&',descriptionLine.replace("%prefix%",getPrefix()).replace("%cc-prefix%",getCreativeChatPrefix())));
            }
        }
        return parsedDescription;
    }

    public static String getElapsedTime(long currentTime, long oldTime) {

        String elapsedTime = "";

        long elapsedTimeInSeconds = (currentTime - oldTime) / 1000;
        long elapsedTimeInMinutes = elapsedTimeInSeconds / 60;
        long elapsedTimeInHours = elapsedTimeInMinutes / 60;
        long elapsedTimeInDays = elapsedTimeInHours / 24;

        elapsedTimeInSeconds %= 60;
        elapsedTimeInMinutes %= 60;
        elapsedTimeInHours %= 24;

        if (elapsedTimeInDays > 0) elapsedTime = elapsedTime.concat(elapsedTimeInDays + " " + getLocaleMessage("time.days",false) + " ");
        if (elapsedTimeInHours > 0) elapsedTime = elapsedTime.concat(elapsedTimeInHours + " "+ getLocaleMessage("time.hours",false) +" ");
        if (elapsedTimeInMinutes > 0) elapsedTime = elapsedTime.concat(elapsedTimeInMinutes + " "+ getLocaleMessage("time.minutes",false) +" ");
        if (elapsedTimeInSeconds > 0) elapsedTime = elapsedTime.concat(elapsedTimeInSeconds + " "+ getLocaleMessage("time.seconds",false) +" ");
        if ((currentTime - oldTime) < 1000) elapsedTime = getLocaleMessage("time.less-second",false) + " ";

        return elapsedTime + getLocaleMessage("time.ago",false);

    }

    static Map<Plot,Long> messagesOnce = new HashMap<>();
    // Если вызывается несколько раз, то сработает только один
    public static void sendMessageOnce(Plot plot, String message, int onceInSeconds) {

        long currentTime = System.currentTimeMillis();

        if (messagesOnce.get(plot) != null) {
            long timeInMap = messagesOnce.get(plot);
            long elapsedTime = currentTime-timeInMap;
            long elapsedSeconds = elapsedTime/1000;
            if (elapsedSeconds < onceInSeconds) return;
        }

        for (Player player : plot.getPlayers()) {
            player.sendMessage(message);
        }
        messagesOnce.put(plot,currentTime);

    }

    public static void sendMessageOnce(Plot plot, TextComponent message, int onceInSeconds) {

        long currentTime = System.currentTimeMillis();

        if (messagesOnce.get(plot) != null) {
            long timeInMap = messagesOnce.get(plot);
            long elapsedTime = currentTime-timeInMap;
            long elapsedSeconds = elapsedTime/1000;
            if (elapsedSeconds < onceInSeconds) return;
        }

        for (Player player : plot.getPlayers()) {
            player.sendMessage(message);
        }
        messagesOnce.put(plot,currentTime);

    }


    public static String parsePlotLines(Plot plot, String string) {
        String plotReputation = String.valueOf(plot.plotReputation);
        if (plot.plotReputation >= 1) plotReputation = "§a+" + plotReputation;
        else if (plot.plotReputation <= -1) plotReputation = "§c" + plotReputation;
        else plotReputation = "§e" + plotReputation;
        return parsePAPI(Bukkit.getOfflinePlayer(plot.owner),string.replace("%plotName%",plot.plotName).replace("%plotOnline%",String.valueOf(plot.getOnline())).replace("%plotOwner%",plot.owner).replace("%plotID%",plot.worldID).replace("%plotCategory%",plot.plotCategory.getName()).replace("%plotUniques%",String.valueOf(plot.getUniques())).replace("%plotReputation%",plotReputation).replace("%plotLastTime%",getElapsedTime(System.currentTimeMillis(),plot.getLastActivityTime())).replace("%plotCreationTime%",getElapsedTime(System.currentTimeMillis(), plot.getCreationTime())));
    }

    public static String parsePAPI(OfflinePlayer player, String string) {
        if (Main.isPAPIEnabled) {
            return PAPIUtils.parsePlaceholdersAPI(player,string);
        } else {
            return string;
        }
    }
}
