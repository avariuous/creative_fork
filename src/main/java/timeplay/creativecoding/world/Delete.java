/*
 Creative TimePlay 2023

 Удалить мир
 */

package timeplay.creativecoding.world;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;

import java.util.stream.Collectors;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.deleteWorld;
import static timeplay.creativecoding.utils.FileUtils.deleteWorldConfig;
import static timeplay.creativecoding.world.PlotManager.loadedPlots;

public class Delete {

    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");
    public static void Delete(Plot plot, Player player) {

        String worldName = plot.world.getName();
        try {
            // Телепортирует всех игроков в мире на спавн
            loadedPlots.remove(plot);
            for (Player p :Bukkit.getOnlinePlayers().stream().filter(player1 -> player1.getWorld().equals(worldName)).collect(Collectors.toList())) {
                Main.LobbyTeleport(p);
            }
            if (player != null) {
                player.sendTitle("§cУдаление...","§7Стираем данные мира...",20,60,20);
            }
            // Удаляет папку мира
            deleteWorld(Bukkit.getWorld(worldName).getWorldFolder());
            deleteWorldConfig(worldName);
            // После 3 секунд удаления мир отгружается полностью
            Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
                Bukkit.unloadWorld(worldName,false);
            }, 60, 20);
        } catch (NullPointerException error) {
            sendCriticalErrorMessage(error.getMessage());
        }
    }


}
