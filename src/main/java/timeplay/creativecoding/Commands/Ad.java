package timeplay.creativecoding.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import timeplay.creativecoding.World.GetData;

public class Ad implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                try {
                    player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",20,60,20);
                    player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
                    player.setGameMode(GameMode.ADVENTURE);
                    player.getInventory().clear();
                    player.clearTitle();
                    player.playSound(player.getLocation(), Sound.valueOf("BLOCK_BEACON_AMBIENT"),100,2);
                } catch(NullPointerException error1) {
                    player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                    player.clearTitle();
                    player.sendMessage("§c Мира с таким ID не существует.");
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage("");
                    p.sendMessage("§7" + player.getName() + "§7 приглашает вас в игру:");
                    p.sendMessage("§f" + GetData.title(player.getWorld()));
                    p.sendMessage("");
                    p.sendMessage("§a [Нажми, чтобы играть]");
                    p.sendMessage("");
                }
            }
        }

        return true;
    }

}
