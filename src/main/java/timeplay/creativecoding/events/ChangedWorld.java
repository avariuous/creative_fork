/*
 Creative TimePlay 2023

 Событие когда игрок перемещается между мирами
 */

package timeplay.creativecoding.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangedWorld implements Listener {

    @EventHandler
    public void onPlayerWorldChanged(PlayerChangedWorldEvent event) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            // не работает!!! :(
            if (player.getWorld() != event.getPlayer().getWorld()) {
                player.hidePlayer(event.getPlayer());
                event.getPlayer().hidePlayer(player);
            } else {
                player.showPlayer(event.getPlayer());
                event.getPlayer().showPlayer(player);
            }
        }

    }

}
