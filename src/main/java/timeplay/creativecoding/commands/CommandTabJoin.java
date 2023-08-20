/*
 Creative TimePlay 2023

 Дополнительный класс для команды Join, просто
 чтобы был список при нажатии Таба.
 */

package timeplay.creativecoding.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import timeplay.creativecoding.world.Plot;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.world.PlotManager.plots;

public class CommandTabJoin implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> TabCompleter = new ArrayList<>();
            for (Plot plot : plots) {
                TabCompleter.add(plot.worldID);
            }
            return TabCompleter;
        }
        return null;
    }
}
