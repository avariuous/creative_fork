package timeplay.creativecoding.Coding;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBreak implements Listener  {

    @EventHandler
    public void DiamondBlockPlace(BlockPlaceEvent e) {
        if ((e.getBlock().getType() == Material.DIAMOND_BLOCK) && (e.getBlockAgainst().getType() == Material.BLUE_STAINED_GLASS)) {
            Integer signlocation = e.getBlock().getZ() - 1;
            Integer blocklocation = e.getBlock().getX() - 1;
            String world = e.getPlayer().getName() + "1";
            Bukkit.getWorld(world).getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signlocation).setType(Material.OAK_WALL_SIGN);
            Bukkit.getWorld(world).getBlockAt(blocklocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.DIAMOND_ORE);
            Block block = Bukkit.getWorld(world).getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signlocation);
            Sign sign = (Sign) block.getState();
            sign.setLine(1, "Событие игрока");
            sign.update();
        }
        if ((e.getBlock().getType() == Material.IRON_BLOCK) && (e.getBlockAgainst().getType() == Material.GRAY_STAINED_GLASS)) {
            Integer signlocation = e.getBlock().getZ() - 1;
            Integer blocklocation = e.getBlock().getX() - 1;
            String world = e.getPlayer().getName() + "1";
            Bukkit.getWorld(world).getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signlocation).setType(Material.OAK_WALL_SIGN);
            Bukkit.getWorld(world).getBlockAt(blocklocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.IRON_ORE);
            Block block = Bukkit.getWorld(world).getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signlocation);
            Sign sign = (Sign) block.getState();
            sign.setLine(1, "Действие игрока");
            sign.update();
        }
        if ((e.getBlock().getType() == Material.IRON_BLOCK) && (e.getBlockAgainst().getType() == Material.BLUE_STAINED_GLASS)) {
            e.setCancelled(true);
        }
        if ((e.getBlock().getType() == Material.DIAMOND_BLOCK) && (e.getBlockAgainst().getType() == Material.GRAY_STAINED_GLASS)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void DiamondBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.DIAMOND_BLOCK) {
            Integer glasslocation = e.getBlock().getY() - 1;
            if (Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(e.getBlock().getX(),glasslocation,e.getBlock().getZ()).getType() == Material.BLUE_STAINED_GLASS) {
                Integer signlocation = e.getBlock().getZ() - 1;
                Integer blocklocation = e.getBlock().getX() - 1;
                Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signlocation).setType(Material.AIR);
                Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(blocklocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.AIR);
            }
        }
        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
            Integer blocklocation = e.getBlock().getX() + 1;
            if (Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(blocklocation,e.getBlock().getY(),e.getBlock().getZ()).getType() == Material.DIAMOND_BLOCK) {
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType() == Material.OAK_WALL_SIGN) {
            Integer blocklocation = e.getBlock().getZ() + 1;
            if (Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(e.getBlock().getX(),e.getBlock().getY(),blocklocation).getType() == Material.DIAMOND_BLOCK) {
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType() == Material.IRON_BLOCK) {
            Integer glasslocation = e.getBlock().getY() - 1;
            if (Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(e.getBlock().getX(),glasslocation,e.getBlock().getZ()).getType() == Material.GRAY_STAINED_GLASS) {
                Integer signlocation = e.getBlock().getZ() - 1;
                Integer blocklocation = e.getBlock().getX() - 1;
                Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(e.getBlock().getX(),e.getBlock().getY(),signlocation).setType(Material.AIR);
                Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(blocklocation,e.getBlock().getY(),e.getBlock().getZ()).setType(Material.AIR);
            }
        }
        if (e.getBlock().getType() == Material.IRON_ORE) {
            Integer blocklocation = e.getBlock().getX() + 1;
            if (Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(blocklocation,e.getBlock().getY(),e.getBlock().getZ()).getType() == Material.IRON_BLOCK) {
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType() == Material.OAK_WALL_SIGN) {
            Integer blocklocation = e.getBlock().getZ() + 1;
            if (Bukkit.getWorld(e.getPlayer().getName() + "1").getBlockAt(e.getBlock().getX(),e.getBlock().getY(),blocklocation).getType() == Material.IRON_BLOCK) {
                e.setCancelled(true);
            }
        }
    }

}
