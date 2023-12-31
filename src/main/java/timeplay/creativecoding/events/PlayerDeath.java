/*
 Creative TimePlay 2023

 Событие смерти
 */

package timeplay.creativecoding.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import timeplay.creativecoding.coding.activators.PlayerRespawnActivator;
import timeplay.creativecoding.plots.Plot;
import timeplay.creativecoding.plots.PlotManager;

import java.util.HashMap;
import java.util.Map;

import static timeplay.creativecoding.Main.teleportToLobby;
import static timeplay.creativecoding.plots.PlotManager.getPlotByPlayer;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class PlayerDeath implements Listener {

    public static Map<Player, Location> deathLocations = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity().getPlayer();
        String deathMessage = event.getDeathMessage();

        if (player == null) return;
        event.setDeathMessage(null);
        Plot plot = PlotManager.getPlotByPlayer(player);

        if (plot != null) {
            deathLocations.put(player, plot.world.getSpawnLocation());
            for (Player p : plot.getPlayers()) {
                p.sendMessage("§7 " + player.getName() + "§f " + translateDeathMessage(player));
            }
            plot.script.executeActivator(new PlayerRespawnActivator(), player);
        } else {
            event.setKeepInventory(true);
            teleportToLobby(player);
        }
        player.sendTitle("§cПотрачено","§7 " + player.getName() + "§f " + translateDeathMessage(player));
    }

    public static String translateDeathMessage(Player player) {
        EntityDamageEvent.DamageCause damageCause = player.getLastDamageCause().getCause();
        switch (damageCause) {
            case BLOCK_EXPLOSION:
                return getLocaleMessage("deaths.block-explosion");
            case CONTACT:
                return getLocaleMessage("deaths.contact");
            case CRAMMING:
                return getLocaleMessage("deaths.cramming");
            case DRAGON_BREATH:
                return getLocaleMessage("deaths.dragon-breath");
            case DROWNING:
                return getLocaleMessage("deaths.drowning");
            case DRYOUT:
                return getLocaleMessage("deaths.dryout");
            case ENTITY_ATTACK:
                return getLocaleMessage("deaths.entity-attack").replace("%entity%",player.getLastDamageCause().getEntity().getName());
            case ENTITY_EXPLOSION:
                return getLocaleMessage("deaths.entity-explosion").replace("%entity%",player.getLastDamageCause().getEntity().getName());
            case ENTITY_SWEEP_ATTACK:
                return getLocaleMessage("deaths.entity-sweep-attack").replace("%entity%",player.getLastDamageCause().getEntity().getName());
            case FALL:
                return getLocaleMessage("deaths.fall");
            case FALLING_BLOCK:
                return getLocaleMessage("deaths.falling-block");
            case FIRE:
                return getLocaleMessage("deaths.fire");
            case FIRE_TICK:
                return getLocaleMessage("deaths.fire-tick");
            case FLY_INTO_WALL:
                return getLocaleMessage("deaths.fly-into-wall");
            case HOT_FLOOR:
                return getLocaleMessage("deaths.hot-floor");
            case LAVA:
                return getLocaleMessage("deaths.lava");
            case LIGHTNING:
                return getLocaleMessage("deaths.lightning");
            case MAGIC:
                return getLocaleMessage("deaths.magic");
            case MELTING:
                return getLocaleMessage("deaths.melting");
            case POISON:
                return getLocaleMessage("deaths.poison");
            case PROJECTILE:
                return getLocaleMessage("deaths.projectile");
            case STARVATION:
                return getLocaleMessage("deaths.starvation");
            case SUFFOCATION:
                return getLocaleMessage("deaths.suffocation");
            case SUICIDE:
                return getLocaleMessage("deaths.suicide");
            case THORNS:
                return getLocaleMessage("deaths.thorns");
            case VOID:
                return getLocaleMessage("deaths.void");
            case WITHER:
                return getLocaleMessage("deaths.wither");
            case CUSTOM:
            default:
                return getLocaleMessage("deaths.custom");
        }
    }
}
