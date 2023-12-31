/*
 Creative TimePlay 2023

 Событие ломания блока
 Используется для плота разработчиков
 */
package timeplay.creativecoding.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import timeplay.creativecoding.plots.DevPlot;

import static timeplay.creativecoding.plots.PlotManager.getDevPlot;
public class PlayerBreakBlock implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        DevPlot devPlot = getDevPlot(player);
        if (devPlot != null) {
            Block block = event.getBlock();

            if (devPlot.getIndestructibleBlocks().contains(block.getType())) {
                player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,100,1.2f);
                event.setCancelled(true);
            }

            if (devPlot.getAllCodingBlocksForPlacing().contains(block.getType())) {

                Block chestBlock = block.getRelative(BlockFace.UP);
                Block additionalBlock = block.getRelative(BlockFace.EAST);
                Block signBlock = block.getRelative(BlockFace.SOUTH);

                additionalBlock.setType(Material.AIR);
                signBlock.setType(Material.AIR);
                if (chestBlock.getType() == Material.CHEST) {
                    Chest chest = (Chest) chestBlock.getState();
                    for (ItemStack item : chest.getBlockInventory().getContents()) {
                        chestBlock.getWorld().dropItem(chestBlock.getLocation(),item);
                    }
                    chestBlock.setType(Material.AIR);
                }
            }

            if (block.getType() == Material.OAK_WALL_SIGN) {
                event.setCancelled(true);
            }

        }
    }
}
