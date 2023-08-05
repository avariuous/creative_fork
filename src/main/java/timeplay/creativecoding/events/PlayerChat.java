/*
 Creative TimePlay 2022

 Событие чата
 */

package timeplay.creativecoding.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.utils.ErrorUtils.sendPlayerErrorMessage;

public class PlayerChat implements Listener {

    public static Map<Player, String> confirmation = new HashMap<>();
    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (event.getMessage().startsWith("!")) {
            event.setCancelled(true);
            player.sendMessage("§f");
            player.sendMessage("§8§l | §6! ГЛОБАЛЬНЫЙ ЧАТ ОТКЛЮЧЕН");
            player.sendMessage("§8§l | §7Зачем? Потомучто у нас есть свой");
            player.sendMessage("§8§l | §7креатив-чат, попробуй его!");
            player.sendMessage("§8§l | §aЧтобы включить его напиши /cc on");
            player.sendMessage("§8§l | §fЧтобы писать в нём напиши /cc Привет!");
            player.sendMessage("§f");
        }

        try {

            if (confirmation.get(player).equals("title")) {
                event.setCancelled(true);
                if (event.getMessage().length() > 0 && event.getMessage().length() < 30) {
                    String newtitle = "§f" + event.getMessage().replace('&', '§');
                    World world = player.getWorld();
                    String worldName = world.getName();
                    File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldName + ".yml");
                    final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
                    player.sendMessage(Main.prefix() + "§fУстановлено §6новое §fназвание: " + newtitle);
                    worldfile.set("title", newtitle);
                    confirmation.remove(player);
                    try {
                        worldfile.save(file);
                    } catch (IOException error) {
                    }
                } else {
                    player.sendMessage("§cНеправильное название мира! Возможно оно больше 30 символов, попробуйте ещё раз...");
                }
            }
            if (confirmation.get(player).equals("description")) {
                event.setCancelled(true);
                if (event.getMessage().length() > 0 && event.getMessage().length() < 30) {
                    String newDesc = "§f" + event.getMessage().replace('&', '§');
                    World world = player.getWorld();
                    String worldName = world.getName();
                    File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldName + ".yml");
                    final FileConfiguration worldfile = YamlConfiguration.loadConfiguration(file);
                    player.sendMessage(Main.prefix() + "§fУстановлено §6новое §fописание: " + newDesc);
                    worldfile.set("description", newDesc);
                    confirmation.remove(player);
                    try {
                        worldfile.save(file);
                    } catch (IOException error) {
                    }
                } else {
                    player.sendMessage("§cНеправильное описание мира! Возможно оно больше 30 символов, попробуйте ещё раз...");
                }
            }
        } catch (NullPointerException error) {
            sendPlayerErrorMessage(player,error.getMessage());
        }
    }
}
