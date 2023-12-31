package timeplay.creativecoding.coding.actions;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;

public class ShowTitleAction extends Action {

    private ItemStack icon;
    private Material material;
    private String localePath;

    private Entity entity;
    private List<String> arguments;
    private int parameter;

    public ShowTitleAction(Entity entity, List<String> arguments, int messageSendType) {
        this.entity = entity;
        this.arguments = arguments;
        this.parameter = messageSendType;
    }

    public ShowTitleAction() {
        material = Material.OAK_SIGN;
        localePath = "items.developer.actions.show-title";
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

        String title = "";
        String subtitle = "";
        int fadeIn = 20;
        int stay = 20;
        int fadeOut = 20;

        if (!arguments.isEmpty()) {
            title = arguments.get(0);
        }
        if (arguments.size() > 1) {
            subtitle = arguments.get(1);
        }
        if (arguments.size() > 2) {
            stay = Integer.parseInt(arguments.get(2));
        }
        if (arguments.size() > 3) {
            fadeIn = Integer.parseInt(arguments.get(3));
        }
        if (arguments.size() > 4) {
            fadeOut = Integer.parseInt(arguments.get(4));
        }

        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        player.sendTitle(title,subtitle,fadeIn,stay,fadeOut);
    }

    public String getName() {
        return "show_title";
    }

}
