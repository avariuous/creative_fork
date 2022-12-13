/*
 Creative- TimePlay 2022

 Событие смерти
 */

package timeplay.creativecoding.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("§7" + event.getEntity().getPlayer().getName() + "§f умер");
        event.getEntity().sendTitle("§cПотрачено","§f" + event.getDeathMessage());
    }
}
