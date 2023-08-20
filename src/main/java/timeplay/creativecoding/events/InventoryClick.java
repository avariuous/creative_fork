/*
 Creative TimePlay 2023

 Событие нажатия в инвентаре
 */

package timeplay.creativecoding.events;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.menu.OwnWorldsMenu;
import timeplay.creativecoding.menu.AllWorldsMenu;
import timeplay.creativecoding.menu.WorldSettingsMenu;
import timeplay.creativecoding.world.Plot;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static timeplay.creativecoding.utils.ErrorUtils.sendPlayerErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.Plot.getPlotByWorldName;
import static timeplay.creativecoding.world.Plot.teleportToPlot;
import static timeplay.creativecoding.world.PlotManager.*;

public class InventoryClick implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            Player player = (Player) event.getWhoClicked();
            // Меню настроек мира
            if (event.getInventory().getHolder() instanceof WorldSettingsMenu) {
                if (!(event.getClickedInventory().getHolder() instanceof WorldSettingsMenu)) return;
                if (event.getSlot() == 40 || item.getType() == Material.AIR) { // в 40 слоте пример мира
                    event.setCancelled(true);
                    return;
                }
                if (item.getType() == Material.EMERALD) {
                    try {
                        Material material = event.getCursor().getType();
                        Plot plot = Plot.getPlotByPlayer(player);
                        File settingsFile = getPlotConfigFile(plot);
                        FileConfiguration settingsConfig = getPlotConfig(plot);
                        if (material != Material.AIR) {
                            plot.plotIcon = material;
                            player.sendMessage(getLocaleMessage("settings.world-icon.changed"));
                            settingsConfig.set("icon", String.valueOf(material));
                            WorldSettingsMenu.openInventory(player);
                            try {
                                settingsConfig.save(settingsFile);
                            } catch (IOException error) {
                                sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения значка. " + error.getMessage());
                            }
                        } else {
                            player.sendMessage(getLocaleMessage("settings.world-icon.error"));
                            player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,2);
                        }
                    } catch (NullPointerException error) {
                        sendPlayerErrorMessage(player,"Невозможно установить значок мира. " + error.getMessage());
                    }
                }
                event.setCancelled(true);
                if (item.getType() == Material.ENDER_PEARL) {
                    player.getWorld().setSpawnLocation(player.getLocation());
                    player.sendTitle(getLocaleMessage("settings.world-spawn.title"), getLocaleMessage("settings.world-spawn.subtitle"));
                    player.closeInventory();
                } else if (item.getType() == Material.NAME_TAG) {
                    player.sendTitle(getLocaleMessage("settings.world-name.title"), getLocaleMessage("settings.world-name.subtitle"));
                    player.sendMessage(getLocaleMessage("settings.world-name.usage").replace("%player%",player.getName()));
                    player.closeInventory();
                    if (!(PlayerChat.confirmation.containsKey(player))) {
                        PlayerChat.confirmation.put(player, "title");
                    }
                } else if (item.getType() == Material.BOOK) {
                    player.sendTitle(getLocaleMessage("settings.world-description.title"), getLocaleMessage("settings.world-description.subtitle"));
                    player.sendMessage(getLocaleMessage("settings.world-description.usage"));
                    player.closeInventory();
                    if (!(PlayerChat.confirmation.containsKey(player))) {
                        PlayerChat.confirmation.put(player, "description");
                    }
                } else if (item.getType() == Material.OAK_DOOR || item.getType() == Material.IRON_DOOR) {
                    Plot plot = Plot.getPlotByPlayer(player);
                    File settingsFile = getPlotConfigFile(plot);
                    FileConfiguration settingsConfig = getPlotConfig(plot);
                    if (plot.plotSharing == Plot.sharing.PUBLIC) {
                        plot.plotSharing = Plot.sharing.PRIVATE;
                        settingsConfig.set("sharing", String.valueOf(Plot.sharing.PRIVATE));
                        player.sendMessage(getLocaleMessage("settings.world-sharing.disabled"));
                        player.playSound(player.getLocation(),Sound.BLOCK_IRON_DOOR_CLOSE,100,1);
                    } else {
                        plot.plotSharing = Plot.sharing.PUBLIC;
                        settingsConfig.set("sharing", String.valueOf(Plot.sharing.PUBLIC));
                        player.sendMessage(getLocaleMessage("settings.world-sharing.enabled"));
                        player.playSound(player.getLocation(),Sound.BLOCK_IRON_DOOR_OPEN,100,1);
                    }
                    try {
                        settingsConfig.save(settingsFile);
                        WorldSettingsMenu.openInventory(player);
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения значка. " + error.getMessage());
                    }
                }
            // Меню своих миров
            } else if (event.getInventory().getHolder() instanceof OwnWorldsMenu) {
                event.setCancelled(true);
                if (item.getType() == Material.AIR) return;
                if (!(event.getClickedInventory().getHolder() instanceof OwnWorldsMenu)) return;
                try {
                    boolean worldClicked = false;
                    if (getPlayerPlots(player).size() > 0) {
                        for (int slot : OwnWorldsMenu.worldSlots) {
                            if (event.getSlot() == slot) {
                                worldClicked = true;
                                break;
                            }
                        }
                    }
                    if (worldClicked) {
                        List lore = meta.getLore();
                        String world = "plot" + lore.get(0).toString().replace("§8ID: ", "");
                        if (!(event.getClick() == ClickType.SHIFT_RIGHT)) {
                            teleportToPlot(player,world);
                        } else {
                            player.closeInventory();
                            deletePlot(getPlotByWorldName(world),player);
                        }
                    } else {
                        if (item.getType() == Material.REPEATING_COMMAND_BLOCK) {
                            AllWorldsMenu.openInventory(player,1);
                        } else if (item.getType() == Material.SPECTRAL_ARROW) {
                            player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                            OwnWorldsMenu.openInventory(player, OwnWorldsMenu.openedPage.get(player) + 1);
                        } else if (item.getType() == Material.ARROW) {
                            player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                            OwnWorldsMenu.openInventory(player, OwnWorldsMenu.openedPage.get(player) - 1);
                        } else if (item.getType() == Material.WHITE_STAINED_GLASS) {
                            new Plot(player);
                        }
                    }
                } catch (Exception error) {
                    sendPlayerErrorMessage(player,"Произошла ошибка при обработке клика инвентаря. " + error.getMessage());
                }
            // Меню всех миров
            } else if (event.getInventory().getHolder() instanceof AllWorldsMenu) {
                event.setCancelled(true);
                if (item.getType() == Material.AIR) return;
                if (!(event.getClickedInventory().getHolder() instanceof AllWorldsMenu)) return;
                try {
                    boolean worldClicked = false;
                    if (plots.size() > 0) {
                        for (int slot : AllWorldsMenu.worldSlots) {
                            if (event.getSlot() == slot) {
                                worldClicked = true;
                                break;
                            }
                        }
                    }
                    if (worldClicked) {
                        List lore = meta.getLore();
                        String world = "plot" + lore.get(0).toString().replace("§8ID: ", "");
                        teleportToPlot(player,world);
                    } else {
                        if (item.getType() == Material.COMMAND_BLOCK) {
                            OwnWorldsMenu.openInventory(player,1);
                        } else if (item.getType() == Material.SPECTRAL_ARROW) {
                            player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                            AllWorldsMenu.openInventory(player, AllWorldsMenu.openedPage.get(player) + 1);
                        } else if (item.getType() == Material.ARROW) {
                            player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                            AllWorldsMenu.openInventory(player, AllWorldsMenu.openedPage.get(player) - 1);
                        }
                    }
                } catch (Exception error) {
                    sendPlayerErrorMessage(player,"Произошла ошибка при обработке клика инвентаря. " + error.getMessage());
                }
            }
            if (!player.getWorld().getName().startsWith("plot")) {
                if (item.getItemMeta().displayName().equals(getLocaleMessage("items.lobby.games.name")) || item.getItemMeta().displayName().equals(getLocaleMessage("items.lobby.own.name"))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof AllWorldsMenu || inventoryHolder instanceof OwnWorldsMenu || inventoryHolder instanceof WorldSettingsMenu) {
            event.setCancelled(true);
        }
    }
}
