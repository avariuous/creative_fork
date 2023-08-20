/*
 Creative TimePlay 2023

 Событие смерти
 */

package timeplay.creativecoding.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import timeplay.creativecoding.world.Plot;

import java.util.HashMap;
import java.util.Map;

public class Death implements Listener {

    public static Map<Player, Location> deathLocations = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity().getPlayer();
        String deathMessage = event.getDeathMessage();
        event.setDeathMessage(null);

        if (player.getLocation().getWorld().getName().startsWith("plot")) {
            for (Player p : Plot.getPlayers(Plot.getPlotByPlayer(player))) {
                p.sendMessage(" " + deathMessage);
            }
        }

        player.sendTitle("§cПотрачено","§7" + deathMessage);
        deathLocations.put(player,Plot.getPlotByPlayer(player).world.getSpawnLocation());

    }
}
