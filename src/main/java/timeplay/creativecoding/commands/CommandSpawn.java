package timeplay.creativecoding.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import timeplay.creativecoding.utils.CooldownUtils;

import static timeplay.creativecoding.Main.teleportToLobby;
import static timeplay.creativecoding.utils.CooldownUtils.getCooldown;
import static timeplay.creativecoding.utils.CooldownUtils.setCooldown;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class CommandSpawn implements CommandExecutor {

    final static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Creative");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (!(sender instanceof Player) || sender.hasPermission("creative.spawnothers")) {
                if (!(Bukkit.getPlayer(args[0]) == null)) {
                    teleportToLobby((Bukkit.getPlayer(args[0])));
                } else {
                    sender.sendMessage(getLocaleMessage("not-found-player"));
                }
            } else {
                sender.sendMessage(getLocaleMessage("no-perms"));
            }
        } else {
            if (sender instanceof Player) {
                if (getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND) > 0) {
                    sender.sendMessage(getLocaleMessage("cooldown").replace("%cooldown%",String.valueOf(getCooldown(((Player) sender).getPlayer(), CooldownUtils.CooldownType.GENERIC_COMMAND))));
                    return true;
                }
                setCooldown(((Player) sender).getPlayer(),plugin.getConfig().getInt("cooldowns.generic-command"), CooldownUtils.CooldownType.GENERIC_COMMAND);
                teleportToLobby(((Player) sender).getPlayer());
            }
        }
        return true;
    }
}
