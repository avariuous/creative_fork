/*
 Creative TimePlay 2023

 Событие когда игрок перемещается между мирами
 */

package timeplay.creativecoding.events;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import timeplay.creativecoding.coding.activators.PlayerJoinActivator;
import timeplay.creativecoding.coding.activators.PlayerQuitActivator;
import timeplay.creativecoding.commands.CreativeChat;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.FileUtils;

import java.util.List;

import static timeplay.creativecoding.plots.PlotManager.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class ChangedWorld implements Listener {

    @EventHandler
    public void onPlayerWorldChanged(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();
        World oldWorld = event.getFrom();
        World newWorld = player.getWorld();

        PlayerChat.confirmation.remove(event.getPlayer());
        CreativeChat.creativeChatOff.remove(event.getPlayer());
        event.getPlayer().clearTitle();

        Plot oldPlot = PlotManager.getPlotByWorld(oldWorld);
        Plot newPlot = PlotManager.getPlotByWorld(newWorld);

        if (newPlot != null) {
            if (!oldWorld.getName().endsWith("dev")) {
                if (newPlot.getOnline() < 1) return;
                for (Player p : newPlot.getPlayers()) {
                    if (newPlot.joinMesssagesFlag == 1) {
                        p.sendMessage(getLocaleMessage("world.joined", player));
                    }
                    p.showPlayer(player);
                    player.showPlayer(p);
                }
            } else {
                if (!newWorld.getName().equals(oldWorld.getName().replace("dev", ""))) {
                    Plot oldPlot_ = PlotManager.getPlotByWorldName(oldWorld.getName().replace("dev", ""));
                    if (oldPlot_ != null) {
                        if (oldPlot_.getOnline() > 0) {
                            for (Player p : oldPlot_.getPlayers()) {
                                if (oldPlot_.joinMesssagesFlag == 1) {
                                    p.sendMessage(getLocaleMessage("world.left", player));
                                }
                                p.hidePlayer(player);
                                player.hidePlayer(p);
                            }
                        }
                    }
                }
            }
        }

        if (oldPlot != null) {
            List<String> notTrustedDevelopers = FileUtils.getPlayersFromPlotConfig(oldPlot, Plot.PlayersType.DEVELOPERS_NOT_TRUSTED);
            List<String> notTrustedBuilders = FileUtils.getPlayersFromPlotConfig(oldPlot, Plot.PlayersType.BUILDERS_NOT_TRUSTED);
            if (!oldWorld.getName().endsWith("dev")) oldPlot.script.executeActivator(new PlayerQuitActivator(), player);
            if (!newWorld.getName().endsWith("dev")) {
                if (oldPlot.getOnline() > 0) {
                    for (Player p : oldPlot.getPlayers()) {
                        if (oldPlot.joinMesssagesFlag == 1) {
                            p.sendMessage(getLocaleMessage("world.left",player));
                            if (oldPlot.plotMode == Plot.Mode.BUILD && oldPlot.owner.equalsIgnoreCase(player.getName())) {
                                if (notTrustedBuilders.contains(p.getName())) {
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.sendMessage(getLocaleMessage("world.build-mode.cant-build-when-offline"));
                                }
                            }
                            if (getDevPlot(p) != null && oldPlot.owner.equalsIgnoreCase(player.getName())) {
                                if (notTrustedDevelopers.contains(p.getName())) {
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.sendMessage(getLocaleMessage("world.dev-mode.cant-dev-when-offline"));
                                }
                            }
                        }
                        p.hidePlayer(player);
                        player.hidePlayer(p);
                    }
                } else {
                    unloadPlot(oldPlot);
                }
            }
        }
    }
}
