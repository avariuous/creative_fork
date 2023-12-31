/*
 Creative TimePlay 2022

 Событие возрождения
 */

package timeplay.creativecoding.events;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import timeplay.creativecoding.coding.activators.PlayerQuitActivator;
import timeplay.creativecoding.coding.activators.PlayerRespawnActivator;
import timeplay.creativecoding.plots.Plot;

import static timeplay.creativecoding.events.PlayerDeath.deathLocations;
import static timeplay.creativecoding.plots.PlotManager.getPlotByPlayer;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!deathLocations.containsKey(event.getPlayer())) return;
        Location deathLocation = deathLocations.get(event.getPlayer());
        event.setRespawnLocation(deathLocation);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf("ENTITY_PLAYER_BREATH"),100,2);
        deathLocations.remove(event.getPlayer());
        Plot plot = getPlotByPlayer(event.getPlayer());
        if (plot != null) {
            plot.script.executeActivator(new PlayerRespawnActivator(), event.getPlayer());
        }
    }
}
