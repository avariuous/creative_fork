/*
 Creative TimePlay 2023

 Менеджер для плотов
 Бесполезен
 */

package timeplay.creativecoding.world;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlotManager {

    public static PlotManager plotManager;

    public PlotManager() {
        plotManager = this;
    }

    public static List<Plot> loadedPlots = new ArrayList<>();

    public static List<Plot> getOwnPlotList(Player player) {
        List<Plot> playerPlotList = new ArrayList<>();
        for (Plot plot : loadedPlots) {
            if (plot.owner == player) {
                playerPlotList.add(plot);
            }
        }
        return playerPlotList;
    }

}
