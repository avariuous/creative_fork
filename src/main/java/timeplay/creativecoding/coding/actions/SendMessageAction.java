package timeplay.creativecoding.coding.actions;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import timeplay.creativecoding.coding.activators.Activator;

import java.util.List;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemDescription;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleItemName;

public class SendMessageAction extends Action {

    private ItemStack icon;
    private Material material;
    private String localePath;

    private Entity entity;
    private List<String> messages;
    private int parameter;

    public SendMessageAction(Entity entity, List<String> messages, int messageSendType) {
        this.entity = entity;
        this.messages = messages;
        this.parameter = messageSendType;
    }

    public SendMessageAction() {
        material = Material.BOOK;
        localePath = "items.developer.actions.send-message";
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
        if (parameter == 1) {
            for (String message : messages) {
                entity.sendMessage(message);
            }
        }
        if (parameter == 2) {
            entity.sendMessage(String.join(" ",messages));
        }
    }

    public String getName() {
        return "send_message";
    }

}
