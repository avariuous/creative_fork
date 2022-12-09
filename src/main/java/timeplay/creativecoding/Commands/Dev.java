package timeplay.creativecoding.Commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Dev implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            World world = ((Player) sender).getWorld();
            ((Player) sender).setGameMode(GameMode.CREATIVE);
            ((Player) sender).getPlayer().getInventory().clear();
            ((Player) sender).sendMessage("§f Вы перешли в режим §6редактирования кода§f.");
            ((Player) sender).sendTitle("§fРежим §6кода","§fВы перешли в §6код§f.");
            ((Player) sender).teleport(world.getSpawnLocation());
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf("BLOCK_BEACON_POWER_SELECT"),100,1.3f);
            devItems(((Player) sender).getPlayer());
        }
        return true;
    }

    public void devItems(Player player) {
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
