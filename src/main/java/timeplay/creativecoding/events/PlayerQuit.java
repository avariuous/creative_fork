/*
 Creative TimePlay 2022

 Событие выхода из сервера
 */

package timeplay.creativecoding.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import timeplay.creativecoding.coding.activators.PlayerJoinActivator;
import timeplay.creativecoding.coding.activators.PlayerQuitActivator;
import timeplay.creativecoding.commands.CreativeChat;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.FileUtils;

import java.util.List;

import static timeplay.creativecoding.plots.PlotManager.getDevPlot;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.plots.PlotManager.unloadPlot;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        PlayerChat.confirmation.remove(player);
        CreativeChat.creativeChatOff.remove(player);

        Plot plot = PlotManager.getPlotByPlayer(player);

        if (plot != null) {

            List<String> notTrustedDevelopers = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.DEVELOPERS_NOT_TRUSTED);
            List<String> notTrustedBuilders = FileUtils.getPlayersFromPlotConfig(plot, Plot.PlayersType.BUILDERS_NOT_TRUSTED);

            if (plot.getOnline() > 0) {
                for (Player p : plot.getPlayers()) {
                    if (plot.plotMode == Plot.Mode.BUILD && plot.getOwner().equalsIgnoreCase(player.getName())) {
                        if (notTrustedBuilders.contains(p.getName())) {
                            p.setGameMode(GameMode.ADVENTURE);
                            p.sendMessage(getLocaleMessage("world.build-mode.cant-build-when-offline"));
                        }
                    }
                    if (getDevPlot(p) != null && plot.getOwner().equalsIgnoreCase(player.getName())) {
                        if (notTrustedDevelopers.contains(p.getName())) {
                            p.setGameMode(GameMode.ADVENTURE);
                            p.sendMessage(getLocaleMessage("world.dev-mode.cant-dev-when-offline"));
                        }
                    }
                }
            } else {
                unloadPlot(plot);
            }
            plot.script.executeActivator(new PlayerQuitActivator(), player);
        }
    }


}
