/*
 Creative TimePlay 2023

 Утилита для получения сообщения из файла локализации
 */

package timeplay.creativecoding.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.utils.ErrorUtils.sendWarningErrorMessage;

public class MessageUtils {

    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    public static String getLocaleMessage(String messageID) {
        try {
            String lang = String.valueOf(plugin.getConfig().get("messages.locale"));
            File file = new File((plugin.getDataFolder() + File.separator + "locales" + File.separator), lang + ".yml");
            YamlConfiguration messages = YamlConfiguration.loadConfiguration(file);
            String message = ChatColor.translateAlternateColorCodes('&',messages.getString(messageID).replace("%prefix%",plugin.getConfig().getString("messages.prefix")).replace("%ccprefix%",plugin.getConfig().getString("messages.cc-prefix")));
            if (message.equalsIgnoreCase("null")) {
                sendWarningErrorMessage("Не нашлось сообщение " + messageID + " в файле локализации!");
                return "§2 Ошибка §8» §fНе найдено сообщение §6" + messageID + "§f! Сообщите администрации, чтобы они заполнили его в файле локализации §6locales"+File.separator+lang+".yml";
            }
            return message;
        } catch (Exception e) {
            sendWarningErrorMessage("Не нашлось сообщение " + messageID + " Возможно не найден config.yml, либо строка locale не верная, либо не найден файл ru.yml в папке locales");
            Object locale = "ru";
            plugin.getConfig().set("messages.locale",locale);
            plugin.reloadConfig();
            return messageID;
        }
    }

    public static String getLocaleMessage(String messageID, Player player) {
        try {
            String lang = String.valueOf(plugin.getConfig().get("messages.locale"));
            File file = new File((plugin.getDataFolder() + File.separator + "locales" + File.separator), lang + ".yml");
            YamlConfiguration messages = YamlConfiguration.loadConfiguration(file);
            String message = ChatColor.translateAlternateColorCodes('&',messages.getString(messageID).replace("%player%", player.getName()).replace("%prefix%",plugin.getConfig().getString("messages.prefix")).replace("%ccprefix%",plugin.getConfig().getString("messages.cc-prefix")));
            if (message.equalsIgnoreCase("null")) {
                sendWarningErrorMessage("Не нашлось сообщение " + messageID + " в файле локализации!");
                return "§2 Ошибка §8» §fНе найдено сообщение §6" + messageID + "§f! Сообщите администрации, чтобы они заполнили его в файле локализации §6locales"+File.separator+lang+".yml";            }
            return message;
        } catch (Exception e) {
            sendWarningErrorMessage("Не нашлось сообщение " + messageID + " Возможно не найден config.yml, либо строка locale не верная, либо не найден файл ru.yml в папке locales");
            Object locale = "ru";
            plugin.getConfig().set("messages.locale",locale);
            plugin.reloadConfig();
            return messageID;
        }
    }

    public static String getLocaleMessage(String messageID, Boolean sendMessageIDError) {
        try {
            String lang = String.valueOf(plugin.getConfig().get("messages.locale"));
            File file = new File((plugin.getDataFolder() + File.separator + "locales" + File.separator), lang + ".yml");
            YamlConfiguration messages = YamlConfiguration.loadConfiguration(file);
            String message = ChatColor.translateAlternateColorCodes('&',messages.getString(messageID).replace("%prefix%",plugin.getConfig().getString("messages.prefix")).replace("%ccprefix%",plugin.getConfig().getString("messages.cc-prefix")));
            if (message.equalsIgnoreCase("null")) {
                sendWarningErrorMessage("Не нашлось сообщение " + messageID + " в файле локализации!");
                if (sendMessageIDError) {
                    return "§2 Ошибка §8» §fНе найдено сообщение §6" + messageID + "§f! Сообщите администрации, чтобы они заполнили его в файле локализации §6locales" + File.separator + lang + ".yml";
                } else {
                    return messageID;
                }
            }
            return message;
        } catch (Exception e) {
            sendWarningErrorMessage("Не нашлось сообщение " + messageID + " Возможно не найден config.yml, либо строка locale не верная, либо не найден файл ru.yml в папке locales");
            Object locale = "ru";
            plugin.getConfig().set("messages.locale",locale);
            plugin.reloadConfig();
            return messageID;
        }
    }

