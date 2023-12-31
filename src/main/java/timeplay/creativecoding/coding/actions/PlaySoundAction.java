package timeplay.creativecoding.coding.actions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.plots.Plot;

import java.util.List;

import static timeplay.creativecoding.plots.PlotManager.getPlotByPlayer;
import static timeplay.creativecoding.utils.ErrorUtils.sendPlotErrorMessage;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;

public class PlaySoundAction extends Action {

    private ItemStack icon;
    private Material material;
    private String localePath;

    private Entity entity;
    private List<String> arguments;
    private int parameter;

    public PlaySoundAction(Entity entity, List<String> arguments, int messageSendType) {
        this.entity = entity;
        this.arguments = arguments;
        this.parameter = messageSendType;
    }

    public PlaySoundAction() {
        material = Material.MUSIC_DISC_CAT;
        localePath = "items.developer.actions.play-sound";
        icon = getIcon();
    }

    public ItemStack getIcon() {
        icon = new ItemStack(material,1);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(getLocaleItemName(localePath+".name"));
        meta.setLore(getLocaleItemDescription(localePath+".lore"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        icon.setItemMeta(meta);
        return icon;
    }

    public void execute() {

        Sound sound = Sound.valueOf("ENTITY_PLAYER_LEVELUP");
        int volume = 100;
        int pitch = 1;
        Location location = entity.getLocation();

        if (!arguments.isEmpty()) {
            try {
                sound = Sound.valueOf(arguments.get(0));
            } catch (IllegalArgumentException error) {
                if (!(entity instanceof Player)) return;
                Player player = (Player) entity;
                Plot plot = getPlotByPlayer(player);
                if (plot == null) return;
                sendPlotErrorMessage(plot,"При попытке проиграть звук обнаружен неправильное название звука");
            }
        }
        if (arguments.size() > 1) {
            volume = Integer.parseInt(arguments.get(1));
        }
        if (arguments.size() > 2) {
            pitch = Integer.parseInt(arguments.get(2));
        }
        if (arguments.size() > 3) {
            location = entity.getLocation();
        }

        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        player.playSound(location,sound,volume,pitch);

    }

    public String getName() {
        return "show_title";
    }

}
