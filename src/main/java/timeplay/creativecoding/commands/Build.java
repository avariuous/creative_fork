/*
 Creative TimePlay 2023

 В этом классе содержится команда /build, которая
 позволяет создателю мира включить режим строительства.
 */

package timeplay.creativecoding.commands;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.world.Plot;

public class Build implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
            if (Plot.getOwner(plot).equals(sender.getName())) {
                for (Player p : Plot.getPlayers(plot)){
                    p.getInventory().clear();
                    p.getInventory().setHeldItemSlot(4);
                    p.getActivePotionEffects().clear();
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setExp(0F);
                    p.setFireTicks(0);
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(timeplay.creativecoding.Main.prefix() + "§fМир запущен в режиме §6строительства§f.");
                    p.sendTitle("§fРежим §6строительства","§fМир запущен в режиме §6строительства§f.");
                    p.teleport(plot.world.getSpawnLocation());
                    p.playSound(p.getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"),100,1.7f);
                }
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                plot.worldMode = Plot.mode.BUILD;
            } else {
               sender.sendMessage("§c Ты не владелец этого мира!");
            }
        }
        return true;
    }
}
