/*
 Creative TimePlay 2023

 Событие когда сущность получает урон
 */
package timeplay.creativecoding.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

public class EntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (event.getEntity() instanceof Player) {

            Player victim = ((Player) event.getEntity()).getPlayer();

            if (victim != null) {

                Plot plot = PlotManager.getPlotByPlayer(victim);
                if (plot != null) {
                    if (plot.plotMode == Plot.Mode.BUILD) event.setCancelled(true);

                    if (victim.getLocation().distance(victim.getWorld().getSpawnLocation()) < 5) event.setCancelled(true);

                    int playerDamageFlag = plot.playerDamageFlag;
                    if (playerDamageFlag == 2) event.setCancelled(true);
                    if (playerDamageFlag == 3 && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) event.setCancelled(true);
                    if (playerDamageFlag == 4 && event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
                    if (playerDamageFlag == 5 && (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.FALL)) event.setCancelled(true);
                    if (event.getCause() == EntityDamageEvent.DamageCause.VOID) ((Player) event.getEntity()).setHealth(0);

                } else if (victim.getWorld().getName().equalsIgnoreCase("world")) {
                    event.setCancelled(true);
                }
            }

        }
    }
}
