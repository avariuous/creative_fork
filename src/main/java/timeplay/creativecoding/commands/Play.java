/*
 Creative TimePlay 2023

 В этом классе содержится команда /play,
 которая должна запустить игру.

 !!! Эта функция пока-что тоже бесполезна.
 */

package timeplay.creativecoding.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.world.Plot;


public class Play implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
            // Проверка на владельца мира
            if (Plot.getOwner(plot).equals(sender.getName())) {
                plot.worldMode = Plot.mode.PLAYING;
                for (Player p : Plot.getPlayers(plot)) {
                    p.getInventory().clear();
                    p.getInventory().setHeldItemSlot(4);
                    p.getActivePotionEffects().clear();
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setExp(0F);
                    p.setFireTicks(0);
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(timeplay.creativecoding.Main.prefix() +"§fМир запущен в режиме §6игры§f.");
                    p.teleport(plot.world.getSpawnLocation());
                }
            } else {
                sender.sendMessage("§c Ты не владелец этого мира!");
            }
        }
        return true;
    }
}
