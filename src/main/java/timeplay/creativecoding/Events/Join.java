package timeplay.creativecoding.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import timeplay.creativecoding.Main;

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.LobbyTeleport(event.getPlayer());
    }
}
