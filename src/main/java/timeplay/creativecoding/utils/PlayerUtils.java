package timeplay.creativecoding.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;

public class PlayerUtils {

    @NotNull
    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");


    public static String getGroup(Player player) {
        ConfigurationSection groupsSection = plugin.getConfig().getConfigurationSection("groups");
        if (groupsSection != null) {
            String returnGroup;
            returnGroup = "default";
            for (String group : groupsSection.getKeys(false)) {
                if (!(group.equals("default"))) {
                    String permission = plugin.getConfig().getString("groups." + group + ".permission");
                    if (permission != null && player.hasPermission(permission)) {
                        returnGroup = group;
                    }
                }
            }
            return returnGroup;
        } else {
            sendCriticalErrorMessage("При попытке получить группу игрока оказалось, что секция groups из config.yml не заполнена, вам необходимо её заполнить.");
            return "default";
        }
    }


    public static int getIntFromGroups(Player player, String intPath) {
        return plugin.getConfig().getInt("groups." + getGroup(player) + "." + intPath);
    }

    public static int getIntFromGroups(String group, String intPath) {
        return plugin.getConfig().getInt("groups." + group + "." + intPath);
    }

    public static int getListFromGroups(String group, String listPath) {
        return plugin.getConfig().getInt("groups." + group + "." + listPath);
    }

    public static int getPlayerPlotsLimit(String group) {
        return getIntFromGroups(group,"creating-world.limit");
    }

    public static int getPlayerPlotsLimit(Player player) {
        return getIntFromGroups(player,"creating-world.limit");
    }

    public static int getPlayerPlotSize(String group) {
        return getIntFromGroups(group,"world.size");
    }

    public static int getPlayerPlotEntitiesLimit(String group) {
        return getIntFromGroups(group,"world.entities-limit");
    }

    public static int getPlayerPlotRedstoneOperationsLimit(String group) {
        return getIntFromGroups(group,"world.redstone-operations-limit");
    }

    public static int getPlayerPermissionsList(String group) {
        return getListFromGroups(group,"permissions");
    }

}
