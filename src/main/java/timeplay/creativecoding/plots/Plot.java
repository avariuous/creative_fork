/*
 Creative TimePlay 2023

 Плот это мир Minecraft, который имеет свои параметры
 по типу названия, описания, значка, доступности посещения.

 Концепция такова - если игрок хочет зайти в плот, то
 мир плота подгружается и телепортирует игрока.
 Если игроков в мире плота нет, то мир и плот отгружаются,
 дабы сохранить ресурсы сервера.

 */

package timeplay.creativecoding.plots;

import org.bukkit.*;
import org.bukkit.entity.*;
import timeplay.creativecoding.coding.BlockParser;
import timeplay.creativecoding.coding.CodeScript;
import timeplay.creativecoding.coding.activators.PlayerJoinActivator;
import timeplay.creativecoding.utils.FileUtils;
import timeplay.creativecoding.utils.PlayerUtils;
import timeplay.creativecoding.utils.WorldUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static timeplay.creativecoding.Main.clearPlayer;
import static timeplay.creativecoding.Main.teleportToLobby;
import static timeplay.creativecoding.utils.ErrorUtils.*;
import static timeplay.creativecoding.utils.FileUtils.*;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;
import static timeplay.creativecoding.utils.WorldUtils.generateWorld;
import static timeplay.creativecoding.plots.PlotManager.*;

public class Plot {

    public World world;
    public String worldName;
    public String worldID;

    public String owner;
    public String ownerGroup;

    public boolean isLoaded;
    public DevPlot devPlot;

    public String plotName;
    public String plotDescription;
    public Material plotIcon;
    public String plotCustomID;

    public int plotReputation;
    public Mode plotMode;
    public Sharing plotSharing;
    public Category plotCategory;

    public int worldSize;

    public int entitiesLimit;
    public int redstoneOperationsLimit;
    public int lastRedstoneOperationsAmount;

    public int playerDamageFlag;
    public int joinMesssagesFlag;
    public int dayCycleFlag;
    public int weatherFlag;
    public int fireSpreadFlag;
    public int blockInteractFlag;

    public CodeScript script;

    public enum Mode {
        // Режим игры
        PLAYING() {
            public void onPlayerJoin(Player player) {
                player.setGameMode(GameMode.ADVENTURE);
                Plot plot = getPlotByPlayer(player);
                if (plot != null) player.setGameMode(plot.owner.equalsIgnoreCase(player.getName()) ? GameMode.CREATIVE : GameMode.ADVENTURE);
            }
        // Режим строительства
        }, BUILD() {
            public void onPlayerJoin(Player player) {
                player.setGameMode(GameMode.ADVENTURE);
                Plot plot = getPlotByPlayer(player);
                if (plot != null) player.setGameMode(plot.owner.equalsIgnoreCase(player.getName()) ? GameMode.CREATIVE : GameMode.ADVENTURE);
            }
        };
        public void onPlayerJoin(Player player) {}
        public void onPlayerQuit() {}
        }

    public enum Sharing {
        PUBLIC, PRIVATE, CLOSED
    }
    public enum Category {
        SANDBOX(getLocaleMessage("world.categories.sandbox")),
        ADVENTURE(getLocaleMessage("world.categories.adventure")),
        STRATEGY(getLocaleMessage("world.categories.strategy")),
        ARCADE(getLocaleMessage("world.categories.arcade")),
        ROLEPLAY(getLocaleMessage("world.categories.roleplay")),
        STORY(getLocaleMessage("world.categories.story")),
        SIMULATOR(getLocaleMessage("world.categories.simulator")),
        EXPERIMENT(getLocaleMessage("world.categories.experiment"));

        private final String name;

        Category(String localeMessage) {
            this.name = localeMessage;
        }

        public String getName() {
            return name;
        }
    }

    public enum PlayersType {

