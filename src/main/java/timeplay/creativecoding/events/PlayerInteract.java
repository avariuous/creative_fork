/*
 Creative TimePlay 2022

 Событие использования
 */

package timeplay.creativecoding.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import timeplay.creativecoding.menu.OwnWorldsMenu;

import static timeplay.creativecoding.menu.AllWorldsMenu.openMenu;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.NETHER_STAR && event.getPlayer().getWorld().getName().equals("world")) {
            Player player = event.getPlayer();
            OwnWorldsMenu.openMenu(player);
        }
    }

    @EventHandler
    public void compass(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.COMPASS) && event.getPlayer().getWorld().getName().equals("world")) {
            openMenu(event.getPlayer());
        }
    }
}
