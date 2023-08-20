/*
 Creative TimePlay 2022

 В этом классе содержится команда /dev, которая
 позволяет создателю мира перейти в режим кодинга.

 !!! На данной версии этот режим бесполезен, ведь ещё не придумана генерация кода
 */

package timeplay.creativecoding.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.world.Plot;

import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.commands.CommandAd.plugin;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class CommandDev implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (((Player) sender).getPlayer().getWorld().getName().contains("world")) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("only-in-world"));
                return true;
            }
            if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                ((Player) sender).getPlayer().sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(),CooldownUtils.CooldownType.GENERIC_COMMAND))));
                return true;
            }
            setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
            Plot plot = Plot.getPlotByPlayer(((Player) sender).getPlayer());
            // Проверка на владельца мира
            if (Plot.getOwner(plot).equals(sender.getName())) {
                clearPlayer(((Player) sender).getPlayer());
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                sender.sendMessage(getLocaleMessage("world.dev-mode", ((Player) sender).getPlayer()));
                ((Player) sender).sendTitle("§fРежим §6кода", "§fВы перешли в §6код§f.");
                ((Player) sender).teleport(plot.world.getSpawnLocation());
                ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"), 100, 1.3f);
                giveDevItems(((Player) sender).getPlayer());
            } else {
                sender.sendMessage(getLocaleMessage("not-owner",((Player) sender).getPlayer()));
            }
        }
        return true;
    }

    public void giveDevItems(Player player) {
        ItemStack diamondblock = new ItemStack(Material.DIAMOND_BLOCK);
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE);
        ItemStack ironblock = new ItemStack(Material.IRON_BLOCK);
        ItemStack netherbricks = new ItemStack(Material.NETHER_BRICKS);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN);
        diamondblock.getItemMeta().setDisplayName("§bСобытие игрока");
        cobblestone.getItemMeta().setDisplayName("§7Действия игрока");
        ironblock.getItemMeta().setDisplayName("§fДействия над переменными");
        netherbricks.getItemMeta().setDisplayName("§cИгровые действия");
        obsidian.getItemMeta().setDisplayName("§fЕсли переменная");
        player.getInventory().setItem(0,diamondblock);
        player.getInventory().setItem(1,cobblestone);
        player.getInventory().setItem(2,ironblock);
        player.getInventory().setItem(3,netherbricks);
        player.getInventory().setItem(4,obsidian);
    }
}
