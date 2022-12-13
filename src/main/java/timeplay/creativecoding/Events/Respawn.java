/*
 Creative- TimePlay 2022

 Событие возраждения
 */

package timeplay.creativecoding.Events;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Respawn implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        World world = event.getPlayer().getWorld();
        event.getPlayer().teleport(world.getSpawnLocation());
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf("ENTITY_PLAYER_BREATH"),100,2);
    }
}
