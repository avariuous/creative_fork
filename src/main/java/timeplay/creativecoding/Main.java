/*
 Creative TimePlay 2023

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
import timeplay.creativecoding.coding.PlaceBreak;
import timeplay.creativecoding.commands.*;
import timeplay.creativecoding.events.*;

import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.*;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Регистрация команд
        this.getCommand("creativecoding").setExecutor(new CommandCreative());
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
        // Регистрация событий
        getServer().getPluginManager().registerEvents(new PlaceBreak(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new Quit(), this);
        getServer().getPluginManager().registerEvents(new Respawn(), this);
        getServer().getPluginManager().registerEvents(new Death(), this);
        getServer().getPluginManager().registerEvents(new ChangedWorld(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        saveDefaultConfig();
        Bukkit.getServer().getLogger().info("Creative is enabled!");
        // Подгрузка миров игроков
        loadLocales();
        if (loadWorlds()) Bukkit.getLogger().info("Все миры загружены успешно!");
        //
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§6 Creative§f запущен!");
            teleportToLobby(player);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getLogger().info("Creative is disabled.");
        // Телепортация всех игроков в лобби
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.sendMessage("§6 Creative§f завершает работу, пожалуйста подождите...");
            teleportToLobby(player);
        }
        unloadWorlds();
    }
    // Телепортация в лобби
    public static void teleportToLobby(Player player) {
        clearPlayer(player);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());

        // if (Bukkit.getPluginManager().getPlugin("Creative").isEnabled()) LobbyScoreboard.show(player);

        player.stopSound(Sound.MUSIC_DISC_CHIRP);
        player.sendTitle(getLocaleMessage("lobby.title"),getLocaleMessage("lobby.subtitle"),20,60,20);
        player.sendMessage(getLocaleMessage("lobby.message"));
        player.playSound(player.getLocation(),Sound.BLOCK_BEACON_DEACTIVATE,100,2);
        player.playSound(player.getLocation(),Sound.MUSIC_DISC_CHIRP,100,0.7f);

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

    public static void clearPlayer(Player player) {

        player.closeInventory();
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.setFireTicks(0);
        player.setExp(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setGliding(false);

    }

    public static String version = "1.3";
}
