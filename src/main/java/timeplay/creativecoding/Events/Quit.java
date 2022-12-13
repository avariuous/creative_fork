/*
 Creative- TimePlay 2022

 Событие выхода из сервера
 */

package timeplay.creativecoding.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import timeplay.creativecoding.Commands.CreativeChat;

public class Quit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerChat.confirmation.remove(player);
        CreativeChat.creativeChatOff.remove(player);
        System.out.println(PlayerChat.confirmation);
    }
}
