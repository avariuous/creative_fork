package timeplay.creativecoding.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryClick implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Миры от игроков") && event.getCurrentItem() != null) {
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            String displayname = meta.getDisplayName();
            List lore = meta.getLore();
            HumanEntity player = event.getWhoClicked();
            try {
                if (item.getType() == Material.DIAMOND) {
                    World world = Bukkit.getWorld(lore.get(0).toString().replace("§8ID: ", ""));
                    player.teleport(world.getSpawnLocation());
                    player.setGameMode(GameMode.ADVENTURE);
                    player.getInventory().clear();
                }
            } catch (NullPointerException error) {}
            event.setCancelled(true);
        }
    }
}
