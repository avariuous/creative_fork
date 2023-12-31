/*
 Creative TimePlay 2022

 Событие входа на сервер
 */

package timeplay.creativecoding.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import timeplay.creativecoding.Main;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.teleportToLobby(event.getPlayer());
    }
}
