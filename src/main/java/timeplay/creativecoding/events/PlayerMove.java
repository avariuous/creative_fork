/*
 Creative TimePlay 2023

 Событие передвижения игрока
 Используется для телепорта игрока, если он за границей мира
 */
package timeplay.creativecoding.events;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Проверка на пересечение границы мира
        WorldBorder border = event.getPlayer().getWorld().getWorldBorder();
        Location borderCenter = border.getCenter();

        double radius = border.getSize()/2;

        double borderCenterX1 = borderCenter.getX()+radius;
        double borderCenterX2 = borderCenter.getX()-radius;
        double borderCenterZ1 = borderCenter.getZ()+radius;
        double borderCenterZ2 = borderCenter.getZ()-radius;

        Location location = player.getLocation();
        double playerX = location.getX();
        double playerZ = location.getZ();

        if (!(borderCenterX1 > playerX && playerX > borderCenterX2)) {
            player.teleport(player.getWorld().getSpawnLocation());
        } else if (!(borderCenterZ1 > playerZ && playerZ > borderCenterZ2)) {
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }
}
