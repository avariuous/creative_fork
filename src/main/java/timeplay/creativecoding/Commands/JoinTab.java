/*
 Creative- TimePlay 2022

 Дополнительный класс для команды Join, просто
 чтобы был список при нажатии Таба.
 */

package timeplay.creativecoding.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class JoinTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> TabCompleter = new ArrayList<>();
            for (World w: Bukkit.getWorlds()) {
                if (!(w.getName().equals("world_the_end") || w.getName().equals("world"))) {
                    TabCompleter.add(w.getName());
                }
            }
            return TabCompleter;
        }
        return null;
    }
}
