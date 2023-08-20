/*
 Creative TimePlay 2023

 Утилита для задержек использования команд
 */

package timeplay.creativecoding.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class CooldownUtils {

    static HashMap<Player, Long> genericCommandCooldown = new HashMap<>();
    static HashMap<Player, Long> advertisementCommandCooldown = new HashMap<>();
    static HashMap<Player, Long> creativeChatCooldown = new HashMap<>();
    static HashMap<Player, Long> worldChatCooldown = new HashMap<>();

    public enum CooldownType {
        GENERIC_COMMAND, ADVERTISEMENT_COMMAND, CREATIVE_CHAT, WORLD_CHAT
    }

    public static long getCooldownFromMap(Player player, CooldownType type) {

        HashMap<Player, Long> cooldownMap = getCooldownMap(type);

        if (!(cooldownMap.containsKey(player))) return 0L;
        return cooldownMap.get(player);
    }

    public static void setCooldown(Player player, int cooldown, CooldownType type) {

        long cooldownInMillis = cooldown * 1000L;
        long currentTime = System.currentTimeMillis();
        long cooldownEndTime = currentTime + cooldownInMillis;

        HashMap<Player, Long> cooldownMap = getCooldownMap(type);
        cooldownMap.put(player, cooldownEndTime);

    }

    public static int getCooldown(Player player, CooldownType type) {

        if (player.hasPermission("creative.cooldownbypass")) return 0;
        long cooldownEndTime = getCooldownFromMap(player,type);
        if (cooldownEndTime == 0L) return 0;

        long currentTime = System.currentTimeMillis();

        if (cooldownEndTime < currentTime) {
            return 0;
        } else {
            return Math.round(cooldownEndTime - currentTime)/1000;
        }
    }

    private static HashMap<Player, Long> getCooldownMap(CooldownType type) {
        switch (type) {
            case GENERIC_COMMAND:
                return genericCommandCooldown;
            case ADVERTISEMENT_COMMAND:
                return advertisementCommandCooldown;
            case CREATIVE_CHAT:
                return creativeChatCooldown;
            case WORLD_CHAT:
                return worldChatCooldown;
            default:
                throw new IllegalArgumentException("Невозможно получить заддержку с типом: " + type);
        }
    }


}
