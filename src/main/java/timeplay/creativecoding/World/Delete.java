/*
 Creative- TimePlay 2022

 Удалить мир
 */

package timeplay.creativecoding.World;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.Main;

import java.io.File;
import java.util.stream.Collectors;

public class Delete {
    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CreativeCoding");
    public static void Delete(World world, Player player) {
        String worldname = world.getName();
        try {
            // Телепортирует всех игроков в мире на спавн
            for (Player p :Bukkit.getOnlinePlayers().stream().filter(player1 -> player1.getWorld().equals(world)).collect(Collectors.toList())) {
                Main.LobbyTeleport(p);
            }
            if (player != null) {
                player.sendTitle("§cУдаление...","§7Стираем данные мира...",20,60,20);
            }
            // Удаляет папку мира
            deleteWorld(Bukkit.getWorld(worldname).getWorldFolder());
            deleteWorldConfig(worldname);
            // После 3 секунд удаления мир отгружается полностью
            Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
                Bukkit.unloadWorld(worldname,false);
            }, 60, 20);
        } catch (NullPointerException error1) {
            if (player != null) {
                player.playSound(((Player) player).getLocation(), Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
                player.sendMessage("");
                player.sendMessage("§cПроизошла критическая ошибка :(");
                player.sendMessage("§cКод ошибки: §lNullPointerException");
                player.sendMessage("");
            }
        }
    }

    // Удаление папки мира
    public static boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    // Удаление файла конфигурации мира plugins\CreativeCoding\worlds\...yml
    public static void deleteWorldConfig(String worldname) {
        File file = new File((plugin.getDataFolder() + "\\worlds\\"), worldname + ".yml");
        if(file.exists()) {
            file.delete();
        }
    }
}
