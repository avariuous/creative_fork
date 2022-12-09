package timeplay.creativecoding.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Worlds {

    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "Миры от игроков");
        int slot = 0;
        for (World w: Bukkit.getWorlds()) {
            if (!(w.getName().equals("world_the_end") || w.getName().equals("world"))) {
                ItemStack item = new ItemStack(Material.DIAMOND);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§7Мир§f " + w.getName());
                List<String> lore = new ArrayList<String>();
                lore.add("§8ID: " + w.getName());
                lore.add(" ");
                lore.add("§7 Мой прекрасный мир!");
                lore.add(" ");
                lore.add("§7 Мир: §f" + w.getName());
                lore.add("§7 Онлайн: §f" + Bukkit.getOnlinePlayers().stream().filter(player1 -> player1.getWorld().equals(w)).collect(Collectors.toList()).size());
                lore.add("§7 Посетителей: §fСкоро");
                lore.add(" ");
                lore.add("§eНажми, чтобы подключиться");
                meta.setLore(lore);
                item.setItemMeta(meta);
                if (slot < 54) {
                    menu.setItem(slot, item);
                    slot++;
                }
            }
        }
        player.openInventory(menu);
        player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ENDER_CHEST_OPEN"),100,1.3f);
    }
}
