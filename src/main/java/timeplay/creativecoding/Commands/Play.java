package timeplay.creativecoding.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class Play implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            World world = ((Player) sender).getWorld();
            ((Player) sender).setGameMode(GameMode.ADVENTURE);
            for (Player p : Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().equals(world)).collect(Collectors.toList())) {
                p.sendMessage("§f Мир запущен в режиме §6игры§f.");
                p.teleport(world.getSpawnLocation());
            }
        }
        return true;
    }
}
