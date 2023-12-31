/*
 Creative 1.4
 2023

 Это главный класс плагина, который запускает
 сам плагин, выполняет загрузку плотов, телепортирует
 игроков в лобби. Отвечает так-же за отключение
 плагина, которое отгружает плоты.

 Лицензия кода всего плагина: GNU GPL v3.0
 */

package timeplay.creativecoding;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import timeplay.creativecoding.commands.*;
import timeplay.creativecoding.events.*;

import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.*;

public final class Main extends JavaPlugin implements Listener {

    public static String version = "1.4 Release-Candidate";
    public static String codename = "Keep your friends close";

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        Bukkit.getServer().getLogger().info("Loading Creative+ " + version + ": " + codename + ", please wait...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,1));
            player.sendTitle("§f§lCREATIVE§b+ " + version,"§f" + codename + "...",0,100,0);
        }
        // Регистрация команд
        Bukkit.getServer().getLogger().info("Loading Creative+ commands...");
        this.getCommand("creative").setExecutor(new CommandCreative());
        this.getCommand("menu").setExecutor(new CommandMenu());
        this.getCommand("spawn").setExecutor(new CommandSpawn());
        this.getCommand("world").setExecutor(new CommandWorld());
        this.getCommand("chat").setExecutor(new CreativeChat());
        this.getCommand("join").setExecutor(new CommandJoin());
        this.getCommand("ad").setExecutor(new CommandAd());
        this.getCommand("join").setTabCompleter(new CommandTabJoin());
        this.getCommand("play").setExecutor(new CommandPlay());
        this.getCommand("build").setExecutor(new CommandBuild());
        this.getCommand("dev").setExecutor(new CommandDev());
        this.getCommand("like").setExecutor(new CommandLike());
        this.getCommand("dislike").setExecutor(new CommandDislike());
        // Регистрация событий
        Bukkit.getServer().getLogger().info("Loading Creative+ events...");
        getServer().getPluginManager().registerEvents(new ChangedWorld(), this);
        getServer().getPluginManager().registerEvents(new EntitySpawn(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerPlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerBucket(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new BlockRedstone(), this);
        saveDefaultConfig();
        loadLocales();
        if (loadPlots()) Bukkit.getLogger().info("Loaded all Creative+ worlds to base.");
        long loadedTime = System.currentTimeMillis()-startTime;
        for (Player player : Bukkit.getOnlinePlayers()) {
            teleportToLobby(player);
            player.sendActionBar("§f§l Creative§b+§f " + version + " is loaded for " + loadedTime + " ms.");
        }

        isPAPIEnabled = isPluginEnabled("PlaceholderAPI");

        Bukkit.getLogger().info("Creative+ " + version + ": " + codename + " is loaded for " + loadedTime);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getLogger().info("Creative is disabled.");
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage("§f§l Creative§b+§f is shutting down, please wait...");
            teleportToLobby(player);
        }
        unloadPlots();
    }

    // Телепортация в лобби
    public static void teleportToLobby(Player player) {
        clearPlayer(player);
        World lobbyWorld = Bukkit.getWorld("world");
        if (lobbyWorld != null) player.teleport(lobbyWorld.getSpawnLocation());

        player.sendTitle(getLocaleMessage("lobby.title"),getLocaleMessage("lobby.subtitle"),20,60,20);
        player.sendMessage(getLocaleMessage("lobby.message"));
        player.playSound(player.getLocation(),Sound.BLOCK_BEACON_DEACTIVATE,100,1.5f);
        player.playSound(player.getLocation(),Sound.MUSIC_DISC_CHIRP,100,0.8f);

        ItemStack item1 = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName(getLocaleItemName("items.lobby.games.name"));
        meta1.setLore(getLocaleItemDescription("items.lobby.games.lore"));
        item1.setItemMeta(meta1);
        player.getInventory().setItem(3, item1);

        ItemStack item2 = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName(getLocaleItemName("items.lobby.own.name"));
        meta2.setLore(getLocaleItemDescription("items.lobby.own.lore"));
        item2.setItemMeta(meta2);
        player.getInventory().setItem(5, item2);
    }

    // Очистка параметров игрока
    public static void clearPlayer(Player player) {

        player.closeInventory();
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setFireTicks(0);
        player.setExp(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setGliding(false);
        for (Sound sound : Sound.values()) {
            player.stopSound(sound);
        }

    }

    public static boolean isPluginEnabled(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    public static boolean isPAPIEnabled = false;

}
