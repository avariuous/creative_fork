/*
 Creative TimePlay 2022

 Событие чата
 */

package timeplay.creativecoding.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.world.Plot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.ErrorUtils.sendPlayerErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.getPlotConfig;
import static timeplay.creativecoding.utils.FileUtils.getPlotConfigFile;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlayerChat implements Listener {

    public static Map<Player, String> confirmation = new HashMap<>();
    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (event.getMessage().startsWith("!")) {
            event.setCancelled(true);
            player.sendMessage(getLocaleMessage("creative-chat.exclamation-chat-off"));
        }

        if (getCooldown(player, CooldownUtils.CooldownType.WORLD_CHAT) > 0) {
            player.sendMessage(getLocaleMessage("world.chat-cooldown").replace("%cooldown%",String.valueOf(getCooldown(player, CooldownUtils.CooldownType.WORLD_CHAT))));
        } else {
            setCooldown(player,plugin.getConfig().getInt("cooldowns.world-chat"), CooldownUtils.CooldownType.WORLD_CHAT);
            for (Player p : player.getWorld().getPlayers()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("messages.world-chat").replace("%player%",player.getName()).replace("%message%",event.getMessage())));
            }
            Bukkit.getLogger().info("[WORLD-CHAT: "+player.getWorld().getName()+"] "+player.getName()+": "+event.getMessage());
        }

        if (confirmation.isEmpty()) return;
        if (confirmation.containsKey(player)) {
            Plot plot;
            player.clearTitle();
            switch(confirmation.get(player)) {
                case "title":
                    plot = Plot.getPlotByPlayer(player);
                    String newName = "§f" + ChatColor.translateAlternateColorCodes('&',event.getMessage());
                    if (player.getName().equals(plot.owner)) {

                        FileConfiguration config = getPlotConfig(plot);
                        File configFile = getPlotConfigFile(plot);

                        if (!(ChatColor.stripColor(newName).length() > 30) && (ChatColor.stripColor(newName).length() > 4)) {
                            try {
                                plot.plotName = newName;
                                config.set("name",newName);
                                config.save(configFile);
                                player.sendMessage(getLocaleMessage("settings.world-name.changed").replace("%name%",newName));
                            } catch (Exception error) {
                                sendPlayerErrorMessage(player,"Ошибка при сохранении файла конфигурации мира "+ plot.world.getName() + ": " + error.getMessage());
                            }
                        } else {
                            player.sendMessage(getLocaleMessage("settings.world-name.error"));
                        }
                    }
                    break;
                case "description":
                    plot = Plot.getPlotByPlayer(player);
                    String newDescription = "§f" + ChatColor.translateAlternateColorCodes('&',event.getMessage());
                    if (player.getName().equals(plot.owner)) {

                        FileConfiguration config = getPlotConfig(plot);
                        File configFile = getPlotConfigFile(plot);

                        if (!(ChatColor.stripColor(newDescription).length() > 30) && (ChatColor.stripColor(newDescription).length() > 4))  {
                            try {
                                plot.plotDescription = newDescription;
                                config.set("description",newDescription);
                                config.save(configFile);
                                player.sendMessage(getLocaleMessage("settings.world-description.changed").replace("%description%",newDescription));
                            } catch (Exception error) {
                                sendPlayerErrorMessage(player,"Ошибка при сохранении файла конфигурации мира "+ plot.world.getName() + ": " + error.getMessage());
                            }
                        } else {
                            player.sendMessage(getLocaleMessage("settings.world-description.error"));
                        }
                    }
                    break;
            }
            confirmation.remove(player);
        }
    }
}
