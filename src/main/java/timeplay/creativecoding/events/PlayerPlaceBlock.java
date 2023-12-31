/*
 Creative TimePlay 2023

 Событие когда игрок ставит блок
 Используется для плота разработчиков
 */
package timeplay.creativecoding.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import timeplay.creativecoding.plots.DevPlot;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.plots.PlotManager.getDevPlot;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlayerPlaceBlock implements Listener {

    public List<Integer> blockActionsX = new ArrayList<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        DevPlot devPlot = getDevPlot(player);

        // Плот кодинга
        if (devPlot != null) {

            Block block = event.getBlock();
            Block blockAgainst = event.getBlockAgainst();

            if (blockAgainst.getType() == devPlot.floorBlockMaterial && !(devPlot.getAllowedBlocks().contains(block.getType()))) {
                player.sendActionBar(getLocaleMessage("world.dev-mode.cant-place-on-floor"));
                player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,1.2f);
                event.setCancelled(true);
            }

            if (blockAgainst.getType() == devPlot.eventBlockMaterial) {

                if (devPlot.getEventsBlocks().contains(block.getType())) {
                    Material additionalBlockMaterial = Material.REDSTONE_ORE;
                    String signText = "unknown";
                    switch (block.getType()) {
                        case DIAMOND_BLOCK:
                            additionalBlockMaterial = Material.DIAMOND_ORE;
                            signText = "event_player";
                            break;
                        case EMERALD_BLOCK:
                            additionalBlockMaterial = Material.EMERALD_ORE;
                            signText = "cycle";
                            break;
                        case GOLD_BLOCK:
                            additionalBlockMaterial = Material.GOLD_ORE;
                            signText = "event_world";
                            break;
                        case LAPIS_BLOCK:
                            additionalBlockMaterial = Material.LAPIS_ORE;
                            signText = "function";
                            break;
                    }
                    placeDevBlock(block,additionalBlockMaterial,signText,devPlot);
                } else {
                    player.sendActionBar(getLocaleMessage("world.dev-mode.cant-place-action-on-event"));
                    player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,1.2f);
                    event.setCancelled(true);
                }
            }

            if (blockAgainst.getType() == devPlot.actionBlockMaterial) {
                if (devPlot.getActionsBlocks().contains(block.getType()) || devPlot.getConditionBlocks().contains(block.getType())) {
                    Material additionalBlockMaterial = Material.REDSTONE_ORE;
                    String signText = "Неизвестно";
                    switch (block.getType()) {
                        case COBBLESTONE:
                            additionalBlockMaterial = Material.STONE;
                            signText = "action_player";
                            break;
                        case IRON_BLOCK:
                            additionalBlockMaterial = Material.IRON_ORE;
                            signText = "action_variable";
                            break;
                        case NETHER_BRICKS:
                            additionalBlockMaterial = Material.NETHERRACK;
                            signText = "action_world";
                            break;
                        case OAK_PLANKS:
                            additionalBlockMaterial = Material.PISTON;
                            signText = "if_player";
                            break;
                    }
                    placeDevBlock(block,additionalBlockMaterial,signText,devPlot);
                } else {
                    player.sendActionBar(getLocaleMessage("world.dev-mode.cant-place-event-on-action"));
                    player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,1.2f);
                    event.setCancelled(true);
                }
            }

            if (block.getType() == blockAgainst.getType()) {
                player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,1.2f);
                event.setCancelled(true);
            }

            if (blockAgainst.getType() == Material.DIAMOND_ORE || blockAgainst.getType() == Material.IRON_ORE  || blockAgainst.getType() == Material.STONE || blockAgainst.getType() == Material.NETHERRACK) {
                player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,1.2f);
                event.setCancelled(true);
            }

        }
    }

    public static void placeDevBlock(Block block, Material additionalBlockMaterial, String signText, DevPlot devPlot) {
        Block eastBlock = block.getRelative(BlockFace.EAST);
        eastBlock.setType(additionalBlockMaterial);
        if (eastBlock.getType() == Material.PISTON) {
            Directional data = (Directional) eastBlock.getBlockData();
            data.setFacing(BlockFace.EAST);
            eastBlock.setBlockData(data);
            Block farEastBlock = eastBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.EAST);
            farEastBlock.setType(Material.PISTON);
            data = (Directional) farEastBlock.getBlockData();
            data.setFacing(BlockFace.WEST);
            farEastBlock.setBlockData(data);
        }

    /*    if (devPlot.getActionsBlocks().contains(block.getType())) {
            devPlot.linkedPlot.script.setActionBlock(block,null);
        }*/

        Block wallSign = block.getRelative(BlockFace.SOUTH);
        wallSign.setType(Material.OAK_WALL_SIGN);

        Sign sign = (Sign) wallSign.getState();
        sign.setLine(1,signText);
        sign.update();

        Directional data = (Directional) wallSign.getBlockData();
        data.setFacing(BlockFace.SOUTH);
        wallSign.setBlockData(data);
    }

}
