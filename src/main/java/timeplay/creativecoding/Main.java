/*
 Creative- TimePlay 2022

 Главный класс, запускает сам плагин.
 Содержит так-же телепортацию в лобби, получение префикса чата плагина.
 */

package timeplay.creativecoding;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import timeplay.creativecoding.Coding.PlaceBreak;
import timeplay.creativecoding.Commands.*;
import timeplay.creativecoding.Events.*;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Регистрация команд
        this.getCommand("creativecoding").setExecutor(new CreativeCoding());
        this.getCommand("chat").setExecutor(new CreativeChat());
        this.getCommand("join").setExecutor(new timeplay.creativecoding.Commands.Join());
        this.getCommand("ad").setExecutor(new Ad());
        this.getCommand("join").setTabCompleter(new JoinTab());
        this.getCommand("play").setExecutor(new Play());
        this.getCommand("build").setExecutor(new Build());
        this.getCommand("dev").setExecutor(new Dev());
        // Регистрация событий
        getServer().getPluginManager().registerEvents(new PlaceBreak(), this);
        getServer().getPluginManager().registerEvents(new timeplay.creativecoding.Events.Join(), this);
        getServer().getPluginManager().registerEvents(new Quit(), this);
        getServer().getPluginManager().registerEvents(new Respawn(), this);
        getServer().getPluginManager().registerEvents(new Death(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        saveDefaultConfig();
        Bukkit.getServer().getLogger().info("Creative- is enabled.");
        // Подгрузка миров игроков
        File folder = new File(this.getDataFolder() + "\\worlds\\");
        File[] listOfFiles = folder.listFiles();
        try {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String fileName = file.toString().replace(this.getDataFolder() + "\\worlds\\", "").replace(".yml", "");
                    WorldCreator load = new WorldCreator(fileName);
                    try {
                        Bukkit.getLogger().info("Creative- loads world: " + fileName);
                        load.createWorld();
                    } catch (NullPointerException error) {
                        Bukkit.getLogger().warning("Error while loading " + fileName);
                    }
                }
            }
        } catch (NullPointerException error) {
            Bukkit.getLogger().warning("Error while loading worlds... :(");
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getLogger().info("Creative- is disabled.");
        // Телепортация всех игроков в лобби
        for (Player p: Bukkit.getOnlinePlayers()) {
            p.sendMessage("§6Creative§f- отключился, пожалуйста подождите...");
            LobbyTeleport(p);
        }
    }
    // Телепортация в лобби
    public static void LobbyTeleport(Player player) {
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.sendTitle("§6§lCREATIVE§e-","§7Открытый для всех креатив",20,60,20);
        ((Player) player).playSound(((Player) player).getLocation(),Sound.valueOf("BLOCK_BEACON_DEACTIVATE"),100,2);
        ItemStack item1 = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("§dКомпасс игр");
        item1.setItemMeta(meta1);
        player.getInventory().setItem(3, item1);
        ItemStack item2 = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName("§bТелепортация на свою игру");
        item2.setItemMeta(meta2);
        player.getInventory().setItem(5, item2);
    }

    // Получение префикса чата с config
    public static String prefix() {
        try {
            return Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding").getConfig().getString("prefix").replace('&', '§');
        } catch (NullPointerException e) {
            return "§2Миры §8> §f";
        }
    }
}
