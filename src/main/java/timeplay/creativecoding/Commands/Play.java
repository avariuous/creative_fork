/*
 Creative- TimePlay 2022

 В этом классе содержится команда /play,
 которая должна запустить игру.

 !!! Эта функция пока-что тоже бесполезна.
 */

package timeplay.creativecoding.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.World.GetData;

import java.util.stream.Collectors;

public class Play implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            World world = ((Player) sender).getWorld();
            // Проверка на владельца мира
            if (GetData.isOwner(((Player) sender).getPlayer())) {
                ((Player) sender).setGameMode(GameMode.ADVENTURE);
                for (Player p : Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().equals(world)).collect(Collectors.toList())) {
                    p.sendMessage("§f Мир запущен в режиме §6игры§f.");
                    p.teleport(world.getSpawnLocation());
                }
            } else {
                sender.sendMessage("§c Ты не владелец этого мира!");
            }
        }
        return true;
    }
}
