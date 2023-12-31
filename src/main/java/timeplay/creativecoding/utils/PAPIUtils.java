package timeplay.creativecoding.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public class PAPIUtils {

    public static String parsePlaceholdersAPI(OfflinePlayer offlinePlayer, String string) {
        return PlaceholderAPI.setPlaceholders(offlinePlayer,string);
    }

}
