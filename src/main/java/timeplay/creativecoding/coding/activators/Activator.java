package timeplay.creativecoding.coding.activators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.coding.actions.Action;
import timeplay.creativecoding.plots.Plot;

import java.util.List;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;

public abstract class Activator {

    public ItemStack icon;
    private Material material;
    private String localePath;

    private List<Action> actions;

    public ItemStack getIcon() {
        icon = new ItemStack(material,1);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(getLocaleItemName(localePath+".name"));
        meta.setLore(getLocaleItemDescription(localePath+".lore"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        icon.setItemMeta(meta);
        return icon;
    }


    public Activator () {
    }

    public Activator (List<Action> actions) {
        this.actions = actions;
    }

    public String getName() {
        return "activator";
    }

}
