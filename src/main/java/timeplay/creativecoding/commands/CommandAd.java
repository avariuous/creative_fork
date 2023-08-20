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
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.world.Plot;

import java.io.File;

import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.Plot.teleportToPlot;
import static timeplay.creativecoding.world.PlotManager.loadPlot;

public class CommandAd implements CommandExecutor {

    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");
    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            Plot plot = Plot.getPlotByPlayer(player);
            if (args.length == 1) {
                if (getCooldown(player, CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                    player.sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(player,CooldownUtils.CooldownType.GENERIC_COMMAND))));
                    return true;
                }
                setCooldown(player,plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
                File[] worldsFolders = getWorldsFolders(true);
                if (worldsFolders.length > 0) {
                    boolean worldExists = false;
                    for (File world : worldsFolders) {
                        if (world.getName().replace("plot","").equals(args[0])) {
                            worldExists = true;
                            break;
                        }
                    }
                    if (worldExists) {
                        String worldName = "plot" + args[0];
                        teleportToPlot(player,worldName);
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
                if (!player.getWorld().getName().startsWith("plot")) {
                    player.sendMessage(getLocaleMessage("only-in-world"));
                    return true;
                }
                if (getCooldown(player, CooldownUtils.CooldownType.ADVERTISEMENT_COMMAND) > 0) {
                    player.sendMessage(getLocaleMessage("advertisement.cooldown").replace("%cooldown%",String.valueOf(getCooldown(player,CooldownUtils.CooldownType.ADVERTISEMENT_COMMAND))));
                    return true;
                }
                setCooldown(player,plugin.getConfig().getInt("cooldowns.advertisement"), CooldownUtils.CooldownType.ADVERTISEMENT_COMMAND);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    TextComponent advertisement = new TextComponent("§7 \n§7 " + player.getName() + "§7 приглашает вас в игру:\n §f" + Plot.getPlotName(plot) + "\n §f \n §a [Нажмите, чтобы зайти]\n§f");
                    advertisement.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getLocaleMessage("advertisement.hover"))));
                    advertisement.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ad " + player.getWorld().getName().replace("plot","")));
                        p.sendMessage(advertisement);
                }
            }
        }

        return true;
    }

}
