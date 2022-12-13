/*
 Creative- TimePlay 2022

 В этом классе содержатся комманды вида /cc
 Они нужны для создания мира, удаления мира и другого.
 Их может использовать игрок либо консоль.
 */

package timeplay.creativecoding.Commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.Menu.WorldSettings;
import timeplay.creativecoding.Menu.Worlds;
import timeplay.creativecoding.World.Create;
import timeplay.creativecoding.World.Delete;
import timeplay.creativecoding.World.GetData;

public class CreativeCoding implements CommandExecutor {

    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Если аргументы команды есть
        if (args.length > 0) {
            // Если это игрок
            if (sender instanceof Player) {
                // Если аргумент равен
                switch (args[0]) {
                    case ("create"):
                        if (sender.hasPermission("creativecoding.create")) {
                            Create.CreateWorld(((Player) sender).getPlayer());
                        }
                        break;
                    case ("delete"):
                        if (sender.hasPermission("creativecoding.delete")) {
                            if (GetData.isOwner(((Player) sender).getPlayer())) {
                                Delete.Delete(((Player) sender).getWorld(), ((Player) sender).getPlayer());
                            } else {
                                if (sender.hasPermission("creativecoding.deletebypass")) {
                                    Delete.Delete(((Player) sender).getWorld(), ((Player) sender).getPlayer());
                                } else {
                                    sender.sendMessage("§c Ты не владелец этого мира!");
                                }
                            }
                        }
                        break;
                    case ("spawn"):
                        if (sender.hasPermission("creativecoding.spawn")) {
                            Player player = ((Player) sender).getPlayer();
                            Main.LobbyTeleport(player);
                        }
                        break;
                    case ("menu"):
                        if (sender.hasPermission("creativecoding.menu")) {
                            Worlds.openMenu(((Player) sender).getPlayer());
                        }
                        break;
                    case ("settings"):
                        if (sender.hasPermission("creativecoding.menu")) {
                            if (GetData.isOwner(((Player) sender).getPlayer())) {
                                WorldSettings.openMenu(((Player) sender).getPlayer());
                            } else {
                                sender.sendMessage("§c Ты не владелец этого мира!");
                            }
                        }
                        break;
                    case ("reload"):
                        if (sender.hasPermission("creativecoding.reload")) {
                            sender.sendMessage("§6Creative §8| §7Перезагрузка плагина и конфига...");
                            ((Player) sender).sendTitle("§6§lCREATIVE", "§7Перезагрузка плагина и конфига...", 20, 60, 20);
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 1);
                            plugin.getPluginLoader().disablePlugin(plugin);
                            plugin.reloadConfig();
                            plugin.getPluginLoader().enablePlugin(plugin);
                            sender.sendMessage("§6Creative §8| §7Плагин и конфиг перезагружен §aуспешно");
                            ((Player) sender).sendTitle("§6§lCREATIVE", "§7Успешно перезагружено!", 20, 60, 20);
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 2);
                        }
                        break;
                    case ("tp"):
                        try {
                            Player player = ((Player) sender).getPlayer();
                            ((Player) sender).sendTitle("§aЗагрузка...", "§7Подождите несколько секунд...", 20, 60, 20);
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("BLOCK_BEACON_AMBIENT"), 100, 2);
                            player.teleport(Bukkit.getWorld(sender.getName()).getSpawnLocation());
                        } catch (NullPointerException error1) {
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("BLOCK_ANVIL_DESTROY"), 100, 2);
                            sender.sendMessage("");
                            sender.sendMessage("§cУ тебя ещё нет миров! Создай мир командой");
                            sender.sendMessage("§c§l /cc create");
                            sender.sendMessage("");
                        }
                        break;
                }
            // Если это консоль
            } else {
                switch (args[0]) {
                    case ("reload"):
                        Bukkit.getLogger().info("Creative- is reloading...");
                        plugin.getPluginLoader().disablePlugin(plugin);
                        plugin.reloadConfig();
                        plugin.getPluginLoader().enablePlugin(plugin);
                        Bukkit.getLogger().info("Creative- is reloaded!");
                        break;
                    case ("delete"):
                        if (args.length == 2) {
                            Delete.Delete(Bukkit.getWorld(args[0]), null);
                            break;
                        }
                }
            }
        // Если аргументов команды нет
        } else {
                ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 1);
                sender.sendMessage("");
                sender.sendMessage("§6§lCREATIVE§f+");
                sender.sendMessage("§7Открытый для всех креатив");
                sender.sendMessage("");
                sender.sendMessage("§7 Создать мир §f/cc create");
                sender.sendMessage("§7 Телепорт в мир §f/cc tp");
                sender.sendMessage("§7 Вернуться на спавн §f/cc spawn");
                sender.sendMessage("§7 Удалить мир §f/cc delete");
                sender.sendMessage("");
            }
        return true;
    }
}
