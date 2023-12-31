package timeplay.creativecoding.coding.activators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;

public class PlayerRespawnActivator extends Activator {

    private ItemStack icon;
    private Material material;
    private String localePath;

    public PlayerRespawnActivator() {
        material = Material.PLAYER_HEAD;
        localePath = "items.developer.events.player-respawn";
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
        return "player_respawn";
    }

}
