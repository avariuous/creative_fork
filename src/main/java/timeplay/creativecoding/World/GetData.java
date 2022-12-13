/*
 Creative- TimePlay 2022

 Здесь можно получить данные об мире
 */

package timeplay.creativecoding.World;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GetData {

    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");

    // Узнать является ли игрок владельцем мира? true false
    public static boolean isOwner(Player player) {
        World world = player.getWorld();
        String worldname = world.getName();
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if (player.getName().equals(String.valueOf(worldfile.get("owner")))) {
            return true;
        }
        else {
            return false;
        }
    }

    // Узнать название мира
    public static String title(World world) {
        String worldname = world.getName();
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return String.valueOf(worldfile.get("title"));
        }
        else {
            return "Unknown title";
        }
    }
    // Узнать описание мира
    public static String description(World world) {
        String worldname = world.getName();
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return String.valueOf(worldfile.get("description"));
        }
        else {
            return "Unknown description";
        }
    }

    public static Material icon(World world) {
        String worldname = world.getName();
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return Material.valueOf(String.valueOf(worldfile.get("icon")));
        }
        else {
            return Material.GOLD_NUGGET;
        }
    }

    // Узнать владельца мира
    public static String owner(World world) {
        String worldname = world.getName();
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return String.valueOf(worldfile.get("owner"));
        }
        else {
            return "Unknown owner";
        }
    }

    // Узнать онлайн мира
    public static int online(World world) {
        return Bukkit.getOnlinePlayers().stream().filter(player1 -> player1.getWorld().equals(world)).collect(Collectors.toList()).size();
    }

    // Узнать список игроков в мире
    public static List onlineList(World world) {
        return Bukkit.getOnlinePlayers().stream().filter(player1 -> player1.getWorld().equals(world)).collect(Collectors.toList());
    }
}
