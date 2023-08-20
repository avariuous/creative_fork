/*
 Creative TimePlay 2022

 Событие возрождения
 */

package timeplay.creativecoding.events;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static timeplay.creativecoding.events.Death.deathLocations;

public class Respawn implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.getPlayer().teleport(deathLocations.get(event.getPlayer()));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf("ENTITY_PLAYER_BREATH"),100,2);
        deathLocations.remove(event.getPlayer());
    }
}