        UNIQUE("players.unique"),
        LIKED("players.liked"),
        DISLIKED("players.disliked"),
        WHITELISTED("players.whitelist"),
        BLACKLISTED("players.blacklist"),
        BUILDERS_TRUSTED("players.builders.trusted"),
        BUILDERS_NOT_TRUSTED("players.builders.not-trusted"),
        DEVELOPERS_TRUSTED("players.developers.trusted"),
        DEVELOPERS_NOT_TRUSTED("players.developers.not-trusted"),
        DEVELOPERS_GUESTS("players.developers.guests");


        private final String path;

        PlayersType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
    
    // Создание мира
    public Plot(Player player) {
        owner = player.getName();
        ownerGroup = getOwnerGroup();

        plotName = getLocaleMessage("creating-world.default-world-name",false);
        plotDescription = getLocaleMessage("creating-world.default-world-description",false);
        plotIcon = Material.DIAMOND;

        plotMode = Mode.BUILD;
        plotCategory = Category.SANDBOX;
        plotSharing = Sharing.PUBLIC;
        plotReputation = 0;

        lastRedstoneOperationsAmount = 0;
        redstoneOperationsLimit = PlayerUtils.getPlayerPlotRedstoneOperationsLimit(ownerGroup);
        entitiesLimit = PlayerUtils.getPlayerPlotEntitiesLimit(ownerGroup);

        worldSize = 256;

        plots.add(this);
        create(this);

        devPlot = new DevPlot(this);
        script = new CodeScript(this,getPlotScriptFile(this));
        loadPlotFlags(this);
    }

    // Загрузка в базу миров
    public Plot(String fileName) {
        worldName = fileName;
        owner = this.getOwner();
        ownerGroup = getOwnerGroup();

        plotName = this.getPlotName();
        plotDescription = this.getPlotDescription();
        plotIcon = this.getPlotIcon();

        isLoaded = false;

        plotMode = Mode.BUILD;
        plotCategory = this.getPlotCategory();
        plotSharing = this.getWorldSharing();
        plotReputation = this.getReputation();

        lastRedstoneOperationsAmount = 0;
        redstoneOperationsLimit = PlayerUtils.getPlayerPlotRedstoneOperationsLimit(ownerGroup);
        entitiesLimit = PlayerUtils.getPlayerPlotEntitiesLimit(ownerGroup);
        worldSize = 256;

        worldID = fileName.replace("plot","");
        plotCustomID = this.getPlotCustomID();
        loadPlotFlags(this);

        devPlot = new DevPlot(this);
        plots.add(this);
        script = new CodeScript(this,getPlotScriptFile(this));
    }

    // Создание плота
    public void create(Plot plot) {
        Player player = Bukkit.getPlayer(plot.owner);
        String worldName = "plot" + WorldUtils.generateWorldID();
        player.sendTitle(getLocaleMessage("creating-world.title"),getLocaleMessage("creating-world.subtitle"),10,300,40);
        if (!generateWorld(plot,player,worldName)) {
            player.clearTitle();
            sendPlayerErrorMessage(player,"§cПроизошла ошибка при создании мира... \n§cОбратитесь к администрации!");
        }
    }

    // Получить доступность посещения плота
    public Sharing getWorldSharing() {
        if (getPlotConfig(this).get("sharing") != null) {
            try {
                return Sharing.valueOf(String.valueOf(getPlotConfig(this).get("sharing")));
            } catch (Exception error) {
                return Sharing.PRIVATE;
            }
        } else {
            return Sharing.PRIVATE;
        }
    }

    // Получить название плота
    public String getPlotName() {
        if (getPlotConfig(this).get("name") != null) {
            return String.valueOf(getPlotConfig(this).get("name"));
        } else {
            return "Неизвестное название";
        }
    }

    // Получить описание плота
    public String getPlotDescription() {
        if (getPlotConfig(this).get("description") != null) {
            return String.valueOf(getPlotConfig(this).get("description"));
        } else {
            return "Неизвестное описание";
        }
    }

