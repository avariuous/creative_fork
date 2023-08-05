/*
 Creative TimePlay 2022

 В этом классе содержатся комманды вида /cc
 Они нужны для создания мира, удаления мира и другого.
 Их может использовать игрок либо консоль.
 */

package timeplay.creativecoding.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.menu.OwnWorldsMenu;
import timeplay.creativecoding.menu.WorldSettingsMenu;
import timeplay.creativecoding.menu.AllWorldsMenu;
import timeplay.creativecoding.world.Delete;
import timeplay.creativecoding.world.Plot;

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
                            new Plot(((Player) sender).getPlayer());
                        }
                        break;
                    case ("delete"):
                        if (sender.hasPermission("creativecoding.delete")) {
                            Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
                            if (plot.owner == (((Player) sender).getPlayer())) {
                                Delete.Delete(plot, ((Player) sender).getPlayer());
                            } else {
                                if (sender.hasPermission("creativecoding.deletebypass")) {
                                    Delete.Delete(plot, ((Player) sender).getPlayer());
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
                            AllWorldsMenu.openMenu(((Player) sender).getPlayer());
                        }
                        break;
                    case ("settings"):
                        if (sender.hasPermission("creativecoding.menu")) {
                            Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
                            if (plot.owner == (((Player) sender).getPlayer())) {
                                WorldSettingsMenu.openMenu(((Player) sender).getPlayer());
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
                        OwnWorldsMenu.openMenu(((Player) sender).getPlayer());
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
                            Delete.Delete(Plot.getPlotByWorld(Bukkit.getWorld(args[0])), null);
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
                sender.sendMessage("§7 Создать мир §f/coding create");
                sender.sendMessage("§7 Телепорт в мир §f/coding tp");
                sender.sendMessage("§7 Вернуться на спавн §f/coding spawn");
                sender.sendMessage("§7 Удалить мир §f/coding delete");
                sender.sendMessage("");
            }
        return true;
    }
}
