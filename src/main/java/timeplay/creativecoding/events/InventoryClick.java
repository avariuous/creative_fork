/*
 Creative TimePlay 2023

 Событие нажатия в инвентаре
 */

package timeplay.creativecoding.events;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.menu.OwnWorldsMenu;
import timeplay.creativecoding.menu.AllWorldsMenu;
import timeplay.creativecoding.world.Plot;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InventoryClick implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            String title = event.getView().getTitle();
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            HumanEntity player = event.getWhoClicked();
            Player play = (Player) player;
            switch (title) {
                // Меню список миров от игроков
                case ("Миры от игроков"):
                    event.setCancelled(true);
                    try {
                        if (item.getType() != Material.AIR) {
                            if (item.getType() == Material.CRAFTING_TABLE) {
                                OwnWorldsMenu.openMenu(play);
                            } else {
                                List lore = meta.getLore();
                                World world = Bukkit.getWorld(lore.get(0).toString().replace("§8ID: ", ""));
                                player.teleport(world.getSpawnLocation());
                                player.setGameMode(GameMode.ADVENTURE);
                                player.getInventory().clear();
                            }
                        }
                    } catch (NullPointerException error) {}
                    break;
                case ("Твои миры"):
                    event.setCancelled(true);
                    try {
                        if (item.getType() != Material.AIR) {
                            if (item.getType() == Material.COMPASS) {
                                AllWorldsMenu.openMenu(play);
                            } else if (item.getType() == Material.NETHER_STAR) {
                                player.closeInventory();
                                new Plot((Player) player);
                            } else {
                                List lore = meta.getLore();
                                World world = Bukkit.getWorld(lore.get(0).toString().replace("§8ID: ", ""));
                                player.teleport(world.getSpawnLocation());
                                player.setGameMode(GameMode.ADVENTURE);
                                player.getInventory().clear();
                            }
                        }
                    } catch (NullPointerException error) {}
                    break;

                // Меню настроек мира
                case ("Настройки мира"):
                    if (item.getType() == Material.ENDER_PEARL) {
                        player.getWorld().setSpawnLocation(player.getLocation());
                        play.sendTitle("§bТочка спавна","§fуспешно §6изменена§f.");
                        player.closeInventory();
                    }
                    if (item.getType() == Material.NAME_TAG) {
                        play.sendTitle("§fВведи в чат","§fновое §6название§f мира.");
                        play.sendMessage(Main.prefix() + "§fНапиши в чат §6новое §fназвание мира, например: §eВыживание§f, §aБольшой паркур§f, §dСемья РП§f.");
                        player.closeInventory();
                        if (!(PlayerChat.confirmation.containsKey(player))) {
                            PlayerChat.confirmation.put(play,"title");
                        }
                    }
                    if (item.getType() == Material.BOOK) {
                        play.sendTitle("§fВведи в чат","§fновое §6описание§f мира.");
                        play.sendMessage(Main.prefix() + "§fНапиши в чат §6новое §fописание мира, например: §eЛучший мир от меня§f, §aВыживай прямо здесь!§f.");
                        player.closeInventory();
                        if (!(PlayerChat.confirmation.containsKey(player))) {
                            PlayerChat.confirmation.put(play,"description");
                        }
                    }
                    if (item.getType() == Material.EMERALD) {
                        try {
                            final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");
                            Material material = event.getCursor().getType();
                            World world = player.getWorld();
                            String worldName = world.getName();
                            File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldName + ".yml");
                            final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
                            if (material != Material.AIR) {
                                player.sendMessage(Main.prefix() + "§fЗначок изменён.");
                                worldfile.set("icon", String.valueOf(material));
                                try {
                                    worldfile.save(file);
                                } catch (IOException ignored) {}
                            }
                            else {
                                player.sendMessage(Main.prefix() + "§cНе является предметом.");
                                player.closeInventory();
                            }
                        } catch (NullPointerException ignored) {}
                        event.setCancelled(true);
                    }
                    break;
            }
        }
    }
    @EventHandler
    public void onDrag (InventoryDragEvent event) {
        try {
            if (event.getView().getTitle() == "Настройки мира") {
                if (event.getCursor().getType() == Material.AIR) {
                    event.setCancelled(true);
                }
            }
        } catch (NullPointerException ignored) {}
    }
}
