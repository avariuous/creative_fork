/*
 Creative TimePlay 2023

 Кнопка, которая меняет значения
 */

package timeplay.creativecoding.menu.buttons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class RadioButton {

    int currentChoice;
    int maxChoicesAmount;
    List<Runnable> choiceActions;
    ItemStack buttonItem;
    List<String> originalLore;
    String turnedPath;
    String itemLocalePath;
    static Map<ItemStack,RadioButton> radioButtonList = new HashMap<>();

    public RadioButton(Material material, String name, List<String> lore, int currentChoice,
                       int maxChoicesAmount, List<Runnable> choicesActions, String itemLocalePath,
                       String turnedPath) {
        setChoices(currentChoice, maxChoicesAmount, choicesActions);
        setItemButton(material, name, lore, itemLocalePath, turnedPath);
        radioButtonList.put(getButtonItem(),this);
    }
    private void setChoices(int currentChoice, int maxChoicesAmount, List<Runnable> choicesActions) {
        this.currentChoice = currentChoice;
        this.maxChoicesAmount = maxChoicesAmount;
        this.choiceActions = choicesActions;
    }

    private void setItemButton(Material material, String name, List<String> lore, String itemLocalePath, String chosenLocalePath) {
        this.originalLore = lore;
        this.itemLocalePath = itemLocalePath;
        this.turnedPath = chosenLocalePath;

        ItemStack buttonItem = new ItemStack(material,1);
        ItemMeta buttonItemMeta = buttonItem.getItemMeta();
        buttonItemMeta.setDisplayName(name);
        buttonItem.setItemMeta(buttonItemMeta);
        this.buttonItem = buttonItem;
        updateLore();
    }

    private void updateLore() {

        ItemMeta buttonItemMeta = buttonItem.getItemMeta();
        List<String> lore = new ArrayList<>();

        String turnedOn = getLocaleMessage(turnedPath+".turned-on");
        String turnedOff = getLocaleMessage(turnedPath+".turned-off");
        String turned;

        for (String loreLine : originalLore) {
            if (loreLine.matches("%[0-9]+%")) {
                int choiceNumber = Integer.parseInt((loreLine.replace("%","")));
                if (choiceNumber == currentChoice) turned = turnedOn;
                else turned = turnedOff;
                loreLine = loreLine.replace("%" + choiceNumber + "%", turned + getLocaleMessage(itemLocalePath + "." + choiceNumber, false));
            }
            lore.add(loreLine);
        }

        buttonItemMeta.setLore(lore);
        buttonItem.setItemMeta(buttonItemMeta);
    }
    public void onChoice() {

        if (this.currentChoice == 0) this.currentChoice = 1;
        int nextChoice = this.currentChoice+1;
        if (nextChoice > this.maxChoicesAmount) {
            this.currentChoice = 1;
            nextChoice = 1;
        }

        Runnable actions = this.choiceActions.get(nextChoice-1);
        actions.run();

        radioButtonList.remove(buttonItem);
        updateLore();
        radioButtonList.put(buttonItem,this);
    }

    public ItemStack getButtonItem() {
        return buttonItem;
    }

    public static RadioButton getRadioButtonByItemStack(ItemStack itemStack) {
        return radioButtonList.get(itemStack);
    }
}
