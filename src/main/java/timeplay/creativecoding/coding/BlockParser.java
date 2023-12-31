/*
 Creative TimePlay 2023

 Это парсер для блоков в /dev
 Он записывает все блоки в codeScript.yml
 */
package timeplay.creativecoding.coding;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import timeplay.creativecoding.plots.DevPlot;

import java.util.ArrayList;
import java.util.List;

import static timeplay.creativecoding.utils.ErrorUtils.sendPlotErrorMessage;
import static timeplay.creativecoding.utils.MessageUtils.getLocaleMessage;

public class BlockParser {

    public static void parseCode(DevPlot devPlot) {

        World world = devPlot.world;
        devPlot.linkedPlot.script.clear();

        for (int y = 1; y < devPlot.getFloors()*4; y=y+4) {
            for (int z = 4; z < 96; z=z+4) {

                // MainBlock - главный блок.
                // Это события, циклы, функции.
                // По Z это прохождение по каждой строчке

                Block mainBlock = world.getBlockAt(4,y,z);

                if (mainBlock.getType() != Material.AIR) {

                    BlockParser blockParser = new BlockParser();
                    String mainType = blockParser.getMainTypeByMaterial(mainBlock.getType());
                    String mainSubtype = blockParser.getSubtype(mainBlock);

                    devPlot.linkedPlot.script.addActivator(mainBlock,mainType,mainSubtype);
                }


                for (int x = 6; x < 96; x=x+2) {

                    // ActionBlock - блок действий.
                    // Это действия и условия.
                    // По X мы проходим каждый блок действия.

                    Block actionBlock = world.getBlockAt(x,y,z);

                    if (actionBlock.getType() != Material.AIR) {
                        Block chest = actionBlock.getRelative(BlockFace.UP);
                        Block sign = actionBlock.getRelative(BlockFace.SOUTH);

                        String actionType = "none";
                        String actionSubtype = "none";
                        String actionSelected = "default";
                        int actionParameter = 1;
                        List<String> arguments = new ArrayList<>();

                        if (chest.getType() == Material.CHEST) {
                            Chest chestB = (Chest) chest.getState();
                            for (ItemStack item : chestB.getBlockInventory().getContents()) {
                                if (item == null) continue;
                                arguments.add(item.getItemMeta().getDisplayName());
                            }
                        }

                        if (sign.getType() == Material.OAK_WALL_SIGN) {
                            Sign signB = (Sign) sign.getState();
                            actionType = signB.getLine(1);
                            actionSubtype = signB.getLine(2);
                            actionSelected = "default";

                            if (!actionType.contains("_")) {
                                sendPlotErrorMessage(devPlot.linkedPlot,"При обработке блоков кодинга оказался неизвестный тип блока '" + actionType + "'. Проверьте блок в /dev: X: " + x + " Y: " + y + " Z: " + z);
                                actionType = "unknown";
                            }
                            if (!(actionSubtype.isEmpty()) && !(actionSubtype.contains("_"))) {
                                sendPlotErrorMessage(devPlot.linkedPlot,"При обработке блоков кодинга оказался неизвестный подтип блока '" + actionSubtype + "' . Проверьте блок в /dev: X: " + x + " Y: " + y + " Z: " + z);
                                actionType = "unknown";
                            }
                        }

                        devPlot.linkedPlot.script.setActionBlock(actionBlock,actionType,actionSubtype,actionParameter,arguments);
                    }

                }

            }
        }

    }

    public String getMainTypeByMaterial(Material material) {
        switch(material) {
            case DIAMOND_BLOCK:
                return "event_player";
            case GOLD_BLOCK:
                return "event_world";
            case LAPIS_BLOCK:
                return "function";
            case EMERALD_BLOCK:
                return "cycle";
            default:
                return "unknown";
        }
    }

    public String getActionBlockType(Material material) {
        switch(material) {
            case COBBLESTONE:
                return "action_player";
            case IRON_BLOCK:
                return "action_var";
            case NETHER_BRICKS:
                return "action_world";
            case LAPIS_ORE:
                return "exec_function";
            default:
                return "unknown";
        }
    }

    public String getSubtype(Block block) {
         Block signBlock = block.getRelative(BlockFace.SOUTH);

         if (signBlock.getType() == Material.OAK_WALL_SIGN) {
             Sign sign = (Sign) signBlock.getState();
             String signText = sign.getLine(2);
             if (signText.isEmpty()) return "none";
             else {
                 return signText;
             }
         }

         return "none";

    }


}
