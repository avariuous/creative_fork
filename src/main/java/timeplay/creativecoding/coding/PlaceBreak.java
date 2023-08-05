/*
 Creative TimePlay 2022

 Класс содержит события поставить или сломать блоки кодинга
 Пока-что они ничего в себе не несут.
 */

package timeplay.creativecoding.coding;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBreak implements Listener  {

    @EventHandler
    public void DiamondBlockPlace(BlockPlaceEvent e) {
        World world = e.getPlayer().getWorld();
        // Проверка блока + проверка блока на который ставят
        if ((e.getBlock().getType() == Material.DIAMOND_BLOCK) && (e.getBlockAgainst().getType() == Material.BLUE_STAINED_GLASS)) {
            int signLocation = e.getBlock().getZ() - 1;
            int blockLocation = e.getBlock().getX() - 1;
            world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signLocation).setType(Material.OAK_WALL_SIGN);
            world.getBlockAt(blockLocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.DIAMOND_ORE);
            Block block = world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signLocation);
            Sign sign = (Sign) block.getState();
            sign.setLine(1, "Событие игрока");
            sign.update();
        }
        if ((e.getBlock().getType() == Material.IRON_BLOCK) && (e.getBlockAgainst().getType() == Material.GRAY_STAINED_GLASS)) {
            int signLocation = e.getBlock().getZ() - 1;
            int blockLocation = e.getBlock().getX() - 1;
            world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signLocation).setType(Material.OAK_WALL_SIGN);
            world.getBlockAt(blockLocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.IRON_ORE);
            Block block = world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signLocation);
            Sign sign = (Sign) block.getState();
            sign.setLine(1, "Действие игрока");
            sign.update();
        }
        // Синий блок используют только для События, а не действий
        if ((e.getBlock().getType() == Material.IRON_BLOCK) && (e.getBlockAgainst().getType() == Material.BLUE_STAINED_GLASS)) {
            e.setCancelled(true);
        }
        // Серый блок используют только для Действий, Если, а не событий
        if ((e.getBlock().getType() == Material.DIAMOND_BLOCK) && (e.getBlockAgainst().getType() == Material.GRAY_STAINED_GLASS)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void DiamondBlockBreak(BlockBreakEvent e) {
        World world = e.getPlayer().getWorld();
        if (e.getBlock().getType() == Material.DIAMOND_BLOCK) {
            int glassLocation = e.getBlock().getY() - 1;
            if (world.getBlockAt(e.getBlock().getX(),glassLocation,e.getBlock().getZ()).getType() == Material.BLUE_STAINED_GLASS) {
                int signLocation = e.getBlock().getZ() - 1;
                int blockLocation = e.getBlock().getX() - 1;
                world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signLocation).setType(Material.AIR);
                world.getBlockAt(blockLocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.AIR);
            }
        }
        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
            int blockLocation = e.getBlock().getX() + 1;
            if (world.getBlockAt(blockLocation,e.getBlock().getY(),e.getBlock().getZ()).getType() == Material.DIAMOND_BLOCK) {
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType() == Material.OAK_WALL_SIGN) {
            int blockLocation = e.getBlock().getZ() + 1;
            if (world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),blockLocation).getType() == Material.DIAMOND_BLOCK) {
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType() == Material.IRON_BLOCK) {
            int glassLocation = e.getBlock().getY() - 1;
            if (world.getBlockAt(e.getBlock().getX(),glassLocation,e.getBlock().getZ()).getType() == Material.GRAY_STAINED_GLASS) {
                int signLocation = e.getBlock().getZ() - 1;
                int blockLocation = e.getBlock().getX() - 1;
                world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signLocation).setType(Material.AIR);
                world.getBlockAt(blockLocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.AIR);
            }
        }
        if (e.getBlock().getType() == Material.IRON_ORE) {
            int blockLocation = e.getBlock().getX() + 1;
            if (world.getBlockAt(blockLocation,e.getBlock().getY(),e.getBlock().getZ()).getType() == Material.IRON_BLOCK) {
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType() == Material.OAK_WALL_SIGN) {
            int blockLocation = e.getBlock().getZ() + 1;
            if (world.getBlockAt(e.getBlock().getX(),e.getBlock().getY(),blockLocation).getType() == Material.IRON_BLOCK) {
                e.setCancelled(true);
            }
        }
    }

}
