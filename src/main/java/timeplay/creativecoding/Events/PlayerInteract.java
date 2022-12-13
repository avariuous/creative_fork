/*
 Creative- TimePlay 2022

 Событие использования
 */

package timeplay.creativecoding.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static timeplay.creativecoding.Menu.Worlds.openMenu;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.NETHER_STAR && event.getPlayer().getWorld().getName().equals("world")) {
            Player player = event.getPlayer();
            try {
                player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",20,60,20);
                player.teleport(Bukkit.getWorld(player.getName()).getSpawnLocation());
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().clear();
                player.clearTitle();
                player.playSound(player.getLocation(), Sound.valueOf("BLOCK_BEACON_AMBIENT"),100,2);
            } catch(NullPointerException error1) {
                player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                player.clearTitle();
                player.sendMessage("");
                player.sendMessage("§cУ тебя ещё нет миров! Создай мир командой");
                player.sendMessage("§c§l /cc create");
                player.sendMessage("");
            }
        }
    }

    @EventHandler
    public void compass(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.COMPASS) && event.getPlayer().getWorld().getName().equals("world")) {
            openMenu(event.getPlayer());
        }
    }
}
