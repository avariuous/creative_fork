/*
 Creative TimePlay 2023

 В этом классе содержится команда /ad, которая
 позволяет игроку прорекламировать мир или же
 телепортироваться в другой мир.
 */

package timeplay.creativecoding.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import timeplay.creativecoding.Main;
import timeplay.creativecoding.world.Plot;

import java.util.HashMap;
import java.util.Map;

public class Ad implements CommandExecutor {

    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");
    Map<Player, Integer> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            Plot plot = Plot.getPlotByPlayer(player);
            if (args.length == 1) {
                try {
                    player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",20,60,20);
                    player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
                    player.setGameMode(GameMode.ADVENTURE);
                    player.getInventory().clear();
                    Main.clearTitle(player);
                    player.playSound(player.getLocation(), Sound.valueOf("BLOCK_BEACON_AMBIENT"),100,2);
                } catch(NullPointerException error1) {
                    player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                    Main.clearTitle(player);
                    player.sendMessage("§c Мира с таким ID не существует.");
                }
            } else {
                if (!(cooldowns.containsKey(player))) {
                    cooldowns.put(player,120);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (cooldowns.get(player) >= 1) {
                                int c = cooldowns.get(player)-1;
                                cooldowns.replace(player,c);
                            } else {
                                cooldowns.remove(player);
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin,20,20);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        TextComponent advertisement = new TextComponent("§7 \n§7 " + player.getName() + "§7 приглашает вас в игру:\n §f" + Plot.getWorldName(plot) + "\n §f \n §a [Нажмите, чтобы зайти]\n§f");
                        advertisement.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8Нажми, чтобы зайти в мир!")));
                        advertisement.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ad " + player.getWorld().getName()));
                        p.sendMessage(advertisement);
                    }
                } else {
                    player.sendMessage("§c Эту команду можно будет использовать через " + cooldowns.get(player) + "§c секунд.");
                }

            }
        }

        return true;
    }

}
