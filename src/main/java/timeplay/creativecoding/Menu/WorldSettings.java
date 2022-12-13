/*
 Creative- TimePlay 2022

 Меню Настройки мира
 Показывает доступные для измененения параметры мира
 */

package timeplay.creativecoding.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.World.GetData;

import java.util.ArrayList;
import java.util.List;

public class WorldSettings {

    // Открытие меню
    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "Настройки мира");
        World world = player.getWorld();

        // Кнопка предварительного показа Мира в списке
        ItemStack wItem = new ItemStack(GetData.icon(world));
        ItemMeta meta = wItem.getItemMeta();
        meta.setDisplayName(timeplay.creativecoding.World.GetData.title(world));
        List<String> wLore = new ArrayList<>();
        wLore.add("§8ID: " + world.getName());
        wLore.add("");
        wLore.add("§7" + GetData.description(world));
        wLore.add("");
        wLore.add("§7 Онлайн: §f" + GetData.online(world));
        wLore.add("§7 Создатель: §f" + GetData.owner(world));
        wLore.add("§7");
        wLore.add("§eТак выглядит мир в списке!");
        meta.setLore(wLore);
        wItem.setItemMeta(meta);
        menu.setItem(40, wItem);

        // Кнопка параметра переименовать мир
        ItemStack tcItem = new ItemStack(Material.NAME_TAG);
        ItemMeta tcMeta = tcItem.getItemMeta();
        tcMeta.setDisplayName("§eНазвание мира");
        List<String> tcLore = new ArrayList<>();
        tcLore.add("§8Информация мира");
        tcLore.add("");
        tcLore.add("§7 Название мира кратко отображает его");
        tcLore.add("§7 суть и отображается везде.");
        tcLore.add("§7 Примеры: §aЛучшее выживание!§7,");
        tcLore.add("§e Большой Паркур§7, §dМини-игры");
        tcLore.add("");
        tcLore.add("§eНажми, чтобы переименовать");
        tcMeta.setLore(tcLore);
        tcItem.setItemMeta(tcMeta);
        menu.setItem(10, tcItem);

        // Кнопка параметра описания
        ItemStack dItem = new ItemStack(Material.BOOK);
        List<String> dLore = new ArrayList<>();
        ItemMeta dMeta = dItem.getItemMeta();
        dMeta.setDisplayName("§6Описание мира");
        dLore.add("§8Информация мира");
        dLore.add("");
        dLore.add("§7 Описание мира немного подробнее");
        dLore.add("§7 рассказывает про сам мир. Примеры:");
        dLore.add("§b Проходи паркур и получай награды");
        dLore.add("§d Более 10 мини-игр и уровней, играй!");
        dLore.add("");
        dLore.add("§eНажми, чтобы сменить");
        dMeta.setLore(dLore);
        dItem.setItemMeta(dMeta);
        menu.setItem(11, dItem);

        // Кнопка параметра значка
        ItemStack iItem = new ItemStack(Material.EMERALD);
        List<String> iLore = new ArrayList<>();
        ItemMeta iMeta = iItem.getItemMeta();
        iMeta.setDisplayName("§fПоменять значок");
        iLore.add("§8Информация мира");
        iLore.add("");
        iLore.add("§7 Значок мира отображается в списке");
        iLore.add("§7 миров. С значком будет лучше");
        iLore.add("§7 определять и помнить мир!");
        iLore.add("");
        iLore.add("§eНажми, чтобы сменить");
        iMeta.setLore(iLore);
        iItem.setItemMeta(iMeta);
        menu.setItem(12, iItem);

        // Кнопка параметра точка спавна
        ItemStack sItem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta sMeta = sItem.getItemMeta();
        sMeta.setDisplayName("§bУстановить точку спавна");
        List<String> sLore = new ArrayList<>();
        sLore.add("§8Телепорт");
        sLore.add("");
        sLore.add("§7 Точка спавна - место, в котором");
        sLore.add("§7 игрок появляется в мире при входе.");
        sLore.add("");
        sLore.add("§eНажми, чтобы изменить");
        sMeta.setLore(sLore);
        sItem.setItemMeta(sMeta);
        menu.setItem(16, sItem);

        // Показать мир
        player.openInventory(menu);
    }
}
