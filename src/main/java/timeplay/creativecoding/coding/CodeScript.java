/*
 Creative TimePlay 2023

 В этом классе описывается CodeScript - скрипт каждого плота
 Он хранится в файле codeScript.yml
 */
package timeplay.creativecoding.coding;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import timeplay.creativecoding.coding.actions.Action;
import timeplay.creativecoding.coding.actions.PlaySoundAction;
import timeplay.creativecoding.coding.actions.SendMessageAction;
import timeplay.creativecoding.coding.actions.ShowTitleAction;
import timeplay.creativecoding.coding.activators.*;
import timeplay.creativecoding.plots.Plot;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static timeplay.creativecoding.utils.ErrorUtils.sendCriticalErrorMessage;
import static timeplay.creativecoding.utils.ErrorUtils.sendWarningErrorMessage;
import static timeplay.creativecoding.utils.FileUtils.*;

public class CodeScript {

    public Plot linkedPlot;
    private File file;
    public List<Integer> blockActionsX = new ArrayList<>();

    //private List<Activator> activatorList = new ArrayList<>();

    public CodeScript(Plot linkedPlot, File file) {
        this.linkedPlot = linkedPlot;
        this.file = file;
        for (int x = 6; x < 98; x=x+2) {
            blockActionsX.add(x);
        }
    }

