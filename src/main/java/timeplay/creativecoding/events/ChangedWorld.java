/*
 Creative TimePlay 2023

 Событие когда игрок перемещается между мирами
 */

package timeplay.creativecoding.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import timeplay.creativecoding.commands.CreativeChat;
import timeplay.creativecoding.scoreboard.LobbyScoreboard;
import timeplay.creativecoding.scoreboard.WorldScoreboard;
import timeplay.creativecoding.world.Plot;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.world.PlotManager.unloadPlot;

public class ChangedWorld implements Listener {

    @EventHandler
    public void onPlayerWorldChanged(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();
        World oldWorld = event.getFrom();
        World newWorld = player.getWorld();

        PlayerChat.confirmation.remove(event.getPlayer());
        CreativeChat.creativeChatOff.remove(event.getPlayer());
        event.getPlayer().clearTitle();

        //if (LobbyScoreboard.scoreboardTasks.containsKey(player)) LobbyScoreboard.hide(player);
        //if (WorldScoreboard.scoreboardTasks.containsKey(player)) WorldScoreboard.hide(player);

        if (newWorld.getName().startsWith("plot")) {
            Plot plot = Plot.getPlotByWorld(newWorld);
            //WorldScoreboard.show(player);
            for (Player p : Plot.getPlayers(plot)) {
                p.sendMessage(getLocaleMessage("world.joined",player));
            }
        }
        if (oldWorld.getName().startsWith("plot")) {
            Plot plot = Plot.getPlotByWorld(oldWorld);
            //LobbyScoreboard.show(player);
            if (Plot.getPlayers(plot).size() > 0) {
                for (Player p : Plot.getPlayers(plot)) {
                    p.sendMessage(getLocaleMessage("world.left",player));
                }
            } else {
                unloadPlot(plot);
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld() != player.getWorld()) {
                p.hidePlayer(player);
                player.hidePlayer(p);
            } else {
                p.showPlayer(player);
                player.showPlayer(p);
            }
        }

    }

}
