/*
 Creative TimePlay 2023

 Команда /dislike
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

public class CommandDislike implements CommandExecutor {

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
                if (addPlayerToListInPlotConfig(plot,sender.getName(), Plot.PlayersType.DISLIKED)) {
                    plot.plotReputation = plot.plotReputation-1;
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,100,0.7f);
                    sender.sendMessage(getLocaleMessage("world.disliked",((Player) sender).getPlayer()));
                }
            }
        }
        return true;
    }

}