    public List<String> findActions(Plot plot, Object event) {
        List<String> lines = new ArrayList<>();
        String searchable = event.getClass().getSimpleName().toLowerCase().replace("event","");

        List<String> codeLines = YamlConfiguration.loadConfiguration(file).getStringList("code");
        for (String codeLine : codeLines) {
            if (codeLine.startsWith(searchable)) {
                lines.add(codeLine);
            }
        }
        return lines;

    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public boolean exists() {

        File linkedPlotFolder = getPlotFolder(linkedPlot);
        if (linkedPlotFolder == null) return false;

        return true;
    }

    public void addActivator(Block block, String mainType, String mainSubType) {

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        YamlConfiguration scriptConfig = YamlConfiguration.loadConfiguration(file);

        String path = "code.blocks.block" + x + "_" + y + "_" + z;
        scriptConfig.set(path + ".type", mainType);
        scriptConfig.set(path + ".subtype", mainSubType);
        scriptConfig.set(path + ".location.x",x);
        scriptConfig.set(path + ".location.y",y);
        scriptConfig.set(path + ".location.z",z);

        try {
            scriptConfig.save(file);
        } catch (IOException error) {
            sendCriticalErrorMessage("Произошла ошибка при сохранении файла скрипта. " + error.getLocalizedMessage());
        }
    }

    public void setActionBlockForScript(Block block, String actionType, String actionSubType) {

        // type - тип блока (Действие, Условие)
        // subtype - подтип (Написать сообщение)

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        YamlConfiguration scriptConfig = YamlConfiguration.loadConfiguration(file);

        String path = "code.blocks.block" + x + "_" + y + "_" + 4 + ".actions.block" + z;
        scriptConfig.set(path + ".type", actionSubType);
        scriptConfig.set(path + ".location.x",x);
        scriptConfig.set(path + ".location.y",y);
        scriptConfig.set(path + ".location.z",z);

        try {
            scriptConfig.save(file);
        } catch (IOException error) {
            sendCriticalErrorMessage("Произошла ошибка при сохранении файла скрипта. " + error.getLocalizedMessage());
        }
    }


    public void setActionBlock(Block block, String actionType, String actionSubtype, int actionParameter, List<String> arguments) {
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        YamlConfiguration scriptConfig = YamlConfiguration.loadConfiguration(file);
        String path = "code.blocks.block" + 4 + "_" + y + "_" + z + ".actions.block" + getBlockActionNumber(block);

        scriptConfig.set(path + ".type", actionType);
        scriptConfig.set(path + ".subtype", actionSubtype);
        scriptConfig.set(path + ".argument",actionParameter);

        scriptConfig.set(path + ".location.x",x);
        scriptConfig.set(path + ".arguments",arguments);

        try {
            scriptConfig.save(file);
            Bukkit.getLogger().info("Сохранено изменение файла скрипта " + block.getWorld().getName() + " " + file.getPath());
        } catch (IOException error) {
            sendCriticalErrorMessage("Произошла ошибка при сохранении файла скрипта. " + error.getLocalizedMessage());
        }
    }

    public int getBlockActionNumber(Block block) {
        return blockActionsX.indexOf(block.getX())+1;
    }

    /*public void addActivator(Activator activator) {
        this.activatorList.add(activator);
    }

    public void clearActivators() {
        this.activatorList.clear();
    }*/

    public void executeActivator(Activator activator, Entity entity) {
        YamlConfiguration script = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = script.getConfigurationSection("code.blocks");
        if (section == null) return;
        Set<String> keys = section.getKeys(false);

        for (String key : keys) {
            String path = "code.blocks." + key;
            if ((script.getString(path + ".subtype")) == null) return;
            if (script.getString(path + ".subtype").equalsIgnoreCase(activator.getName())) {
                ConfigurationSection actionsSection =  script.getConfigurationSection(path + ".actions");
                if (actionsSection == null) return;
                Set<String> actionsBlocks = actionsSection.getKeys(false);

                for (String actionBlock : actionsBlocks) {

                    String typePath = path + ".actions." + actionBlock + ".type";
                    String subtypePath = path + ".actions." + actionBlock + ".subtype";
                    String argumentsPath = path + ".actions." + actionBlock + ".arguments";
                    String parameterPath = path + ".actions." + actionBlock + ".parameter";

                    String actionType = script.getString(typePath);
                    String actionSubtype = script.getString(subtypePath);

                    List<String> actionArguments = script.getStringList(argumentsPath);
                    int actionParameter = script.getInt(parameterPath);
                    if (actionParameter == 0) {
                        actionParameter = 1;
                    }

                    if (actionSubtype == null) return;
                    if (actionSubtype.equalsIgnoreCase("send_message")) {
                        new SendMessageAction(entity,actionArguments,actionParameter).execute();
                    } else if (actionSubtype.equalsIgnoreCase("show_title")) {
                        new ShowTitleAction(entity,actionArguments,actionParameter).execute();
                    } else if (actionSubtype.equalsIgnoreCase("play_sound")) {
                        new PlaySoundAction(entity,actionArguments,actionParameter).execute();
                    }

                }
            }
        }
    }


    public void setSignLineType(Location location, String line) {
        Block block = location.getBlock();
        if (block.getType() == Material.OAK_WALL_SIGN) {
            Sign signBlock = (Sign) block.getState();
            signBlock.setLine(1,line);
            signBlock.update();
        }
    }

    public void setSignLineSubtype(Location location, String line) {
        Block block = location.getBlock();
        if (block.getType() == Material.OAK_WALL_SIGN) {
            Sign signBlock = (Sign) block.getState();
            signBlock.setLine(2,line);
            signBlock.update();
        }
    }

    public void setSignLineSelector(Location location, String line) {
        Block block = location.getBlock();
        if (block.getType() == Material.OAK_WALL_SIGN) {
            Sign signBlock = (Sign) block.getState();
            signBlock.setLine(3,line);
            signBlock.update();
        }
    }

    public void clear() {

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("code.blocks");

        config.set("old-code.blocks",null);
        if (section == null) return;
        Map<String,Object> newCode = section.getValues(true);
        config.set("old-code.blocks",newCode);
        config.set("code.blocks",null);
        //clearActivators();

        try {
            config.save(file);
        } catch (IOException exception) {
            sendWarningErrorMessage("Произошла ошибка при попытке сохранить новый код в старый... " + this.linkedPlot.worldName);
        }

    }

    public static List<Activator> getActivators() {

        List<Activator> activatorList = new ArrayList<>();
        activatorList.add(new PlayerJoinActivator());
        activatorList.add(new PlayerQuitActivator());
        activatorList.add(new PlayerDeathActivator());
        activatorList.add(new PlayerRespawnActivator());
        return activatorList;

    }

    public static List<Action> getActions() {

        List<Action> actionList = new ArrayList<>();
        actionList.add(new SendMessageAction());
        actionList.add(new ShowTitleAction());
        actionList.add(new PlaySoundAction());
        return actionList;

    }



}
