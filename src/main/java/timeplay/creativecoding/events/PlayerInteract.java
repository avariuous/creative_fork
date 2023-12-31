/*
 Creative TimePlay 2022

 Событие использования
 */

package timeplay.creativecoding.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import timeplay.creativecoding.coding.BlockParser;
import timeplay.creativecoding.coding.menus.PlayerActionsMenu;
import timeplay.creativecoding.coding.menus.PlayerEventsMenu;
import timeplay.creativecoding.menu.AllWorldsMenu;
import timeplay.creativecoding.menu.OwnWorldsMenu;
import timeplay.creativecoding.menu.WorldSettingsMenu;
import timeplay.creativecoding.plots.DevPlot;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.utils.FileUtils;

import java.util.List;

import static timeplay.creativecoding.plots.PlotManager.getDevPlot;
import static timeplay.creativecoding.plots.PlotManager.getPlotByPlayer;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().getItemInHand().getType() == Material.NETHER_STAR && event.getPlayer().getWorld().getName().equals("world") && !(event.getPlayer().getCooldown(Material.NETHER_STAR) > 0)) {
            event.getPlayer().setCooldown(Material.NETHER_STAR,60);
            OwnWorldsMenu.openInventory(event.getPlayer(),1);
        }

        DevPlot devPlot = getDevPlot(player);
        if (devPlot != null) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock == null) return;
            Block mainBlock = clickedBlock.getRelative(BlockFace.NORTH);

            if (clickedBlock.getType() == Material.OAK_WALL_SIGN) {
                String mainBlockType = new BlockParser().getMainTypeByMaterial(mainBlock.getType());
                String actionBlockType = new BlockParser().getActionBlockType(mainBlock.getType());

                if (mainBlockType.startsWith("event")) {
                    PlayerEventsMenu.openInventory(player,1,event.getClickedBlock().getLocation());
                } else if (actionBlockType.startsWith("action")) {
                    PlayerActionsMenu.openInventory(player,1,event.getClickedBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void compass(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && (event.getPlayer().getItemInHand().getType() == Material.COMPASS)
                && event.getPlayer().getWorld().getName().equals("world")
                && !(event.getPlayer().getCooldown(Material.COMPASS) > 0)) {
            event.getPlayer().setCooldown(Material.COMPASS,60);
            AllWorldsMenu.openInventory(event.getPlayer(),1);
        }

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && event.getPlayer().getItemInHand().getType() == Material.COMPASS
                && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(getLocaleItemName("items.developer.world-settings.name"))
                && !(event.getPlayer().getCooldown(Material.COMPASS) > 0)) {
            Plot plot = getPlotByPlayer(event.getPlayer());
            if (plot == null) return;
            if (plot.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
                event.getPlayer().setCooldown(Material.COMPASS,60);
                WorldSettingsMenu.openInventory(event.getPlayer());
            }
        }

        Plot plot = getPlotByPlayer(event.getPlayer());
        if (plot != null) {
            if (!event.getPlayer().getWorld().getName().contains("dev") && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL)) {
                if (!(plot.getOwner().equalsIgnoreCase(event.getPlayer().getName()) || plot.getBuilders().contains(event.getPlayer().getName()))) {
                    switch(plot.blockInteractFlag) {
                        case 2:
                            event.getPlayer().sendActionBar(getLocaleMessage("world.cant-block-interact"));
                            event.setCancelled(true);
                            break;
                        case 3:
                            if (event.getClickedBlock().getType() == Material.COMPARATOR || event.getClickedBlock().getType() == Material.REPEATER || event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
                                event.getPlayer().sendActionBar(getLocaleMessage("world.cant-block-interact"));
                                event.setCancelled(true);
                            }
                            break;
                        case 4:
                            if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType().toString().contains("DOOR")) {
                                event.getPlayer().sendActionBar(getLocaleMessage("world.cant-block-interact"));
                                event.setCancelled(true);
                            }
                            break;
                        case 5:
                            if (event.getClickedBlock().getType().toString().contains("BUTTON") || event.getClickedBlock().getType().toString().contains("PRESSURE_PLATE") || event.getClickedBlock().getType() == Material.LEVER) {
                                event.getPlayer().sendActionBar(getLocaleMessage("world.cant-block-interact"));
                                event.setCancelled(true);
                            }
                            break;
                    }
                }
            }
        }


    }//items.developer.world-settings.name"
}
