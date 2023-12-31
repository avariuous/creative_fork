/*
 Creative TimePlay 2023

 Событие когда срабатывают редстоун блоки
 */
package timeplay.creativecoding.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.utils.MessageUtils.sendMessageOnce;

public class BlockRedstone implements Listener {

    @NotNull
    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Location location = event.getBlock().getLocation();
        Plot plot = PlotManager.getPlotByWorld(location.getWorld());
        if (plot != null) {
            plot.lastRedstoneOperationsAmount++;
            if (plot.lastRedstoneOperationsAmount > plot.redstoneOperationsLimit) {
                    sendMessageOnce(plot,getLocaleMessage("world.redstone-limit").replace("%count%",String.valueOf(plot.redstoneOperationsLimit)),5);

                    if (location.getBlock().getType() == Material.OBSERVER) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                location.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(plugin,1L);
                    } else {
                        location.getBlock().setType(Material.CAVE_AIR);
                    }
                    plot.lastRedstoneOperationsAmount = 0;
            }
            if (plot.lastRedstoneOperationsAmount > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plot.lastRedstoneOperationsAmount = plot.lastRedstoneOperationsAmount-1;
                    }
                }.runTaskLater(plugin,5L);
            }
        }
    }

}