    public static String getLocaleItemName(String nameID) {
        try {
            String lang = String.valueOf(plugin.getConfig().get("messages.locale"));
            File file = new File((plugin.getDataFolder() + File.separator + "locales" + File.separator), lang + ".yml");
            YamlConfiguration names = YamlConfiguration.loadConfiguration(file);
            String name = ChatColor.translateAlternateColorCodes('&',names.getString(nameID).replace("%prefix%",plugin.getConfig().getString("messages.prefix")).replace("%ccprefix%",plugin.getConfig().getString("messages.cc-prefix")));
            if (name.length() > 50) name = name.substring(0,50);
            if (name.equalsIgnoreCase("null")) {
                sendWarningErrorMessage("Не нашлось название предмета " + nameID + " в файле локализации!");
                return "§fНе найдено название: " + nameID;
            }
            return name;
        } catch (Exception e) {
            sendWarningErrorMessage("Не нашлось название предмета " + nameID + " Возможно вы не заполнили это в файле локализации, либо не найден config.yml, либо строка locale не верная, либо не найден файл ru.yml в папке locales");
            Object locale = "ru";
            plugin.getConfig().set("messages.locale",locale);
            plugin.reloadConfig();
            return "§f" + nameID;
        }
    }

    public static List<String> getLocaleItemDescription(String descriptionID) {
        List<String> description = new ArrayList<>();
        try {
            String lang = String.valueOf(plugin.getConfig().get("messages.locale"));
            File file = new File((plugin.getDataFolder() + File.separator + "locales" + File.separator), lang + ".yml");
            YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
            List<String> descriptions = langConfig.getStringList(descriptionID);
            if (descriptions.size() < 1) {
                sendWarningErrorMessage("Не найдено описание в предмета " + descriptionID);
                description.add("§6Не найдено описание предмета");
                description.add("§6" + descriptionID);
                description.add("§fОбратитесь к администрации!");
                description.add("§f Нужно заполнить это поле в файле ");
                description.add("§f локализации locales" + File.separator + lang + ".yml");
            } else if (descriptions.size() > 50) {
                sendWarningErrorMessage("Слишком много строк описания в " + descriptionID);
                for (String descriptionLine : descriptions) {
                    description.add(ChatColor.translateAlternateColorCodes('&',descriptionLine.replace("%prefix%",plugin.getConfig().getString("messages.prefix")).replace("%ccprefix%",plugin.getConfig().getString("messages.cc-prefix"))));
                }
            } else {
                for (String descriptionLine : descriptions) {
                    description.add(ChatColor.translateAlternateColorCodes('&',descriptionLine.replace("%prefix%",plugin.getConfig().getString("messages.prefix")).replace("%ccprefix%",plugin.getConfig().getString("messages.cc-prefix"))));
                }
            }
            return description;
        } catch (Exception e) {
            sendWarningErrorMessage("Не нашлось описание предмета " + descriptionID + " Возможно не найден config.yml, либо строка locale не верная, либо не найден файл ru.yml в папке locales");
            Object locale = "ru";
            plugin.getConfig().set("messages.locale",locale);
            plugin.reloadConfig();
            description.add("§6Не найдено описание предмета");
            description.add("§6" + descriptionID);
            description.add("§fОбратитесь к администрации сервера!");
            description.add("§f Либо такого описания нет в файле локализации,");
            description.add("§f либо config.yml не существует, либо файла ");
            description.add("§f локализации не существует, либо вы не заполнили");
            description.add("§f messages.cc-prefix, messages.prefix в конфиге");
            return description;
        }
    }
}