    // Получить описание плота
    public String getPlotCustomID() {
        if (getPlotConfig(this).get("customID") != null) {
            return String.valueOf(getPlotConfig(this).get("customID"));
        } else {
            return this.worldID;
        }
    }

    public Category getPlotCategory() {
        if (getPlotConfig(this).get("category") != null) {
            try {
                return Category.valueOf(String.valueOf(getPlotConfig(this).get("category")));
            } catch (Exception error) {
                return Category.SANDBOX;
            }
        } else {
            return Category.SANDBOX;
        }
    }

    // Получить значок плота
    public Material getPlotIcon() {
        if (getPlotConfig(this).get("icon") != null) {
            if (String.valueOf(getPlotConfig(this).get("icon")).contains("AIR")) return Material.DIAMOND;
            return Material.valueOf(String.valueOf(getPlotConfig(this).get("icon")));
        } else {
            return Material.REDSTONE;
        }
    }

    public int getOnline() {
        List<Player> playersList = this.getPlayers();
        return playersList.size();
    }

    public int getReputation() {
        try {
            return (getPlayersFromPlotConfig(this, PlayersType.LIKED).size() - getPlayersFromPlotConfig(this, PlayersType.DISLIKED).size());
        } catch (Exception error) {
            return 0;
        }
    }

    public Set<String> getAllPlayersFromConfig() {
        Set<String> allPlayers = new HashSet<>();
        try {
            List<String> onlinePlayers = new ArrayList<>();
            List<String> trustedBuilders = getPlayersFromPlotConfig(this, PlayersType.BUILDERS_TRUSTED);
            List<String> notTrustedBuilders = getPlayersFromPlotConfig(this, PlayersType.BUILDERS_NOT_TRUSTED);
            List<String> trustedDevelopers = getPlayersFromPlotConfig(this, PlayersType.BUILDERS_NOT_TRUSTED);
            List<String> notTrustedDevelopers = getPlayersFromPlotConfig(this, PlayersType.BUILDERS_NOT_TRUSTED);

            this.getPlayers().forEach(player -> onlinePlayers.add(player.getName()));

            allPlayers.addAll(trustedBuilders);
            allPlayers.addAll(notTrustedBuilders);
            allPlayers.addAll(trustedDevelopers);
            allPlayers.addAll(notTrustedDevelopers);
            allPlayers.addAll(onlinePlayers);
            allPlayers.remove(this.owner);

            return allPlayers;
        } catch (Exception error) {
            return allPlayers;
        }
    }

    public String getBuilders() {
        try {
            List<String> trustedBuilders = getPlayersFromPlotConfig(this, PlayersType.BUILDERS_TRUSTED);
            List<String> notTrustedBuilders = getPlayersFromPlotConfig(this, PlayersType.BUILDERS_NOT_TRUSTED);
            trustedBuilders.addAll(notTrustedBuilders);
            String builders = String.join(", ",trustedBuilders);
            return builders;
        } catch (Exception error) {
            return "";
        }
    }

    public String getDevelopers() {
        try {
            List<String> trustedDevelopers = getPlayersFromPlotConfig(this, PlayersType.DEVELOPERS_TRUSTED);
            List<String> notTrustedDevelopers = getPlayersFromPlotConfig(this, PlayersType.DEVELOPERS_NOT_TRUSTED);
            List<String> guestDevelopers = getPlayersFromPlotConfig(this, PlayersType.DEVELOPERS_GUESTS);
            trustedDevelopers.addAll(notTrustedDevelopers);
            trustedDevelopers.addAll(guestDevelopers);
            String developers = String.join(", ",trustedDevelopers);
            return developers;
        } catch (Exception error) {
            return "";
        }
    }

    public int getUniques() {
        try {
            return (getPlayersFromPlotConfig(this, PlayersType.UNIQUE).size());
        } catch (Exception error) {
            return 0;
        }
    }

