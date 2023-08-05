/*
 Creative TimePlay 2023

 Список всех миров. Он
 выводит в меню список всех миров
 */

package timeplay.creativecoding.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.world.Plot;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.world.PlotManager.loadedPlots;

public class AllWorldsMenu {

    // Показать меню
    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "Миры от игроков");
        int slot = 0;
        // Заполнение меню мирами
        if (loadedPlots == null) {
            ItemStack worlditem = new ItemStack(Material.BARRIER);
            ItemMeta meta = worlditem.getItemMeta();
            meta.setDisplayName("§cМиров ещё нет!");
            List<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add("§c Никто из игроков не создавал мир, ");
            lore.add("§c по-этому может ты станешь первым?");
            lore.add(" ");
            meta.setLore(lore);
            worlditem.setItemMeta(meta);
            menu.setItem(13,worlditem);
        } else {
            for (Plot plot: loadedPlots) {
                if (!(plot.world.getName().equals("world_the_end") || plot.world.getName().equals("world"))) {
                    ItemStack worlditem = new ItemStack(Plot.getWorldIcon(plot));
                    ItemMeta meta = worlditem.getItemMeta();
                    meta.setDisplayName(Plot.getWorldName(plot));
                    List<String> lore = new ArrayList<String>();
                    lore.add("§8ID: " + plot.world.getName());
                    lore.add(" ");
                    lore.add("§7" + Plot.getWorldDescription(plot));
                    lore.add(" ");
                    lore.add("§7 Онлайн: §f" + Plot.getPlayers(plot).size());
                    lore.add("§7 Создатель: §f" + Plot.getOwner(plot));
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
        }

        // Стеклянное заполнение
        for (int slot2 = 45; slot2 < 54; slot2++) {
            ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
            if (slot2 != 49) {
                menu.setItem(slot2, glass);
            } else {
                // Кнопка своих миров
                ItemStack ownw = new ItemStack(Material.CRAFTING_TABLE);
                ItemMeta ownm = glass.getItemMeta();
                ownm.setDisplayName("§6Свои миры");
                List<String> ownlore = new ArrayList<String>();
                ownlore.add("§8Список своих миров");
                ownlore.add(" ");
                ownlore.add(" §7В этом списке будут лишь");
                ownlore.add(" §7твои личные миры, которые");
                ownlore.add(" §7ты сам создал!");
                ownlore.add(" ");
                ownlore.add("§eНажми, чтобы показать список");
                ownm.setLore(ownlore);
                ownw.setItemMeta(ownm);
                menu.setItem(slot2, ownw);
            }
        }
        // Открыть меню
        player.openInventory(menu);
        player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ENDER_CHEST_OPEN"),100,1.3f);
    }
}
