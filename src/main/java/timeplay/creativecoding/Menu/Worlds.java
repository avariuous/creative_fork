/*
 Creative- TimePlay 2022

 Меню Список всех миров
 Выводит в меню список всех миров
 */

package timeplay.creativecoding.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.World.GetData;

import java.util.ArrayList;
import java.util.List;

public class Worlds {

    // Показать меню
    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "Миры от игроков");
        int slot = 0;
        // Заполнение меню мирами
        for (World w: Bukkit.getWorlds()) {
            if (!(w.getName().equals("world_the_end") || w.getName().equals("world"))) {
                ItemStack worlditem = new ItemStack(GetData.icon(w));
                ItemMeta meta = worlditem.getItemMeta();
                meta.setDisplayName(GetData.title(w));
                List<String> lore = new ArrayList<String>();
                lore.add("§8ID: " + w.getName());
                lore.add(" ");
                lore.add("§7" + GetData.description(w));
                lore.add(" ");
                lore.add("§7 Онлайн: §f" + GetData.online(w));
                lore.add("§7 Создатель: §f" + GetData.owner(w));
                lore.add(" ");
                lore.add("§eНажми, чтобы подключиться");
                meta.setLore(lore);
                worlditem.setItemMeta(meta);
                if (slot < 46) {
                    menu.setItem(slot, worlditem);
                    slot++;
                }
            }
        }
        // Стеклянное заполнение
        for (int slot2 = 45; slot2 < 54; slot2++) {
            ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
            menu.setItem(slot2, glass);
        }
        // Открыть меню
        player.openInventory(menu);
        player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ENDER_CHEST_OPEN"),100,1.3f);
    }
}
