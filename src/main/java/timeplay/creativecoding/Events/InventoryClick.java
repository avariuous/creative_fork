/*
 Creative- TimePlay 2022

 Событие нажатия в инвентаре
 */

package timeplay.creativecoding.Events;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;

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
                case ("Миры от игроков"):
                    event.setCancelled(true);
                    try {
                        if (item.getType() != Material.GRAY_STAINED_GLASS_PANE || item.getType() != Material.AIR) {
                            List lore = meta.getLore();
                            World world = Bukkit.getWorld(lore.get(0).toString().replace("§8ID: ", ""));
                            player.teleport(world.getSpawnLocation());
                            player.setGameMode(GameMode.ADVENTURE);
                            player.getInventory().clear();
                        }
                    } catch (NullPointerException error) {}
                    break;
                case ("Настройки мира"):
                    if (item.getType() == Material.ENDER_PEARL) {
                        player.getWorld().setSpawnLocation(player.getLocation());
                        play.sendTitle("§bТочка спавна","§fуспешно §6изменена§f.");
                        player.closeInventory();
                        event.setCancelled(true);
                    }
                    if (item.getType() == Material.NAME_TAG) {
                        play.sendTitle("§fВведи в чат","§fновое §6название§f мира.");
                        play.sendMessage(Main.prefix() + "§fНапиши в чат §6новое §fназвание мира, например: §eВыживание§f, §aБольшой паркур§f, §dСемья РП§f.");
                        player.closeInventory();
                        if (!(PlayerChat.confirmation.containsKey(player))) {
                            PlayerChat.confirmation.put(play,"title");
                        }
                        event.setCancelled(true);
                    }
                    if (item.getType() == Material.BOOK) {
                        play.sendTitle("§fВведи в чат","§fновое §6описание§f мира.");
                        play.sendMessage(Main.prefix() + "§fНапиши в чат §6новое §fописание мира, например: §eЛучший мир от меня§f, §aВыживай прямо здесь!§f.");
                        player.closeInventory();
                        if (!(PlayerChat.confirmation.containsKey(player))) {
                            PlayerChat.confirmation.put(play,"description");
                        }
                        event.setCancelled(true);
                    }
                    if (item.getType() == Material.EMERALD) {
                        try {
                            final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");
                            Material material = event.getCursor().getType();
                            World world = player.getWorld();
                            String worldName = world.getName();
                            File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldName + ".yml");
                            final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
                            if (!(material.isEmpty()) || material != Material.AIR || material != Material.GRAY_STAINED_GLASS_PANE) {
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
}
