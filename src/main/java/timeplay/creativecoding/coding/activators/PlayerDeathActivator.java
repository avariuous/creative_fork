package timeplay.creativecoding.coding.activators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.coding.actions.Action;

import java.util.List;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;

public class PlayerDeathActivator extends Activator {

    private ItemStack icon;
    private Material material;
    private String localePath;

    public PlayerDeathActivator() {
        material = Material.SKELETON_SKULL;
        localePath = "items.developer.events.player-death";
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

    public String getName() {
        return "player_death";
    }

}
