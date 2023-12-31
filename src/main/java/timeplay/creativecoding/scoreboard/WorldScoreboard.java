package timeplay.creativecoding.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.utils.ErrorUtils.sendWarningErrorMessage;

// скорборд не работает, он не отсылает ошибок и при этом ничего не показывает игроку

public class WorldScoreboard {

    public static Map<Player, Integer> scoreboardTasks = new HashMap<>();
    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    public static void show(Player player) {

        Integer taskID = new BukkitRunnable() {

            public void run() {
                if (player.isOnline()) {

                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
                    Objective objective = board.registerNewObjective("", "dummy");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    Plot plot = PlotManager.getPlotByPlayer(player);
                    try {
                        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("scoreboards.world.name")));
                        int configScoreboardSize = plugin.getConfig().getStringList("scoreboards.world.lines").size();
                        int inGameScoreboardIndex = configScoreboardSize;
                        for (int configScoreboardIndex = 0; configScoreboardIndex < configScoreboardSize; configScoreboardIndex++) {
                            inGameScoreboardIndex--;
                            Score score = objective.getScore(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getStringList("scoreboards.world.lines").get(configScoreboardIndex).replace("%player%",player.getName()).replace("%online%",String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%worldOnline%",String.valueOf(plot.getOnline())).replace("%owner%",plot.owner)));
                            score.setScore(inGameScoreboardIndex);
                        }
                    } catch (NullPointerException error) {
                        sendWarningErrorMessage("При попытке показать скорборд лобби произошла ошибка: " + error.getMessage());
                        cancel();
                    } catch (IllegalArgumentException error) {
                        sendWarningErrorMessage("При попытке показать скорборд скорее всего получилась строка длинее 40 символов, укоротите её. " + error.getMessage());
                        cancel();
                    }
                    player.setScoreboard(board);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20L * plugin.getConfig().getInt("scoreboards.lobby.update-every-seconds")).getTaskId();
        scoreboardTasks.put(player, taskID);

    }

    public static void hide(Player player) {
        try {
            plugin.getServer().getScheduler().cancelTask(scoreboardTasks.get(player));
        } catch (NullPointerException error) {
            sendWarningErrorMessage("Ошибка при попытке скрыть скорборд, плагин не получил сервер? "+error.getMessage());
        }
    }

}
