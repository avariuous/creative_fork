/*
 Creative TimePlay 2022

 Событие выхода из сервера
 */

package timeplay.creativecoding.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import timeplay.creativecoding.commands.CreativeChat;
import timeplay.creativecoding.world.Plot;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.PlotManager.unloadPlot;

public class Quit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        PlayerChat.confirmation.remove(player);
        CreativeChat.creativeChatOff.remove(player);

        if (!(worldName.equals("world") || worldName.equals("world_nether") || worldName.equals("world_the_end"))) {
            Plot plot = Plot.getPlotByWorld(player.getWorld());
            if (Plot.getPlayers(plot).size() > 0) {
                for (Player p : Plot.getPlayers(plot)) {
                    p.sendMessage(getLocaleMessage("world.left",player));
                }
            } else {
                unloadPlot(plot);
            }
        }
    }
}
