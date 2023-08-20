package timeplay.creativecoding.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.menu.WorldSettingsMenu;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.world.Plot;

import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.PlotManager.deletePlot;

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
                        Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
                        if (sender.hasPermission("creative.delete")) {
                            if (Plot.getOwner(plot).equalsIgnoreCase(sender.getName())) {
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
                        if (args.length < 1) return true;
                        Bukkit.getLogger().info("Удаляем мир: " + args[1]);
                        if (Bukkit.getWorld(args[1]) != null) {
                            deletePlot(Plot.getPlotByWorld(Bukkit.getWorld(args[1])),null);
                        } else {
                            Bukkit.getLogger().warning("Такой мир не существует " + args[1]);
                        }
                    }
                    break;
            }
        } else {
            if (sender instanceof Player) {
                if (((Player) sender).getPlayer().getWorld().getName().contains("world")) {
                    ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                    return true;
                }
                Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
                if (Plot.getOwner(plot).equalsIgnoreCase(sender.getName())) {
                    WorldSettingsMenu.openInventory(((Player) sender).getPlayer());
                } else {
                    sender.sendMessage(getLocaleMessage("not-owner"));
                }
            } else {
                Bukkit.getLogger().info("Управление мирами: ");
                Bukkit.getLogger().info(" Удалить мир: /world delete НАЗВАНИЕМИРА ");
            }
        }
        return true;
    }
}
