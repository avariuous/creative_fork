/*
 Creative TimePlay 2022

 В этом классе содержится команда /join или /ad,
 она позволяет зайти на любой мир с помощью его ID.
 */

package timeplay.creativecoding.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.utils.CooldownUtils;

import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.plots.PlotManager.plots;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.plots.Plot.teleportToPlot;

public class CommandJoin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(),CooldownUtils.CooldownType.GENERIC_COMMAND))));
                return true;
            }
            setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                if (plots.size() > 0) {
                    Plot foundPlot = null;
                    for (Plot searchablePlot : plots) {
                        if (searchablePlot.worldID.equals(args[0])) {
                            foundPlot = searchablePlot;
                            break;
                        } else if (searchablePlot.plotCustomID.equalsIgnoreCase(args[0])) {
                            foundPlot = searchablePlot;
                            break;
                        }
                    }
                    if (foundPlot != null) {
                        teleportToPlot(player,foundPlot);
                    } else {
                        player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                        player.clearTitle();
                        player.sendMessage(getLocaleMessage("no-plot-found",player));
                    }
                } else {
                    player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                    player.clearTitle();
                    player.sendMessage(getLocaleMessage("no-plot-found",player));
                }
            } else {
                player.sendMessage(getLocaleMessage("join-usage"));
            }
        }
        return true;
    }
}
