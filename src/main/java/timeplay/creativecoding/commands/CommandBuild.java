/*
 Creative TimePlay 2023

 В этом классе содержится команда /build, которая
 позволяет создателю мира включить режим строительства.
 */

package timeplay.creativecoding.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.plots.PlotManager.getDevPlot;
import static timeplay.creativecoding.plots.PlotManager.getPlotByPlayer;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.*;

public class CommandBuild implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Plot plot = getPlotByPlayer(((Player) sender).getPlayer());
            if (plot == null) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                return true;
            }
            if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(),CooldownUtils.CooldownType.GENERIC_COMMAND))));
                return true;
            }
            setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
            List<String> builders = new ArrayList<>();
            List<String> trustedBuilders = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.BUILDERS_TRUSTED);
            List<String> notTrustedBuilders = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.BUILDERS_NOT_TRUSTED);
            builders.addAll(notTrustedBuilders);
            builders.addAll(trustedBuilders);


            if (args.length == 0) {
                if (!(plot.plotMode == Plot.Mode.BUILD)) {
                    if (plot.owner.equalsIgnoreCase(sender.getName()) || builders.contains(sender.getName())) {
                        Player plotOwner = Bukkit.getPlayer(plot.owner);
                        if (notTrustedBuilders.contains(sender.getName())) {
                            if (plotOwner == null) {
                                sender.sendMessage(getLocaleMessage("world.build-mode.cant-build-when-offline"));
                                return true;
                            }
                            Plot ownerPlot = getPlotByPlayer(plotOwner);
                            if (!(ownerPlot == plot)) {
                                sender.sendMessage(getLocaleMessage("world.build-mode.cant-build-when-offline"));
                                return true;
                            }
                        }
                        for (Player p : plot.getPlayers()){
                            if (getDevPlot(p) == null) {
                                clearPlayer(p);
                                p.sendMessage(getLocaleMessage("world.build-mode.help"));
                                p.sendTitle(getLocaleMessage("world.build-mode.title"),getLocaleMessage("world.build-mode.subtitle"));
                                p.teleport(plot.world.getSpawnLocation());
                                p.playSound(p.getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"),100,1.7f);
                                if (builders.contains(p)) {
                                    if (notTrustedBuilders.contains(p.getName())) {
                                        if (plotOwner != null) {
                                            Plot ownerPlot = getPlotByPlayer(plotOwner);
                                            if (ownerPlot == plot) {
                                                p.setGameMode(GameMode.CREATIVE);
                                            }
                                        }
                                    } else {
                                        p.setGameMode(GameMode.CREATIVE);
                                    }
                                }
                            }
                        }
                        ((Player) sender).setGameMode(GameMode.CREATIVE);
                        if (plot.owner.equalsIgnoreCase(sender.getName())) {
                            ItemStack worldSettingsItem = new ItemStack(Material.COMPASS,1);
                            ItemMeta worldSettingsItemMeta = worldSettingsItem.getItemMeta();
                            worldSettingsItemMeta.setDisplayName(getLocaleItemName("items.developer.world-settings.name"));
                            worldSettingsItemMeta.setLore(getLocaleItemDescription("items.developer.world-settings.lore"));
                            worldSettingsItem.setItemMeta(worldSettingsItemMeta);
                            ((Player) sender).getPlayer().getInventory().setItem(8,worldSettingsItem);
                        }
                    }
                } else {
                    clearPlayer(((Player) sender));
                    sender.sendMessage(getLocaleMessage("world.build-mode.help"));
                    ((Player) sender).sendTitle(getLocaleMessage("world.build-mode.title"),getLocaleMessage("world.build-mode.subtitle"));
                    ((Player) sender).teleport(plot.world.getSpawnLocation());
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"),100,1.7f);
                    if (plot.owner.equalsIgnoreCase(sender.getName()) || builders.contains(sender.getName())) {
                        Player plotOwner = Bukkit.getPlayer(plot.owner);
                        if (notTrustedBuilders.contains(sender.getName())) {
                            if (plotOwner == null) {
                                sender.sendMessage(getLocaleMessage("world.build-mode.cant-build-when-offline"));
                                return true;
                            }
                            Plot ownerPlot = getPlotByPlayer(plotOwner);
                            if (!(ownerPlot == plot)) {
                                sender.sendMessage(getLocaleMessage("world.build-mode.cant-build-when-offline"));
                                return true;
                            }
                        }
                        if (plot.owner.equalsIgnoreCase(sender.getName())) {
                            ItemStack worldSettingsItem = new ItemStack(Material.COMPASS,1);
                            ItemMeta worldSettingsItemMeta = worldSettingsItem.getItemMeta();
                            worldSettingsItemMeta.setDisplayName(getLocaleItemName("items.developer.world-settings.name"));
                            worldSettingsItemMeta.setLore(getLocaleItemDescription("items.developer.world-settings.lore"));
                            worldSettingsItem.setItemMeta(worldSettingsItemMeta);
                            ((Player) sender).getPlayer().getInventory().setItem(8,worldSettingsItem);
                        }
                        ((Player) sender).setGameMode(GameMode.CREATIVE);
                    }
                }
            } else {
                if (!plot.owner.equalsIgnoreCase(sender.getName())) {
                    sender.sendMessage(getLocaleMessage("not-owner"));
                    return true;
                }
                if (plot.owner.equalsIgnoreCase(args[0])) {
                    sender.sendMessage(getLocaleMessage("same-player"));
                    return true;
                }
                if (notTrustedBuilders.contains(args[0])) {
                    plot.setBuilderTrusted(args[0],true);
                    sender.sendMessage(getLocaleMessage("world.players.builders.trusted").replace("%player%", args[0]));
                } else if (trustedBuilders.contains(args[0])) {
                    plot.removeBuilder(args[0]);
                    sender.sendMessage(getLocaleMessage("world.players.builders.removed").replace("%player%", args[0]));
                } else {
                    Player addedPlayer = Bukkit.getPlayer(args[0]);
                    if (addedPlayer != null && addedPlayer != ((Player) sender).getPlayer()) {
                        Plot plot1 = getPlotByPlayer(addedPlayer);
                        if (plot == plot1) {
                            sender.sendMessage(getLocaleMessage("world.players.builders.added").replace("%player%", addedPlayer.getName()));
                            plot.setBuilderTrusted(addedPlayer.getName(),true);
                            addedPlayer.setGameMode(GameMode.CREATIVE);
                        } else {
                            sender.sendMessage(getLocaleMessage("no-player-found"));
                        }
                    } else {
                        sender.sendMessage(getLocaleMessage("no-player-found"));
                    }
                }
            }
        }
        return true;
    }
}
