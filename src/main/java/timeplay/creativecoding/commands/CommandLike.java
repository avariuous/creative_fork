/*
 Creative TimePlay 2023

 Команда /like
 */

package timeplay.creativecoding.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.CooldownUtils;

import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class CommandLike implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Plot plot = PlotManager.getPlotByPlayer(((Player) sender).getPlayer());
            if (plot == null) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                return true;
            }
            if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(),CooldownUtils.CooldownType.GENERIC_COMMAND))));
                return true;
            }
            setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
            if (getPlayersFromPlotConfig(plot, Plot.PlayersType.LIKED).contains(sender.getName())) {
                sender.sendMessage(getLocaleMessage("world.already-rated"));
            } else if (getPlayersFromPlotConfig(plot, Plot.PlayersType.DISLIKED).contains(sender.getName())) {
                sender.sendMessage(getLocaleMessage("world.already-rated"));
            } else {
                if (addPlayerToListInPlotConfig(plot,sender.getName(), Plot.PlayersType.LIKED)) {
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,100,1.3f);
                    plot.plotReputation = plot.plotReputation+1;
                    for (Player p : plot.getPlayers()) {
                        p.sendMessage(getLocaleMessage("world.liked").replace("%player%",sender.getName()));
                    }
                }
            }
        }
        return true;
    }

}
