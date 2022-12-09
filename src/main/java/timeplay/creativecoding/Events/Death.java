package timeplay.creativecoding.Events;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        event.getEntity().getPlayer().teleport(event.getEntity().getPlayer().getWorld().getSpawnLocation());
        event.setDeathMessage("§7" + event.getEntity().getPlayer().getName() + "§f умер");
        event.getEntity().getPlayer().sendTitle("§cПотрачено","§f" + event.getDeathMessage());
        event.getEntity().getPlayer().playSound(event.getEntity().getPlayer().getLocation(), Sound.valueOf("ENTITY_PLAYER_BREATH"),100,2);
    }
}