    public long getCreationTime() {
        try {
            return Long.parseLong(String.valueOf(getPlotConfig(this).get("creation-time")));
        } catch (Exception error) {
            return 0;
        }
    }

    public long getLastActivityTime() {
        try {
            return Long.parseLong(String.valueOf(getPlotConfig(this).get("last-activity-time")));
        } catch (Exception error) {
            return 0;
        }
    }

    public List<Player> getPlayers() {
        List<Player> playerList = new ArrayList<>();
        try {
            if (Bukkit.getWorld(this.worldName) == null) return playerList;
            playerList.addAll(Bukkit.getWorld(this.worldName).getPlayers());
            if (devPlot.isLoaded) {
                playerList.addAll(devPlot.world.getPlayers());
            }
            return playerList;
        } catch (Exception error) {
            return playerList;
        }
    }

    public String getOwner() {
        if (getPlotConfig(this).get("owner") != null) {
            return String.valueOf(getPlotConfig(this).get("owner"));
        } else {
            return "Неизвестный владелец";
        }
    }

    public String getOwnerGroup() {
        if (getPlotConfig(this).get("owner-group") != null) {
            return String.valueOf(getPlotConfig(this).get("owner-group"));
        } else {
            return "default";
        }
    }

    // Телепортировать игрока на плот
    public static void teleportToPlot(Player player, Plot plot) {
        if (!(plot.plotSharing == Sharing.PUBLIC)) {
            if (!plot.owner.equalsIgnoreCase(player.getName())) {
                if (!(player.hasPermission("creative.private.bypass"))) {
                    player.sendMessage(getLocaleMessage("private-plot", player));
                    return;
                }
            }
        }
        if (!plot.owner.equalsIgnoreCase(player.getName()) && FileUtils.getPlayersFromPlotConfig(plot,PlayersType.BLACKLISTED).contains(player.getName())) {
            player.sendMessage(getLocaleMessage("blacklisted-in-plot", player));
            return;
        }
        player.sendTitle(getLocaleMessage("teleporting-to-world.title"),getLocaleMessage("teleporting-to-world.subtitle"),15,9999,15);
        if (!plot.isLoaded) {
            loadPlot(plot);
        }
        clearPlayer(player);
        player.teleport(plot.world.getSpawnLocation());
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,100,2);
        (plot.plotMode == Mode.PLAYING ? Mode.PLAYING : Mode.BUILD).onPlayerJoin(player);
        clearPlayer(player);
        player.sendTitle("","");
        if (!getPlayersFromPlotConfig(plot, PlayersType.UNIQUE).contains(player.getName())) {
            addPlayerToListInPlotConfig(plot,player.getName(), PlayersType.UNIQUE);
        }
        if (plot.owner.equalsIgnoreCase(player.getName())) {
            setPlotConfigParameter(plot,"owner-group",PlayerUtils.getGroup(player));
            plot.ownerGroup = PlayerUtils.getGroup(player);
            if (plot.script.exists() && plot.devPlot.isLoaded) {
                BlockParser.parseCode(plot.devPlot);
            }
        }
        plot.script.executeActivator(new PlayerJoinActivator(), player);
    }

