/*
 Creative TimePlay 2023

 В этом классе содержится команда /cc
 используется для общения в креатив чате!
 */

package timeplay.creativecoding.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.utils.CooldownUtils;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.ErrorUtils.sendWarningErrorMessage;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.utils.MessageUtils.parsePAPI;

public class CreativeChat implements CommandExecutor {

    public static List<Player> creativeChatOff = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args.length == 1 && (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("on"))) {
                    if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                        sender.sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND))));
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("off")) {
                        creativeChatOff.add(((Player) sender).getPlayer());
                        sender.sendMessage(getLocaleMessage("creative-chat.turned-off"));
                    } else if (args[0].equalsIgnoreCase("on")) {
                        creativeChatOff.remove(((Player) sender).getPlayer());
                        sender.sendMessage(getLocaleMessage("creative-chat.turned-on"));
                    }
                    setCooldown(((Player) sender).getPlayer(), plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
                } else {
                    if (creativeChatOff.contains(((Player) sender).getPlayer())) {
                        sender.sendMessage(getLocaleMessage("creative-chat.on-usage"));
                    } else {
                        if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.CREATIVE_CHAT) > 0) {
                            sender.sendMessage(getLocaleMessage("creative-chat.cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.CREATIVE_CHAT))));
                            return true;
                        }
                        setCooldown(((Player) sender).getPlayer(), plugin.getConfig().getInt("cooldowns.creative-chat"), CooldownUtils.CooldownType.CREATIVE_CHAT);
                        Bukkit.getLogger().info("[CREATIVE-CHAT] "+sender.getName()+": "+String.join(" ",args));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!(creativeChatOff.contains(p))) {
                                if (plugin.getConfig().getString("messages.cc-chat") != null)
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',parsePAPI(Bukkit.getPlayer(sender.getName()),plugin.getConfig().getString("messages.cc-chat")).replace("%player%",sender.getName()).replace("%cc-prefix%",plugin.getConfig().getString("messages.cc-prefix")).replace("%message%",String.join(" ",args))));
                                } else {
                                    sendWarningErrorMessage("Не найдено в конфиге значение messages.cc-prefix messages.cc-chat");
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(getLocaleMessage("creative-chat.cc-usage"));
            }
        return true;
    }
}
