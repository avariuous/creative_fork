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
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.utils.FileUtils;

import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.FileUtils.loadLocales;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class CommandCreative implements CommandExecutor {

    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Если аргументы команды есть

        if (args.length > 0) {
            // Если это игрок
            if (sender instanceof Player) {

                if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                    sender.sendMessage(getLocaleMessage("cooldown"));
                    return true;
                }
                setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
                // Если аргумент равен
                switch (args[0]) {
                    case ("reload"):
                        if (sender.hasPermission("creative.reload")) {
                            sender.sendMessage("§fCreative§b+ §8| §7Перезагрузка плагина и конфига...");
                            ((Player) sender).sendTitle("§f§lCREATIVE§b§l+", "§fПерезагрузка плагина и конфига...", 20, 60, 20);
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 1);
                            plugin.reloadConfig();
                            loadLocales();
                            sender.sendMessage("§fCreative§b+ §8| §7Плагин и конфиг перезагружен §aуспешно");
                            ((Player) sender).sendTitle("§f§lCREATIVE§b§l+", "§fУспешно перезагружено!", 20, 60, 20);
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 2);
                        }
                        break;
                    case ("resetlocale"):
                        if (sender.hasPermission("creative.resetlocale")) {
                            sender.sendMessage("§fCreative§b+ §8| §fСброс файла локализации...");
                            FileUtils.resetLocales();
                            sender.sendMessage("§fCreative§b+ §8| §fФайл локализации §6успешно§f сброшен.");
                            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 1);
                        }
                        break;
                }
            // Если это консоль
            } else {
                if (args[0].equals("reload")) {
                    Bukkit.getLogger().info("Creative+ is reloading...");
                    plugin.reloadConfig();
                    loadLocales();
                    Bukkit.getLogger().info("Creative+ is reloaded!");
                }
            }
        // Если аргументов команды нет
        } else {
            try {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.version").replace("%version%", Main.version).replace("%codename%",Main.codename)));
            } catch (NullPointerException e) {
                sender.sendMessage("§fCreative§b+ %version%§f: %codename%".replace("%version%",Main.version).replace("%codename%",Main.codename));
            }
        }
        return true;
    }
}
