/*
 Creative TimePlay 2023

 В этом классе содержится команда /dev, которая
 позволяет создателю мира перейти в режим кодинга.

 */

package timeplay.creativecoding.commands;

import org.bukkit.*;
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
import static timeplay.creativecoding.plots.PlotManager.getPlotByPlayer;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.*;

public class CommandDev implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Plot plot = PlotManager.getPlotByPlayer(((Player) sender).getPlayer());
            if (plot == null) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                return true;
            }
            if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%", String.valueOf(getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND))));
                return true;
            }
            setCooldown(((Player) sender).getPlayer(), plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);

            List<String> developers = new ArrayList<>();
            List<String> trustedDevelopers = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.DEVELOPERS_TRUSTED);
            List<String> notTrustedDevelopers = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.DEVELOPERS_NOT_TRUSTED);
            List<String> guestsDevelopers = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.DEVELOPERS_GUESTS);

            developers.addAll(notTrustedDevelopers);
            developers.addAll(trustedDevelopers);
            developers.addAll(guestsDevelopers);

            if (args.length == 0) {
                // Проверка на владельца мира
                if (plot.owner.equalsIgnoreCase(sender.getName()) || developers.contains(sender.getName())) {
                    Player plotOwner = Bukkit.getPlayer(plot.owner);
                    if (notTrustedDevelopers.contains(sender.getName())) {
                        if (plotOwner == null) {
                            sender.sendMessage(getLocaleMessage("world.build-mode.cant-dev-when-offline"));
                            return true;
                        }
                        Plot ownerPlot = getPlotByPlayer(plotOwner);
                        if (!(ownerPlot == plot)) {
                            sender.sendMessage(getLocaleMessage("world.build-mode.cant-dev-when-offline"));
                            return true;
                        }
                    }
                    clearPlayer(((Player) sender).getPlayer());
                    sender.sendMessage(getLocaleMessage("world.dev-mode.help", ((Player) sender).getPlayer()));
                    plot.teleportToDevPlot(((Player) sender).getPlayer());
                    if (guestsDevelopers.contains(sender.getName())) {
                        ((Player) sender).setGameMode(GameMode.ADVENTURE);
                    } else {
                        ((Player) sender).setGameMode(GameMode.CREATIVE);
                    }
                    giveItems(((Player) sender).getPlayer());
                    ((Player) sender).sendTitle(getLocaleMessage("world.dev-mode.title"), getLocaleMessage("world.dev-mode.subtitle"));
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"), 100, 1.3f);
                } else {
                    sender.sendMessage(getLocaleMessage("not-owner", ((Player) sender).getPlayer()));
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
                if (notTrustedDevelopers.contains(args[0])) {
                    plot.setBuilderTrusted(args[0], true);
                    sender.sendMessage(getLocaleMessage("world.players.developers.trusted").replace("%player%", args[0]));
                } else if (trustedDevelopers.contains(args[0])) {
                    plot.removeBuilder(args[0]);
                    sender.sendMessage(getLocaleMessage("world.players.developers.removed").replace("%player%", args[0]));
                } else {
                    Player addedPlayer = Bukkit.getPlayer(args[0]);
                    if (addedPlayer != null && addedPlayer != ((Player) sender).getPlayer()) {
                        Plot plot1 = getPlotByPlayer(addedPlayer);
                        if (plot == plot1) {
                            sender.sendMessage(getLocaleMessage("world.players.developers.added").replace("%player%", addedPlayer.getName()));
                            plot.setDeveloperTrusted(addedPlayer.getName(), false);
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

    private void giveItems(Player player) {
        ItemStack eventPlayerItem = new ItemStack(Material.DIAMOND_BLOCK, 1);
        ItemMeta eventPlayerItemMeta = eventPlayerItem.getItemMeta();
        eventPlayerItemMeta.setDisplayName(getLocaleItemName("items.developer.event-player.name"));
        eventPlayerItemMeta.setLore(getLocaleItemDescription("items.developer.event-player.lore"));
        eventPlayerItem.setItemMeta(eventPlayerItemMeta);
        player.getInventory().setItem(0, eventPlayerItem);

        ItemStack actionPlayerItem = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta actionPlayerItemMeta = actionPlayerItem.getItemMeta();
        actionPlayerItemMeta.setDisplayName(getLocaleItemName("items.developer.action-player.name"));
        actionPlayerItemMeta.setLore(getLocaleItemDescription("items.developer.action-player.lore"));
        actionPlayerItem.setItemMeta(actionPlayerItemMeta);
        player.getInventory().setItem(1, actionPlayerItem);

        /*ItemStack actionVarItem = new ItemStack(Material.IRON_BLOCK,1);
        ItemMeta actionVarItemMeta = actionVarItem.getItemMeta();
        actionVarItemMeta.setDisplayName(getLocaleItemName("items.developer.action-var.name"));
        actionVarItemMeta.setLore(getLocaleItemDescription("items.developer.action-var.lore"));
        actionVarItem.setItemMeta(actionVarItemMeta);
        player.getInventory().setItem(2,actionVarItem);*/

        ItemStack varTextItem = new ItemStack(Material.BOOK, 1);
        ItemMeta varTextItemMeta = varTextItem.getItemMeta();
        varTextItemMeta.setDisplayName(getLocaleItemName("items.developer.text.name"));
        varTextItemMeta.setLore(getLocaleItemDescription("items.developer.text.lore"));
        varTextItem.setItemMeta(varTextItemMeta);
        player.getInventory().setItem(6, varTextItem);

        /*ItemStack varNumberItem = new ItemStack(Material.SLIME_BALL,1);
        ItemMeta varNumberItemMeta = varNumberItem.getItemMeta();
        varNumberItemMeta.setDisplayName(getLocaleItemName("items.developer.number.name"));
        varNumberItemMeta.setLore(getLocaleItemDescription("items.developer.number.lore"));
        varNumberItem.setItemMeta(varNumberItemMeta);
        player.getInventory().setItem(7,varNumberItem);
*/
        ItemStack worldSettingsItem = new ItemStack(Material.COMPASS, 1);
        ItemMeta worldSettingsItemMeta = worldSettingsItem.getItemMeta();
        worldSettingsItemMeta.setDisplayName(getLocaleItemName("items.developer.world-settings.name"));
        worldSettingsItemMeta.setLore(getLocaleItemDescription("items.developer.world-settings.lore"));
        worldSettingsItem.setItemMeta(worldSettingsItemMeta);
        player.getInventory().setItem(8, worldSettingsItem);
    }
}
