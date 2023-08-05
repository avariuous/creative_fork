/*
 Creative TimePlay 2022

 В этом классе содержится команда /join или /ad,
 она позволяет зайти на любой мир с помощью его ID.
 */

package timeplay.creativecoding.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.Main;

public class Join implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                try {
                    player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",20,60,20);
                    player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
                    player.setGameMode(GameMode.ADVENTURE);
                    player.getInventory().clear();
                    Main.clearTitle(player);
                    player.playSound(player.getLocation(), Sound.valueOf("BLOCK_BEACON_AMBIENT"),100,2);
                } catch(NullPointerException error1) {
                    player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                    Main.clearTitle(player);
                    player.sendMessage("§c Мира с таким ID не существует.");
                }
            } else {
                player.sendMessage("§f Используй §6/join ID мира");
            }
        }
        return true;
    }
}
