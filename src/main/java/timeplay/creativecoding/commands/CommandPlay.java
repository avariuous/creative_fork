/*
 Creative TimePlay 2023

 В этом классе содержится команда /play,
 которая должна запустить игру.

*/
package timeplay.creativecoding.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.coding.BlockParser;
import timeplay.creativecoding.coding.activators.PlayerJoinActivator;
import timeplay.creativecoding.coding.activators.PlayerQuitActivator;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FileUtils.copyFileToDirectory;
import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.plots.PlotManager.getDevPlot;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.*;


public class CommandPlay implements CommandExecutor {

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
            // Проверка на владельца мира

            if (!(plot.plotMode == Plot.Mode.PLAYING)) {

                List<String> developers = new ArrayList<>();
                List<String> trustedDevelopers = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.DEVELOPERS_TRUSTED);
                List<String> notTrustedDevelopers = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.DEVELOPERS_NOT_TRUSTED);

                developers.addAll(notTrustedDevelopers);
                developers.addAll(trustedDevelopers);

                if (plot.getOwner().equals(sender.getName()) || developers.contains(sender.getName())) {
                    plot.plotMode = Plot.Mode.PLAYING;
                    for (Player p : plot.getPlayers()) {
                        if (getDevPlot(p) == null) {
                            clearPlayer(p);
                            p.sendMessage(getLocaleMessage("world.play-mode"));
                            p.teleport(plot.world.getSpawnLocation());
                        }
                    }
                    if (plot.script.exists() && plot.devPlot.isLoaded) {
                        BlockParser.parseCode(plot.devPlot);
                    }

                    if (plot.getOwner().equalsIgnoreCase(sender.getName())) {
                        ItemStack worldSettingsItem = new ItemStack(Material.COMPASS,1);
                        ItemMeta worldSettingsItemMeta = worldSettingsItem.getItemMeta();
                        worldSettingsItemMeta.setDisplayName(getLocaleItemName("items.developer.world-settings.name"));
                        worldSettingsItemMeta.setLore(getLocaleItemDescription("items.developer.world-settings.lore"));
                        worldSettingsItem.setItemMeta(worldSettingsItemMeta);
                        ((Player) sender).getInventory().setItem(8,worldSettingsItem);
                    }
                    for (Player p : plot.getPlayers()) {
                        if (getDevPlot(p) == null) {
                            plot.script.executeActivator(new PlayerQuitActivator(), p);
                            plot.script.executeActivator(new PlayerJoinActivator(), p);
                        }
                    }
                } else {
                    sender.sendMessage(getLocaleMessage("not-owner", ((Player) sender).getPlayer()));
                }
            } else {
                if (plot.getOwner().equals(sender.getName())) {
                    clearPlayer(((Player) sender).getPlayer());
                    if (plot.script.exists() && plot.devPlot.isLoaded) {
                        ItemStack worldSettingsItem = new ItemStack(Material.COMPASS,1);
                        ItemMeta worldSettingsItemMeta = worldSettingsItem.getItemMeta();
                        worldSettingsItemMeta.setDisplayName(getLocaleItemName("items.developer.world-settings.name"));
                        worldSettingsItemMeta.setLore(getLocaleItemDescription("items.developer.world-settings.lore"));
                        worldSettingsItem.setItemMeta(worldSettingsItemMeta);
                        ((Player) sender).getPlayer().getInventory().setItem(8,worldSettingsItem);
                        BlockParser.parseCode(plot.devPlot);
                    }
                }
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("world.play-mode"));
                ((Player) sender).getPlayer().teleport(plot.world.getSpawnLocation());
                plot.script.executeActivator(new PlayerQuitActivator(), ((Player) sender).getPlayer());
                plot.script.executeActivator(new PlayerJoinActivator(), ((Player) sender).getPlayer());
            }

        }
        return true;
    }
}
