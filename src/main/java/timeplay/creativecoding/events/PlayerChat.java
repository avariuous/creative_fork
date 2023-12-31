/*
 Creative TimePlay 2022

 Событие чата
 */

package timeplay.creativecoding.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.menu.AllWorldsMenu;
import timeplay.creativecoding.utils.CooldownUtils;
import timeplay.creativecoding.plots.Plot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static timeplay.creativecoding.plots.PlotManager.*;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.utils.MessageUtils.parsePAPI;

public class PlayerChat implements Listener {

    public static Map<Player, String> confirmation = new HashMap<>();
    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    @EventHandler
    public void onChat(PlayerChatEvent event) {

        Player player = event.getPlayer();

        if (event.getMessage().startsWith("!")) {
            event.setCancelled(true);
            player.sendMessage(getLocaleMessage("creative-chat.exclamation-chat-off"));
            return;
        }

        if (getCooldown(player, CooldownUtils.CooldownType.WORLD_CHAT) > 0) {
            player.sendMessage(getLocaleMessage("world.chat-cooldown").replace("%cooldown%",String.valueOf(getCooldown(player, CooldownUtils.CooldownType.WORLD_CHAT))));
        } else {
            setCooldown(player,plugin.getConfig().getInt("cooldowns.world-chat"), CooldownUtils.CooldownType.WORLD_CHAT);
            if (event.isCancelled()) return;
            event.setCancelled(true);
            for (Player p : player.getWorld().getPlayers()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',parsePAPI(player,plugin.getConfig().getString("messages.world-chat")).replace("%player%",player.getName()).replace("%message%",event.getMessage())));
            }
            Bukkit.getLogger().info("[WORLD-CHAT: "+player.getWorld().getName()+"] "+player.getName()+": "+event.getMessage());
        }

        event.setCancelled(true);
        if (player.getWorld().getName().contains("dev")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType() == Material.BOOK) {
                ItemMeta meta = itemInHand.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',event.getMessage()));
                itemInHand.setItemMeta(meta);
                player.setItemInHand(itemInHand);
                player.sendTitle("",ChatColor.translateAlternateColorCodes('&',event.getMessage()));
            }
            if (itemInHand.getType() == Material.SLIME_BALL) {
                float floatNumber = Float.parseFloat(ChatColor.stripColor(event.getMessage()));
                ItemMeta meta = itemInHand.getItemMeta();
                meta.setDisplayName("§c" + floatNumber);
                itemInHand.setItemMeta(meta);
                player.setItemInHand(itemInHand);
                player.sendTitle("",ChatColor.translateAlternateColorCodes('&',event.getMessage()));
            }
        }

        if (confirmation.isEmpty()) return;
        if (confirmation.containsKey(player)) {
            Plot plot;
            player.clearTitle();
            switch(confirmation.get(player)) {
                case "title":
                    plot = getPlotByPlayer(player);
                    String newName = "§f" + ChatColor.translateAlternateColorCodes('&',event.getMessage());
                    if (player.getName().equals(plot.owner)) {
                        if (!(ChatColor.stripColor(newName).length() > 30) && (ChatColor.stripColor(newName).length() > 4)) {
                            plot.plotName = newName;
                            setPlotConfigParameter(plot,"name",newName);
                            player.sendMessage(getLocaleMessage("settings.world-name.changed").replace("%name%",newName));
                        } else {
                            player.sendMessage(getLocaleMessage("settings.world-name.error"));
                        }
                    }
                    break;
                case "id":
                    plot = getPlotByPlayer(player);
                    String newID = event.getMessage();
                    if (player.getName().equals(plot.owner)) {
                        String pattern = "^[a-zA-Zа-яА-Я0-9]+$";
                        if (newID.length() > 2 && newID.length() < 16 && newID.matches(pattern) && !Character.isDigit(newID.charAt(0))) {
                            boolean existsID = false;
                            for (Plot searchablePlot : plots) {
                                if (searchablePlot.plotCustomID.equalsIgnoreCase(newID)) {
                                    existsID = true;
                                    break;
                                }
                            }
                            if (!existsID) {
                                plot.plotCustomID = newID;
                                setPlotConfigParameter(plot,"customID",newID);
                                player.sendMessage(getLocaleMessage("settings.world-id.changed").replace("%id%",newID));
                            } else {
                                player.sendMessage(getLocaleMessage("settings.world-id.taken"));
                            }
                        } else {
                            player.sendMessage(getLocaleMessage("settings.world-id.error"));
                        }
                    }
                    break;
                case "description":
                    plot = getPlotByPlayer(player);
                    String newDescription = ChatColor.translateAlternateColorCodes('&',event.getMessage());
                    if (player.getName().equals(plot.owner)) {
                        if (!(ChatColor.stripColor(newDescription).length() > 256) && (ChatColor.stripColor(newDescription).length() > 4))  {
                            newDescription = String.join("\\n",splitDescription(newDescription,39));
                            plot.plotDescription = newDescription;
                            setPlotConfigParameter(plot,"description",newDescription);
                            player.sendMessage(getLocaleMessage("settings.world-description.changed").replace("%description%",newDescription));
                        } else {
                            player.sendMessage(getLocaleMessage("settings.world-description.error"));
                        }
                    }
                    break;
                case "searchPlotByPlotName":
                    List<Plot> foundPlotsByName = getPlotsByPlotName(event.getMessage());
                    if (foundPlotsByName.size() >= 1) {
                        AllWorldsMenu.openInventory(player,1,foundPlotsByName);
                    } else {
                        player.sendMessage(getLocaleMessage("menus.all-worlds.items.search.not-found"));
                    }
                    break;
                case "searchPlotByID":
                    List<Plot> foundPlotsByID = getPlotsByID(event.getMessage());
                    if (foundPlotsByID.size() >= 1) {
                        AllWorldsMenu.openInventory(player,1,foundPlotsByID);
                    } else {
                        player.sendMessage(getLocaleMessage("menus.all-worlds.items.search.not-found"));
                    }
                    break;
            }
            confirmation.remove(player);
        }
    }

    private static List<String> splitDescription(String input, int maxLength) {

        List<String> setDescriptionWords = new ArrayList<>();

        if (input.contains("\\n")) {
            String[] newDescriptionWords = input.split("\\n");
            for (String word : newDescriptionWords) {
                setDescriptionWords.add(word);
            }
        } else {
            String[] newDescriptionWords = input.split("\\s+");

            int currentSize = 0;
            String newLine = "";

            if (newDescriptionWords.length > 1) {
                for (String word : newDescriptionWords) {

                    if (currentSize + word.length() > maxLength) {

                        if (word.length() > maxLength) {

                            String newStr = newLine.replaceAll("(.{" + maxLength + "}[^\\n])", "$1\\\\n");
                            String[] newStrings = newStr.split("\\\\n");

                            for (String string : newStrings) {
                                setDescriptionWords.add(string);
                            }

                        } else {
                            setDescriptionWords.add(newLine.trim());
                        }

                        newLine = "";
                        currentSize = 0;

                    }
                    currentSize += word.length();
                    newLine += word + " ";
                }
            } else {

                input = input.replaceAll("(.{"+maxLength+"})", "$1\\\\n");

                String[] newStrings = input.split("\\\\n");
                for (String string : newStrings) {
                    setDescriptionWords.add(string);
                }
            }

            setDescriptionWords.add(newLine.trim());
        }
        return setDescriptionWords;
    }
}
