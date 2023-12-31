/*
 Creative TimePlay 2023

 Событие когда спавнится сущность в мире
 */
package timeplay.creativecoding.events;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;
import timeplay.creativecoding.utils.MessageUtils;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.utils.MessageUtils.sendMessageOnce;

public class EntitySpawn implements Listener {


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        World world = event.getLocation().getWorld();
        Plot plot = PlotManager.getPlotByWorld(world);
        if (plot != null) {
            int limit = plot.entitiesLimit;
            if (world.getEntityCount() > limit) {
                event.setCancelled(true);
                if (plot.getOnline() < 1) return;
                TextComponent warning = new TextComponent(getLocaleMessage("world.entity-limit").replace("%count%",String.valueOf(limit)));
                warning.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getLocaleMessage("world.entity-limit-hover"))));
                warning.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/world deletemobs"));
                sendMessageOnce(plot,warning,3);
            }
        }
        String worldName = world.getName();
        if (worldName.contains("dev")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPlace(EntityPlaceEvent event) {
        World world = event.getBlock().getWorld();
        Plot plot = PlotManager.getPlotByWorld(world);
        if (plot != null) {
            int limit = plot.entitiesLimit;
            if (world.getEntityCount() > limit) {
                event.setCancelled(true);
                if (plot.getOnline() < 1) return;
                TextComponent warning = new TextComponent(getLocaleMessage("world.entity-limit").replace("%count%",String.valueOf(limit)));
                warning.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getLocaleMessage("world.entity-limit-hover"))));
                warning.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/world deletemobs"));
                sendMessageOnce(plot,warning,3);
            }
        }
        String worldName = world.getName();
        if (worldName.contains("dev")) {
            event.setCancelled(true);
        }
    }


}
