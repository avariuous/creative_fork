package timeplay.creativecoding;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import timeplay.creativecoding.Coding.PlaceBreak;
import timeplay.creativecoding.Commands.*;
import timeplay.creativecoding.Events.Death;
import timeplay.creativecoding.Events.InventoryClick;
import timeplay.creativecoding.Events.Join;
import timeplay.creativecoding.Events.PlayerInteract;

public final class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getCommand("creativecoding").setExecutor(new CreativeCoding());
        this.getCommand("join").setExecutor(new timeplay.creativecoding.Commands.Join());
        this.getCommand("join").setTabCompleter(new JoinTab());
        this.getCommand("play").setExecutor(new Play());
        this.getCommand("build").setExecutor(new Build());
        this.getCommand("dev").setExecutor(new Dev());
        getServer().getPluginManager().registerEvents(new PlaceBreak(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new Death(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        saveDefaultConfig();
        Bukkit.getServer().getLogger().info("Creative- is enabled.");

    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getLogger().info("Creative- is disabled.");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§6Creative§f- отключился, пожалуйста подождите...");
            LobbyTeleport(p);
        }
    }
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
}
