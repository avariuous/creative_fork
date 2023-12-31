/*
 Creative TimePlay 2023

 Событие когда игрок льёт жидкость из ведра
 */
package timeplay.creativecoding.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import timeplay.creativecoding.plots.DevPlot;

import static timeplay.creativecoding.plots.PlotManager.getDevPlot;

public class PlayerBucket implements Listener {

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        DevPlot devPlot = getDevPlot(player);
        if (devPlot != null) {
            event.setCancelled(true);
        }
    }

}
