/*
 Creative TimePlay 2023

 Команда /world
 */
package timeplay.creativecoding.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.menu.WorldDeleteMobsMenu;
import timeplay.creativecoding.menu.WorldSettingsMenu;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.plots.Plot;

import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getElapsedTime;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.plots.PlotManager.deletePlot;

public class CommandWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player) {
                if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                    ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(),CooldownUtils.CooldownType.GENERIC_COMMAND))));
                    return true;
                }
                setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
            }
            // Delete
            switch(args[0]) {
                case "delete":
                    // Игрок
                    if (sender instanceof Player) {
                        Plot plot = PlotManager.getPlotByPlayer(((Player) sender).getPlayer());
                        if (sender.hasPermission("creative.delete")) {
                            if (plot.getOwner().equalsIgnoreCase(sender.getName())) {
                                deletePlot(plot, ((Player) sender).getPlayer());
                            } else {
                                // Обход на удаление для тех, у кого есть право
                                if (sender.hasPermission("creative.deletebypass")) deletePlot(plot, ((Player) sender).getPlayer());
                            }
                        } else {
                            sender.sendMessage(getLocaleMessage("no-perms"));
                        }
                    // Консоль
                    } else {
                        if (args.length == 1) return true;
                        Bukkit.getLogger().info("Удаляем мир: " + args[1]);
                        if (Bukkit.getWorld(args[1]) != null) {
                            deletePlot(PlotManager.getPlotByWorld(Bukkit.getWorld(args[1])),null);
                        } else {
                            Bukkit.getLogger().warning("Такой мир не существует " + args[1]);
                        }
                    }
                    break;
                case "deletemobs":
                    if (sender instanceof Player) {
                        Plot plot = PlotManager.getPlotByPlayer(((Player) sender).getPlayer());
                        if (plot != null && (plot.getOwner().equalsIgnoreCase(sender.getName()))) {
                            WorldDeleteMobsMenu.openInventory(((Player) sender).getPlayer());
                        }
                    }
                    break;
                case "info":
                    Plot plot = PlotManager.getPlotByPlayer(((Player) sender).getPlayer());
                    if (plot == null) return true;
                    long now = System.currentTimeMillis();
                    sender.sendMessage(getLocaleMessage("world.info").replace("%name%",plot.plotName)
                            .replace("%id%", plot.worldID).replace("%creation-time%",getElapsedTime(now,plot.getCreationTime()))
                            .replace("%activity-time%",getElapsedTime(now,plot.getLastActivityTime())).replace("%online%",String.valueOf(plot.getOnline()))
                            .replace("%builders%",plot.getBuilders()).replace("%coders%",plot.getDevelopers()).replace("%owner%",plot.getOwner()));
                    break;
            }
        } else {
            if (sender instanceof Player) {
                Plot plot = PlotManager.getPlotByPlayer(((Player) sender).getPlayer());
                if (plot == null) {
                    ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                    return true;
                }
                if (plot.getOwner().equalsIgnoreCase(sender.getName())) {
                    WorldSettingsMenu.openInventory(((Player) sender).getPlayer());
                } else {
                    long now = System.currentTimeMillis();
                    sender.sendMessage(getLocaleMessage("world.info").replace("%name%",plot.plotName)
                            .replace("%id%", plot.worldID).replace("%creation-time%",getElapsedTime(now,plot.getCreationTime()))
                            .replace("%activity-time%",getElapsedTime(now,plot.getLastActivityTime())).replace("%online%",String.valueOf(plot.getOnline()))
                            .replace("%builders%",plot.getBuilders()).replace("%coders%",plot.getDevelopers()).replace("%owner%",plot.getOwner()));
                }
            } else {
                Bukkit.getLogger().info("Управление мирами: ");
                Bukkit.getLogger().info(" Удалить мир: /world delete НАЗВАНИЕМИРА ");
            }
        }
        return true;
    }
}
