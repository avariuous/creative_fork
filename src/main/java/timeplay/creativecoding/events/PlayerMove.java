package timeplay.creativecoding.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().distance(player.getWorld().getWorldBorder().getCenter()) > player.getWorld().getWorldBorder().getSize()) {
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }
}
