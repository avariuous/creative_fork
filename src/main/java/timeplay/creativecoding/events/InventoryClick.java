/*
 Creative TimePlay 2023

 Событие нажатия в инвентаре
 */

package timeplay.creativecoding.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.coding.CodeScript;
import timeplay.creativecoding.coding.menus.PlayerActionsMenu;
import timeplay.creativecoding.coding.menus.PlayerEventsMenu;
import timeplay.creativecoding.menu.*;
import timeplay.creativecoding.menu.buttons.RadioButton;
import timeplay.creativecoding.plots.DevPlot;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.plots.Plot.*;
import static timeplay.creativecoding.utils.ErrorUtils.sendPlayerErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.plots.PlotManager.*;
import static timeplay.creativecoding.utils.MessageUtils.*;

public class InventoryClick implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null) {
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
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
                        Plot plot = getPlotByPlayer(player);
                        if (material != Material.AIR) {
                            plot.plotIcon = material;
                            player.sendMessage(getLocaleMessage("settings.world-icon.changed"));
                            setPlotConfigParameter(plot,"icon", String.valueOf(material));
                            WorldSettingsMenu.openInventory(player);
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
                    player.closeInventory();
                    if (event.getClick().isLeftClick()) {
                        player.getWorld().setSpawnLocation(player.getLocation());
                        player.sendTitle(getLocaleMessage("settings.world-spawn.title"), getLocaleMessage("settings.world-spawn.subtitle"));
                    } else {
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                } else if (item.getType() == Material.NAME_TAG) {
                    player.sendTitle(getLocaleMessage("settings.world-name.title"), getLocaleMessage("settings.world-name.subtitle"));
                    player.sendMessage(getLocaleMessage("settings.world-name.usage").replace("%player%",player.getName()));
                    player.closeInventory();
                    if (!(PlayerChat.confirmation.containsKey(player))) {
                        PlayerChat.confirmation.put(player, "title");
                    }
                } else if (item.getType() == Material.LEAD) {
                    player.sendTitle(getLocaleMessage("settings.world-id.title"), getLocaleMessage("settings.world-id.subtitle"));
                    player.sendMessage(getLocaleMessage("settings.world-id.usage").replace("%player%",player.getName()));
                    player.closeInventory();
                    if (!(PlayerChat.confirmation.containsKey(player))) {
                        PlayerChat.confirmation.put(player, "id");
                    }
                } else if (item.getType() == Material.BOOK) {
                    player.sendTitle(getLocaleMessage("settings.world-description.title"), getLocaleMessage("settings.world-description.subtitle"));
                    player.sendMessage(getLocaleMessage("settings.world-description.usage"));
                    player.closeInventory();
                    if (!(PlayerChat.confirmation.containsKey(player))) {
                        PlayerChat.confirmation.put(player, "description");
                    }
                } else if (item.getType() == Material.BOOKSHELF) {
                    WorldSettingsCategoryMenu.openInventory(player);
                } else if (item.getType() == Material.PLAYER_HEAD) {
                    WorldSettingsPlayersMenu.openInventory(player);
                } else if (item.getType() == Material.OAK_DOOR || item.getType() == Material.IRON_DOOR) {
                    Plot plot = getPlotByPlayer(player);
                    if (plot.plotSharing == Sharing.PUBLIC) {
                        plot.plotSharing = Sharing.PRIVATE;
                        setPlotConfigParameter(plot,"sharing", String.valueOf(Sharing.PRIVATE));
                        player.sendMessage(getLocaleMessage("settings.world-sharing.disabled"));
                        player.playSound(player.getLocation(),Sound.BLOCK_IRON_DOOR_CLOSE,100,1);
                    } else {
                        plot.plotSharing = Sharing.PUBLIC;
                        setPlotConfigParameter(plot,"sharing", String.valueOf(Sharing.PUBLIC));
                        player.sendMessage(getLocaleMessage("settings.world-sharing.enabled"));
                        player.playSound(player.getLocation(),Sound.BLOCK_IRON_DOOR_OPEN,100,1);
                    }
                    WorldSettingsMenu.openInventory(player);
                } else if (item.getType() == Material.PISTON) {
                    WorldSettingsFlagsMenu.openInventory(player);
                }
            } else if (event.getInventory().getHolder() instanceof WorldSettingsCategoryMenu) {
                event.setCancelled(true);
                if (item.getType() == Material.AIR) return;
                if (!(event.getClickedInventory().getHolder() instanceof WorldSettingsCategoryMenu)) return;

                Plot plot = getPlotByPlayer(player);
                File settingsFile = getPlotConfigFile(plot);
                FileConfiguration settingsConfig = getPlotConfig(plot);

                if (item.getType() == Material.SPECTRAL_ARROW) {
                    WorldSettingsMenu.openInventory(player);
                } else if (item.getType() == Material.BRICKS) {
                    plot.plotCategory = Category.SANDBOX;
                    settingsConfig.set("category", Category.SANDBOX.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.sandbox")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.LEATHER_BOOTS) {
                    plot.plotCategory = Category.ADVENTURE;
                    settingsConfig.set("category", Category.ADVENTURE.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.adventure")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.SHIELD) {
                    plot.plotCategory = Category.STRATEGY;
                    settingsConfig.set("category", Category.STRATEGY.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.strategy")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.REPEATING_COMMAND_BLOCK) {
                    plot.plotCategory = Category.ARCADE;
                    settingsConfig.set("category", Category.ARCADE.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.arcade")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.CHEST_MINECART) {
                    plot.plotCategory = Category.ROLEPLAY;
                    settingsConfig.set("category", Category.ROLEPLAY.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.roleplay")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.WRITABLE_BOOK) {
                    plot.plotCategory = Category.STORY;
                    settingsConfig.set("category", Category.STORY.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.story")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.TNT) {
                    plot.plotCategory = Category.EXPERIMENT;
                    settingsConfig.set("category", Category.EXPERIMENT.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.experiment")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                } else if (item.getType() == Material.GOLDEN_PICKAXE) {
                    plot.plotCategory = Category.SIMULATOR;
                    settingsConfig.set("category", Category.SIMULATOR.toString());
                    try {
                        settingsConfig.save(settingsFile);
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("settings.world-category.changed").replace("%category%",getLocaleMessage("world.categories.simulator")));
                    } catch (IOException error) {
                        sendPlayerErrorMessage(player,"Не удалось сохранить файл мира для изменения категории. " + error.getMessage());
                    }
                }
            } else if (event.getInventory().getHolder() instanceof WorldSettingsFlagsMenu) {
                event.setCancelled(true);

                if (item.getType() == Material.AIR) return;
                if (!(event.getClickedInventory().getHolder() instanceof WorldSettingsFlagsMenu)) return;

                Plot plot = getPlotByPlayer(player);
                if (item.getType() == Material.SPECTRAL_ARROW) {
                    WorldSettingsMenu.openInventory(player);
                } else if (item.getType() == Material.TOTEM_OF_UNDYING) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsFlagsMenu.openInventory(player);
                    }
                } else if (item.getType() == Material.CLOCK) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsFlagsMenu.openInventory(player);
                    }
                } else if (item.getType() == Material.OAK_SIGN) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsFlagsMenu.openInventory(player);
                    }
                } else if (item.getType() == Material.CAMPFIRE) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsFlagsMenu.openInventory(player);
                    }
                } else if (item.getType() == Material.WATER_BUCKET) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsFlagsMenu.openInventory(player);
                    }
                } else if (item.getType() == Material.CHEST) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsFlagsMenu.openInventory(player);
                    }
                }
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
                        List<String> lore = meta.getLore();
                        for (String loreLine : lore) {
                            if (loreLine.startsWith(getLocaleMessage("menus.all-worlds.items.world.id"))) {
                                String worldID = loreLine.replace(getLocaleMessage("menus.all-worlds.items.world.id"),"");
                                player.closeInventory();
                                if (PlotManager.getPlotByCustomID(worldID) != null) {
                                    if (!(event.getClick() == ClickType.SHIFT_RIGHT)) {
                                        teleportToPlot(player,PlotManager.getPlotByCustomID(worldID));
                                    } else {
                                        deletePlot(PlotManager.getPlotByCustomID(worldID),player);
                                    }
                                } else {
                                    player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                                    player.clearTitle();
                                    player.sendMessage(getLocaleMessage("no-plot-found",player));
                                }

                            }
                        }
                    } else {
                        if (item.getType() == Material.SPECTRAL_ARROW) {
                            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(getLocaleItemName("menus.own-worlds.items.all-worlds.name"))) {
                                AllWorldsMenu.openInventory(player,1);
                            } else {
                                player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                                OwnWorldsMenu.openInventory(player, OwnWorldsMenu.openedPage.get(player) + 1);
                            }
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
                        List<String> lore = meta.getLore();
                        for (String loreLine : lore) {
                            if (loreLine.startsWith(getLocaleMessage("menus.all-worlds.items.world.id"))) {
                                String worldID = loreLine.replace(getLocaleMessage("menus.all-worlds.items.world.id"),"");
                                player.closeInventory();
                                if (getPlotByCustomID(worldID) != null) {
                                    teleportToPlot(player,getPlotByCustomID(worldID));
                                } else {
                                    player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                                    player.clearTitle();
                                    player.sendMessage(getLocaleMessage("no-plot-found",player));
                                }
                            }
                        }
                    } else {
                        if (item.getType() == Material.COMMAND_BLOCK) {
                            OwnWorldsMenu.openInventory(player,1);
                        } else if (item.getType() == Material.SPECTRAL_ARROW) {
                            player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                            AllWorldsMenu.openInventory(player, AllWorldsMenu.openedPage.get(player) + 1);
                        } else if (item.getType() == Material.ARROW) {
                            player.playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,100,1);
                            AllWorldsMenu.openInventory(player, AllWorldsMenu.openedPage.get(player) - 1);
                        } else if (item.getType() == Material.BEACON) {
                            if (event.getClick() == ClickType.LEFT) {
                                player.sendTitle(getLocaleMessage("menus.all-worlds.items.search.title").replace("%search%",getLocaleMessage("menus.all-worlds.items.search.world-name")), getLocaleMessage("menus.all-worlds.items.search.subtitle").replace("%search%",getLocaleMessage("menus.all-worlds.items.search.world-name")));
                                player.sendMessage(getLocaleMessage("menus.all-worlds.items.search.usage",player).replace("%search%",getLocaleMessage("menus.all-worlds.items.search.world-name")));
                                player.closeInventory();
                                if (!(PlayerChat.confirmation.containsKey(player))) {
                                    PlayerChat.confirmation.put(player, "searchPlotByPlotName");
                                }
                            } else {
                                player.sendTitle(getLocaleMessage("menus.all-worlds.items.search.title").replace("%search%",getLocaleMessage("menus.all-worlds.items.search.id")), getLocaleMessage("menus.all-worlds.items.search.subtitle").replace("%search%",getLocaleMessage("menus.all-worlds.items.search.id")));
                                player.sendMessage(getLocaleMessage("menus.all-worlds.items.search.usage",player).replace("%search%",getLocaleMessage("menus.all-worlds.items.search.id")));
                                player.closeInventory();
                                if (!(PlayerChat.confirmation.containsKey(player))) {
                                    PlayerChat.confirmation.put(player, "searchPlotByID");
                                }
                            }
                        } else if (item.getType() == Material.CHEST_MINECART) {
                            int nextCategory = AllWorldsMenu.chosenCategories.get(player)+1;
                            if (nextCategory < 1 || nextCategory > 9) nextCategory = 1;
                            AllWorldsMenu.chosenCategories.put(player,nextCategory);

                            if (nextCategory == 1) AllWorldsMenu.openInventory(player,1);
                            else AllWorldsMenu.openInventory(player,1,getPlotsByCategory(AllWorldsMenu.getPlayerCategory(player)));
                        } else if (item.getType() == Material.HOPPER) {
                            int nextSort = AllWorldsMenu.chosenSorts.get(player)+1;
                            if (nextSort < 1 || nextSort > 3) nextSort = 1;

                            AllWorldsMenu.chosenSorts.put(player,nextSort);
                            AllWorldsMenu.openInventory(player,AllWorldsMenu.getCurrentPage(player),AllWorldsMenu.getCurrentPlotList(player));

                        }
                    }
                } catch (Exception error) {
                    sendPlayerErrorMessage(player,"Произошла ошибка при обработке клика инвентаря. " + error.getMessage());
                }
            } else if (event.getInventory().getHolder() instanceof PlayerEventsMenu) {

                event.setCancelled(true);
                DevPlot devPlot = getDevPlot(player);
                if (devPlot == null) return;
                if (player.getGameMode() == GameMode.ADVENTURE) {
                    cantDev(player);
                    return;
                }

                String path = getPathFromMessage("items.developer.events.",item.getItemMeta().getDisplayName());
                if (path.endsWith(".name")) {

                    String subtype = path.replace("items.developer.events.","").replace(".name","").replace("-","_");
                    Location signLocation = PlayerEventsMenu.signLocation.get(player);
                    Block eventBlock = signLocation.getBlock().getRelative(BlockFace.NORTH);

                    if (eventBlock.getType() == Material.DIAMOND_BLOCK && signLocation.getWorld().getName().contains("dev")) {

                        Plot plot = getPlotByPlayer(player);
                        plot.script.setSignLineSubtype(signLocation,subtype);
                        player.closeInventory();
                        player.sendTitle(getLocaleMessage("world.dev-mode.set-event"),item.getItemMeta().getDisplayName(),15,20,15);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1.7f);

                    }

                }

            } else if (event.getInventory().getHolder() instanceof PlayerActionsMenu) {
                event.setCancelled(true);

                DevPlot devPlot = getDevPlot(player);
                if (devPlot == null) return;
                if (player.getGameMode() == GameMode.ADVENTURE) {
                    cantDev(player);
                    return;
                }

                String path = getPathFromMessage("items.developer.actions.",item.getItemMeta().getDisplayName());
                if (path.endsWith(".name")) {

                    String subtype = path.replace("items.developer.actions.","").replace(".name","").replace("-","_");
                    Location signLocation = PlayerActionsMenu.signLocation.get(player);
                    Block actionBlock = signLocation.getBlock().getRelative(BlockFace.NORTH);

                    if (actionBlock.getType() == Material.COBBLESTONE && signLocation.getWorld().getName().contains("dev")) {

                        Plot plot = getPlotByPlayer(player);
                        plot.script.setSignLineSubtype(signLocation,subtype);
                        player.closeInventory();
                        player.sendTitle(getLocaleMessage("world.dev-mode.set-action"),item.getItemMeta().getDisplayName(),15,20,15);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1.7f);

                        Block chestBlock = actionBlock.getRelative(BlockFace.UP);
                        chestBlock.setType(Material.CHEST);
                        BlockData blockData = chestBlock.getBlockData();
                        Chest chestState = (Chest) chestBlock.getState();
                        chestState.setCustomName(subtype);
                        chestState.update();
                        ((Directional) blockData).setFacing(BlockFace.SOUTH);
                        chestBlock.setBlockData(blockData);

                    }

                }
            } else if (event.getInventory().getHolder() instanceof WorldDeleteMobsMenu) {
                event.setCancelled(true);
                Plot plot = getPlotByPlayer(player);
                if (plot == null) return;
                if (!plot.owner.equalsIgnoreCase(player.getName())) {
                    player.closeInventory();
                    return;
                }
                int count = 0;
                if (item.getType() == Material.POTATO) {
                    for (Entity entity : plot.world.getEntities()) {
                        if (entity.getType() == EntityType.DROPPED_ITEM) {
                            entity.remove();
                            count++;
                        }
                    }
                    player.closeInventory();
                    player.sendMessage(getLocaleMessage("world.delete-mobs.items").replace("%count%", String.valueOf(count)));
                } else if (item.getType() == Material.SNOWBALL) {
                    for (Entity entity : plot.world.getEntities()) {
                        switch (entity.getType()) {
                            case SNOWBALL:
                            case EGG:
                            case ENDER_PEARL:
                            case ENDER_CRYSTAL:
                            case EXPERIENCE_ORB:
                            case ENDER_SIGNAL:
                            case ARROW:
                            case SPLASH_POTION:
                            case AREA_EFFECT_CLOUD:
                            case SPECTRAL_ARROW:
                            case BOAT:
                            case MINECART:
                            case MINECART_COMMAND:
                            case FIREBALL:
                            case WITHER_SKULL:
                            case MINECART_CHEST:
                            case MINECART_FURNACE:
                            case MINECART_HOPPER:
                            case SHULKER_BULLET:
                            case FIREWORK:
                            case FISHING_HOOK:
                            case MINECART_TNT:
                            case MINECART_MOB_SPAWNER:
                            case FALLING_BLOCK:
                            case DRAGON_FIREBALL:
                            case ARMOR_STAND:
                            case PRIMED_TNT:
                                entity.remove();
                                count++;
                                break;
                        }
                    }
                    player.closeInventory();
                    player.sendMessage(getLocaleMessage("world.delete-mobs.entities").replace("%count%", String.valueOf(count)));
                } else if (item.getType() == Material.PIG_SPAWN_EGG) {
                    for (Entity entity : plot.world.getEntities()) {
                        switch (entity.getType()) {
                            case PIG:
                            case PIGLIN:
                            case PIGLIN_BRUTE:
                            case ZOMBIFIED_PIGLIN:
                            case ELDER_GUARDIAN:
                            case GUARDIAN:
                            case ZOGLIN:
                            case ZOMBIE_VILLAGER:
                            case ZOMBIE:
                            case ZOMBIE_HORSE:
                            case SKELETON:
                            case SKELETON_HORSE:
                            case WITHER_SKELETON:
                            case WITHER:
                            case SHULKER:
                            case WANDERING_TRADER:
                            case WOLF:
                            case OCELOT:
                            case CAT:
                            case SPIDER:
                            case CAVE_SPIDER:
                            case VINDICATOR:
                            case ENDER_DRAGON:
                            case ENDERMAN:
                            case ENDERMITE:
                            case SILVERFISH:
                            case SNOWMAN:
                            case IRON_GOLEM:
                            case PUFFERFISH:
                            case TROPICAL_FISH:
                            case FOX:
                            case VEX:
                            case VILLAGER:
                            case RAVAGER:
                            case EVOKER:
                            case EVOKER_FANGS:
                            case BLAZE:
                            case HOGLIN:
                            case HORSE:
                            case SLIME:
                            case STRAY:
                            case SQUID:
                            case SALMON:
                            case SHEEP:
                            case STRIDER:
                            case COW:
                            case MUSHROOM_COW:
                            case WITCH:
                            case DROWNED:
                            case PANDA:
                            case PARROT:
                            case PILLAGER:
                            case POLAR_BEAR:
                            case CHICKEN:
                            case CREEPER:
                            case GHAST:
                            case GIANT:
                            case MAGMA_CUBE:
                            case PHANTOM:
                            case DONKEY:
                            case DOLPHIN:
                            case TURTLE:
                            case ILLUSIONER:
                            case RABBIT:
                                entity.remove();
                                count++;
                                break;
                        }
                    }
                    player.closeInventory();
                    player.sendMessage(getLocaleMessage("world.delete-mobs.mobs").replace("%count%", String.valueOf(count)));
                }
            } else if (event.getInventory().getHolder() instanceof WorldSettingsPlayersMenu) {
                event.setCancelled(true);
                Plot plot = getPlotByPlayer(player);

                String selectedPlayer = WorldSettingsPlayersMenu.playersSelected.get(player);

                if (plot == null) return;
                if (item.getType() == Material.SPECTRAL_ARROW) {
                    WorldSettingsPlayersMenu.openInventory(player, WorldSettingsPlayersMenu.openedPage.get(player) + 1);
                } else if (item.getType() == Material.ARROW) {
                    WorldSettingsPlayersMenu.openInventory(player, WorldSettingsPlayersMenu.openedPage.get(player) - 1);
                } else if (item.getType() == Material.PLAYER_HEAD) {
                    boolean playerClicked = false;
                        for (int slot : WorldSettingsPlayersMenu.playerSlots) {
                            if (event.getSlot() == slot) {
                                playerClicked = true;
                                break;
                        }
                    }
                    if (playerClicked) {
                        WorldSettingsPlayersMenu.playersSelected.put(player,ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                        WorldSettingsPlayersMenu.openInventory(player,WorldSettingsPlayersMenu.openedPage.get(player));
                    }
                } else if (item.getType() == Material.BARRIER && event.getSlot() == 16) {
                    player.closeInventory();
                    player.sendMessage(getLocaleMessage("world.players.black-list.added").replace("%player%",selectedPlayer));
                    plot.addBlacklist(selectedPlayer);
                } else if (item.getType() == Material.STRUCTURE_VOID) {
                    if (event.getSlot() == 16) {
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("world.players.black-list.removed").replace("%player%",selectedPlayer));
                        plot.removeBlacklist(selectedPlayer);
                    } else {
                        player.closeInventory();
                        player.sendMessage(getLocaleMessage("world.players.kick.kicked").replace("%player%",selectedPlayer));
                        Player plotPlayer = Bukkit.getPlayer(selectedPlayer);
                        if (plotPlayer != null) {
                            if (getPlotByPlayer(plotPlayer) == plot) {
                                plot.kickPlayer(plotPlayer);
                            }
                        }
                    }

                } else if (item.getType() == Material.FEATHER || item.getType() == Material.BRICKS || item.getType() == Material.COMMAND_BLOCK) {
                    if (plot == null) return;
                    RadioButton rd = RadioButton.getRadioButtonByItemStack(item);
                    if (rd != null) {
                        rd.onChoice();
                        player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,100,1);
                        WorldSettingsPlayersMenu.openInventory(player);
                    }
                }
            }
            if (!player.getWorld().getName().startsWith("plot")) {
                if (item.getItemMeta().displayName().equals(getLocaleMessage("items.lobby.games.name")) || item.getItemMeta().displayName().equals(getLocaleMessage("items.lobby.own.name"))) {
                    event.setCancelled(true);
                }
            }
        }
        DevPlot devPlot = getDevPlot(player);
        if (devPlot == null) return;
        if (player.getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);
            cantDev(player);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof AllWorldsMenu || inventoryHolder instanceof OwnWorldsMenu || inventoryHolder instanceof WorldSettingsMenu) {
            event.setCancelled(true);
        }
    }

    private static void cantDev(Player player) {
        player.closeInventory();
        player.sendActionBar(getLocaleMessage("world.dev-mode.cant-dev"));
    }
}
