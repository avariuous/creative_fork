/*
 Creative TimePlay 2023

 В этом классе содержится команда /ad, которая
 позволяет игроку прорекламировать мир или же
 телепортироваться в другой мир.
 */

package timeplay.creativecoding.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.plots.Plot;

import static timeplay.creativecoding.plots.PlotManager.plots;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.plots.Plot.teleportToPlot;

public class CommandAd implements CommandExecutor {

    @NotNull
    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            Plot plot = PlotManager.getPlotByPlayer(player);
            if (args.length == 1) {
                if (getCooldown(player, CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                    player.sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(player,CooldownUtils.CooldownType.GENERIC_COMMAND))));
                    return true;
                }
                setCooldown(player,plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
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
                if (plot == null) {
                    player.sendMessage(getLocaleMessage("only-in-world"));
                    return true;
                }
                if (getCooldown(player, CooldownUtils.CooldownType.ADVERTISEMENT_COMMAND) > 0) {
                    player.sendMessage(getLocaleMessage("advertisement.cooldown").replace("%cooldown%",String.valueOf(getCooldown(player,CooldownUtils.CooldownType.ADVERTISEMENT_COMMAND))));
                    return true;
                }
                setCooldown(player,plugin.getConfig().getInt("cooldowns.advertisement"), CooldownUtils.CooldownType.ADVERTISEMENT_COMMAND);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    TextComponent advertisement = new TextComponent("§7 \n§7 " + player.getName() + "§7 приглашает вас в игру:\n §f" + plot.getPlotName() + "\n §f \n §a [Нажмите, чтобы зайти]\n§f");
                    advertisement.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getLocaleMessage("advertisement.hover"))));
                    advertisement.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ad " + player.getWorld().getName().replace("plot","").replace("dev","")));
                        p.sendMessage(advertisement);
                }
            }
        }

        return true;
    }

}
