/*
 Creative- TimePlay 2022

 В этом классе содержится команда /cc
 используется для общения в креатив чате!
 */

package timeplay.creativecoding.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreativeChat implements CommandExecutor {

    public static List<Player> creativeChatOff = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("off")) {
                    creativeChatOff.add(((Player) sender).getPlayer());
                    sender.sendMessage("§2Креатив-чат §8> §fВы §6отключили§f креатив чат, вам не будут поступать сообщения.");
                } else if (args[0].equalsIgnoreCase("on")) {
                    creativeChatOff.remove(((Player) sender).getPlayer());
                    sender.sendMessage("§2Креатив-чат §8> §fВы §6включили§f креатив чат, теперь общайтесь!");
                } else {
                    if (creativeChatOff.contains(((Player) sender).getPlayer())) {
                        sender.sendMessage("§c Напиши /cc on, чтобы включить чат");
                    } else {
                        for (Player p : Bukkit.getOnlinePlayers().stream().filter(player1 -> (!(creativeChatOff.contains(player1)))).collect(Collectors.toList())) {
                            p.sendMessage("§2Креатив-чат §8> §7" + sender.getName() + "§7: " + String.join(" ",args));
                        }
                    }
                }
            } else {
                sender.sendMessage("§cИспользуйте /cc сообщение");
            }
        }
        return true;
    }
}
