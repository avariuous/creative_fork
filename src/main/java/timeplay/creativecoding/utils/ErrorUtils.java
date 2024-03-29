/*
 Creative TimePlay 2023

 Утилита для вывода ошибок
 */

package timeplay.creativecoding.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import timeplay.creativecoding.plots.Plot;

public class ErrorUtils {

    public static void sendPlayerErrorMessage(Player player, String errorMessage) {
        Bukkit.getLogger().warning("Для игрока " + player.getName() + " произошла ошибка: " + errorMessage);
        player.sendMessage("§2 Ошибка §8» §fПроизошла ошибка: " + errorMessage);
        player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
    }

    public static void sendPlotErrorMessage(Plot plot, String errorMessage) {
        Bukkit.getLogger().warning("В мире " + plot.plotName + " произошла ошибка плота: " + errorMessage);
        for (Player player : plot.getPlayers()) {
            player.sendMessage("§2 Ошибка §8» §fПроизошла ошибка в мире: " + errorMessage);
            player.playSound(player.getLocation(), Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
        }
    }

    public static void sendWarningErrorMessage(String errorMessage) {
        Bukkit.getLogger().warning("Произошла ошибка: " + errorMessage);
    }

    public static void sendCriticalErrorMessage(String errorMessage) {
        Bukkit.getLogger().severe("Произошла критическая ошибка: " + errorMessage);
    }

}
