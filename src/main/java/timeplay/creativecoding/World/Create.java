package timeplay.creativecoding.World;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.ArrayList;

public class Create {
    public static void CreateWorld(Player player) {
        String nickname = player.getName();
        String worldname = nickname;
        try {
            ArrayList<String> worlds = new ArrayList<String>();
            player.sendTitle("§aЗагрузка...","§7Подождите несколько секунд...",10,100,40);
            worlds.add(nickname + "1");
            Bukkit.createWorld(new WorldCreator(worldname).type(WorldType.FLAT).generateStructures(false));
            Bukkit.getWorld(worldname).getWorldBorder().setSize(100);
            setGamerule(worldname,GameRule.DO_MOB_SPAWNING,false);
            setGamerule(worldname,GameRule.DO_DAYLIGHT_CYCLE,false);
            setGamerule(worldname,GameRule.MOB_GRIEFING,false);
            setGamerule(worldname,GameRule.DO_WEATHER_CYCLE,false);
            setGamerule(worldname,GameRule.SHOW_DEATH_MESSAGES,true);
            setGamerule(worldname,GameRule.DO_IMMEDIATE_RESPAWN,true);
            Bukkit.getWorld(worldname).setTime(0);
            player.teleport(Bukkit.getWorld(worldname).getSpawnLocation());
            player.sendTitle("§6§lДОБРО ПОЖАЛОВАТЬ","§7Это §nтвой§7 мир, делай что хочешь");
            player.playSound(player.getLocation(), Sound.valueOf("UI_TOAST_CHALLENGE_COMPLETE"),100,2);
            player.sendMessage("");
            player.sendMessage("§6§lДОБРО ПОЖАЛОВАТЬ В ТВОЙ МИР!");
            player.sendMessage("§7Здесь ты сможешь сотворить что угодно.");
            player.sendMessage("");
            player.sendMessage("§7 Паркуры, PvP-режимы, прятки и многие другие режимы...");
            player.sendMessage("§7 Ограничение лишь размер мира и возможности :(");
            player.sendMessage("§a Цель: Создай свой уникальный режим и собери 1000 посетителей");
            player.sendMessage("");
            player.getInventory().clear();
            player.setGameMode(GameMode.CREATIVE);
            for (int i = 0; i < worlds.size(); i++) {
                player.sendMessage(worlds.get(i));
            }
            for (Entity entity : Bukkit.getWorld(worldname).getEntities()) {
                if (entity instanceof Zombie) {
                    entity.remove();
                }
                if (entity instanceof Spider) {
                    entity.remove();
                }
                if (entity instanceof Creeper) {
                    entity.remove();
                }
                if (entity instanceof Skeleton) {
                    entity.remove();
                }
            }
        } catch(NullPointerException error) {
            player.playSound(player.getLocation(),Sound.valueOf("BLOCK_ANVIL_DESTROY"),100,2);
            player.sendMessage("");
            player.sendMessage("§cПроизошла критическая ошибка :(");
            player.sendMessage("§cКод ошибки: §lNullPointerException");
            player.sendMessage("");
        }
    }

    public static void setGamerule(String worldname, GameRule gameRule, boolean bool) {
        Bukkit.getWorld(worldname).setGameRule(gameRule,bool);
    }
}

