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
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.world.Plot;

import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;


public class CommandPlay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (((Player) sender).getPlayer().getWorld().getName().contains("world")) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                return true;
            }
            if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(),CooldownUtils.CooldownType.GENERIC_COMMAND))));
                return true;
            }
            setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
            Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
            // Проверка на владельца мира
            if (Plot.getOwner(plot).equals(sender.getName())) {
                plot.plotMode = Plot.mode.PLAYING;
                for (Player p : Plot.getPlayers(plot)) {
                    clearPlayer(p);
                    p.sendMessage(getLocaleMessage("world.play-mode"));
                    p.teleport(plot.world.getSpawnLocation());
                }
            } else {
                sender.sendMessage(getLocaleMessage("not-owner", ((Player) sender).getPlayer()));
            }
        }
        return true;
    }
}
