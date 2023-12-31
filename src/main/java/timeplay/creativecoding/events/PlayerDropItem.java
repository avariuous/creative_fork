/*
 Creative TimePlay 2023

 Событие кидания предмета
 */
package timeplay.creativecoding.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().startsWith("plot")) {
            if (event.getItemDrop().getItemStack().getItemMeta().displayName().equals(getLocaleMessage("items.lobby.games.name")) || event.getItemDrop().getItemStack().getItemMeta().displayName().equals(getLocaleMessage("items.lobby.own.name"))) {
                event.setCancelled(true);
            }
        }
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(getLocaleItemName("items.developer.world-settings.name"))) {
            event.setCancelled(true);
        }
    }

}
