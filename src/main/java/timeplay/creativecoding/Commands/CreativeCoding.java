package timeplay.creativecoding.Commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.Menu.Worlds;
import timeplay.creativecoding.World.Create;
import timeplay.creativecoding.World.Delete;

import java.io.File;
import java.util.ArrayList;

public class CreativeCoding implements CommandExecutor {

    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch(args[0]) {
                case("reload"):
                    if ((sender instanceof Player) && sender.hasPermission("creativecoding.reload")) {
                        sender.sendMessage("§6Creative §8| §7Перезагрузка плагина и конфига...");
                        ((Player) sender).sendTitle("§6§lCREATIVE","§7Перезагрузка плагина и конфига...",20,60,20);
                        ((Player) sender).playSound(((Player) sender).getLocation(),Sound.valueOf("ENTITY_PLAYER_LEVELUP"),100,1);
                        plugin.getPluginLoader().disablePlugin(plugin);
                        plugin.reloadConfig();
                        plugin.getPluginLoader().enablePlugin(plugin);
                        sender.sendMessage("§6Creative §8| §7Плагин и конфиг перезагружен §aуспешно");
                        ((Player) sender).sendTitle("§6§lCREATIVE","§7Успешно перезагружено!",20,60,20);
                        ((Player) sender).playSound(((Player) sender).getLocation(),Sound.valueOf("ENTITY_PLAYER_LEVELUP"),100,2);
                    } if (!(sender instanceof Player)) {
                        System.out.println("CreativeCoding is reloading...");
                        plugin.getPluginLoader().disablePlugin(plugin);
                        plugin.reloadConfig();
                        plugin.getPluginLoader().enablePlugin(plugin);
                        System.out.println("CreativeCoding is reloaded!");
                    }
                    break;
                case("create"):
                    if ((sender instanceof Player) && sender.hasPermission("creativecoding.create")) {
                        Create.CreateWorld(((Player) sender).getPlayer());
                        break;
                    }
                case("delete"):
                    if ((sender instanceof Player) && sender.hasPermission("creativecoding.delete")) {
                        Delete.Delete(((Player) sender).getWorld(), ((Player) sender).getPlayer());
                        break;
                    }
                case("tp"):
                    if (sender instanceof Player) {
                        try {
                            Player player = ((Player) sender).getPlayer();
                            ((Player) sender).sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",20,60,20);
                            ((Player) sender).playSound(((Player) sender).getLocation(),Sound.valueOf("BLOCK_BEACON_AMBIENT"),100,2);
                            player.teleport(Bukkit.getWorld(sender.getName()).getSpawnLocation());
                        } catch(NullPointerException error1) {
                            ((Player) sender).playSound(((Player) sender).getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                            sender.sendMessage("");
                            sender.sendMessage("§cУ тебя ещё нет миров! Создай мир командой");
                            sender.sendMessage("§c§l /cc create");
                            sender.sendMessage("");
                        }
                        break;
                    }
                case("spawn"):
                    if ((sender instanceof Player) && sender.hasPermission("creativecoding.spawn")) {
                        //Bukkit.unloadWorld(sender.getName() + "1",true);
                        Player player = ((Player) sender).getPlayer();
                        Main.LobbyTeleport(player);
                        break;
                    }
                case("menu"):
                    if ((sender instanceof Player) && sender.hasPermission("creativecoding.menu")) {
                        Worlds.openMenu(((Player) sender).getPlayer());
                        break;
                    }
            }
        } else {
            if (sender instanceof Player) {
                ((Player) sender).playSound(((Player) sender).getLocation(),Sound.valueOf("ENTITY_PLAYER_LEVELUP"),100,1);
                sender.sendMessage("");
                sender.sendMessage("§6§lCREATIVE§f+");
                sender.sendMessage("§7Открытый для всех креатив");
                sender.sendMessage("");
                sender.sendMessage("§7 Создать мир §f/cc create");
                sender.sendMessage("§7 Телепорт в мир §f/cc tp");
                sender.sendMessage("§7 Вернуться на спавн §f/cc spawn");
                sender.sendMessage("§7 Удалить мир §f/cc delete");
                sender.sendMessage("");
            } else {
                System.out.println("");
                System.out.println("CREATIVE+");
                System.out.println("Открытый для всех креатив");
                System.out.println("Удалить мир /cc delete НАЗВАНИЕМИРА");
                System.out.println("");
            }
        }
        return true;
    }
}