    // Телепортировать игрока в мир разработки плота
    public void teleportToDevPlot(Player player) {
        player.sendTitle(getLocaleMessage("teleporting-to-world.title"),getLocaleMessage("teleporting-to-world.subtitle"),15,9999,15);
        devPlot.loadDevPlotWorld();
        clearPlayer(player);
        player.teleport(this.devPlot.world.getSpawnLocation());
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,100,2);
    }

    public void removeDeveloper(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
            if (this == plot) {
                if (player.getGameMode() == GameMode.CREATIVE) {
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }
        }
        removePlayerFromListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_NOT_TRUSTED);
        removePlayerFromListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_TRUSTED);
    }

    public void removeBuilder(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
            if (this == plot) {
                if (player.getGameMode() == GameMode.CREATIVE) {
                    player.setGameMode(GameMode.ADVENTURE);
                }
                if (getDevPlot(player) != null) {
                    teleportToPlot(player,this);
                }
            }
        }
        removePlayerFromListInPlotConfig(this,nickname,PlayersType.BUILDERS_NOT_TRUSTED);
        removePlayerFromListInPlotConfig(this,nickname,PlayersType.BUILDERS_TRUSTED);
    }

    public void setDeveloperGuest(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
             if (this == plot) {
                    player.sendMessage(getLocaleMessage("world.players.developers.player-guest").replace("%player%",player.getName()));
                    player.playSound(player.getLocation(),Sound.ENTITY_CAT_AMBIENT,100,1);
                }
            }
        addPlayerToListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_GUESTS);
    }

    public void setDeveloperTrusted(String nickname, boolean isTrusted) {
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
            if (this == plot) {
                if (!isTrusted) {
                    player.sendMessage(getLocaleMessage("world.players.developers.player").replace("%player%",player.getName()));
                    player.playSound(player.getLocation(),Sound.ENTITY_CAT_AMBIENT,100,1);
                    if (getDevPlot(player) != null) player.setGameMode(GameMode.CREATIVE);
                }
            }
        }
        if (isTrusted) {
            removePlayerFromListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_NOT_TRUSTED);
            addPlayerToListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_TRUSTED);
        } else {
            addPlayerToListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_NOT_TRUSTED);
        }
        removePlayerFromListInPlotConfig(this,nickname,PlayersType.DEVELOPERS_GUESTS);
    }

    public void setBuilderTrusted(String nickname, boolean isTrusted) {
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
            if (this == plot) {
                if (!isTrusted) {
                    player.sendMessage(getLocaleMessage("world.players.builders.player").replace("%player%",player.getName()));
                    player.playSound(player.getLocation(),Sound.ENTITY_CAT_AMBIENT,100,1);
                    if (getDevPlot(player) != null) return;
                    player.setGameMode(GameMode.CREATIVE);
                }
            }
        }
        if (isTrusted) {
            removePlayerFromListInPlotConfig(this,nickname,PlayersType.BUILDERS_NOT_TRUSTED);
            addPlayerToListInPlotConfig(this,nickname,PlayersType.BUILDERS_TRUSTED);
        } else {
            addPlayerToListInPlotConfig(this,nickname,PlayersType.BUILDERS_NOT_TRUSTED);
        }
    }

    public void kickPlayer(Player player) {
        System.out.println(player.getName() + "kick");
        Plot plot = getPlotByPlayer(player);
        if (this == plot) {
            teleportToLobby(player);
            player.sendMessage(getLocaleMessage("world.players.kick.player").replace("%player%",player.getName()));
            player.playSound(player.getLocation(),Sound.ENTITY_CAT_HURT,100,1);
        }
    }

    public void addBlacklist(String nickname) {
        System.out.println(nickname);
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
            System.out.println("test! ");
            if (this == plot) {
                teleportToLobby(player);
                player.sendMessage(getLocaleMessage("world.players.black-list.player").replace("%player%",player.getName()));
                player.playSound(player.getLocation(),Sound.ENTITY_CAT_HURT,100,1);
            }
        }
        addPlayerToListInPlotConfig(this,nickname,PlayersType.BLACKLISTED);
    }

    public void removeBlacklist(String nickname) {
        removePlayerFromListInPlotConfig(this,nickname,PlayersType.BLACKLISTED);
    }

    public void addWhitelist(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        if (player != null) {
            Plot plot = getPlotByPlayer(player);
            if (this == plot) {
                player.sendMessage(getLocaleMessage("world.players.white-list.player").replace("%player%",player.getName()));
                player.playSound(player.getLocation(),Sound.ENTITY_CAT_AMBIENT,100,1);
            }
        }
        addPlayerToListInPlotConfig(this,nickname,PlayersType.BLACKLISTED);
    }
}
