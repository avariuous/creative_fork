/*
 Creative- TimePlay 2022

 В этом классе содержится команда /build, которая
 позволяет создателю мира включить режим строительства.
 */

package timeplay.creativecoding.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.World.GetData;

import java.util.stream.Collectors;

public class Build implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            World world = ((Player) sender).getPlayer().getWorld();
            // Проверка на владельца мира
            if (GetData.isOwner(((Player) sender).getPlayer())) {
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                for (Player p : Bukkit.getOnlinePlayers().stream().filter(player1 -> player1.getWorld().equals(world)).collect(Collectors.toList())){
                    p.sendMessage(timeplay.creativecoding.Main.prefix() + "§fМир запущен в режиме §6строительства§f.");
                    p.sendTitle("§fРежим §6строительства","§fМир запущен в режиме §6строительства§f.");
                    p.teleport(world.getSpawnLocation());
                    p.playSound(p.getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"),100,1.7f);
                }
            } else {
               sender.sendMessage("§c Ты не владелец этого мира!");
            }
        }
        return true;
    }
}
