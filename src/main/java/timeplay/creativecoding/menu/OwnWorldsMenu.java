/*
 Creative TimePlay 2023

 Меню со списком личных миров игрока
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

public class OwnWorldsMenu {

    // Показать меню
    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 45, "Твои миры");
        int slot = 0;
        // Заполнение меню мирами
        if (loadedPlots != null) {
            for (Plot plot: loadedPlots) {
                if (!(plot.world.getName().equals("world_the_end") || plot.world.getName().equals("world"))) {
                    if (player.getName().equalsIgnoreCase(Plot.getOwner(plot))) {
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
                        if (slot < 37) {
                            menu.setItem(slot, worlditem);
                            slot++;
                        }
                    }
                }
            }
        }

        // Стеклянное заполнение
        for (int slot2 = 36; slot2 < 45; slot2++) {
            ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
            if (slot2 < 40 || slot2 > 41) {
                menu.setItem(slot2, glass);
            } else {
                switch(slot2) {
                    case 40:
                        ItemStack ownw = new ItemStack(Material.COMPASS);
                        ItemMeta ownm = glass.getItemMeta();
                        ownm.setDisplayName("§bВсе миры от игроков");
                        List<String> ownlore = new ArrayList<String>();
                        ownlore.add("§8Список всех миров");
                        ownlore.add(" ");
                        ownlore.add(" §7Узри все миры от игроков,");
                        ownlore.add(" §7исследуй их, а так-же играй.");
                        ownlore.add(" ");
                        ownlore.add("§eНажми, чтобы показать список");
                        ownm.setLore(ownlore);
                        ownw.setItemMeta(ownm);
                        menu.setItem(slot2, ownw);
                        break;
                    case 41:
                        ItemStack createw = new ItemStack(Material.NETHER_STAR);
                        ItemMeta createm = glass.getItemMeta();
                        createm.setDisplayName("§bСоздать свой мир");
                        List<String> createlore = new ArrayList<String>();
                        createlore.add("§8Создание мира");
                        createlore.add(" ");
                        createlore.add(" §7Попробуй создать что-то своё");
                        createlore.add(" §7уникальное для игроков.");
                        createlore.add(" ");
                        createlore.add("§eНажми, чтобы создать мир");
                        createm.setLore(createlore);
                        createw.setItemMeta(createm);
                        menu.setItem(slot2, createw);
                        break;
                }
            }
        }
        // Открыть меню
        player.openInventory(menu);
        player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ENDER_CHEST_OPEN"),100,1.7f);
    }
}
