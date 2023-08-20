/*
 Creative TimePlay 2022

 Событие использования
 */

package timeplay.creativecoding.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import timeplay.creativecoding.menu.AllWorldsMenu;
import timeplay.creativecoding.menu.OwnWorldsMenu;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.NETHER_STAR && event.getPlayer().getWorld().getName().equals("world") && !(event.getPlayer().getCooldown(Material.NETHER_STAR) > 0)) {
            event.getPlayer().setCooldown(Material.NETHER_STAR,60);
            OwnWorldsMenu.openInventory(event.getPlayer(),1);
        }
    }

    @EventHandler
    public void compass(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.COMPASS) && event.getPlayer().getWorld().getName().equals("world") && !(event.getPlayer().getCooldown(Material.COMPASS) > 0)) {
            event.getPlayer().setCooldown(Material.COMPASS,60);
            AllWorldsMenu.openInventory(event.getPlayer(),1);
        }
    }
}
