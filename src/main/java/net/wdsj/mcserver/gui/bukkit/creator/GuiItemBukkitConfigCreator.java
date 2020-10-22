package net.wdsj.mcserver.gui.bukkit.creator;

import mc233.cn.wdsjlib.global.config.ItemStackConfig;
import net.wdsj.mcserver.gui.bukkit.item.GuiItemConfigBukkit;
import net.wdsj.mcserver.gui.common.config.GuiItemStackConfig;
import net.wdsj.mcserver.gui.common.creator.GuiItemConfigCreator;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 19:03
 */
public class GuiItemBukkitConfigCreator implements GuiItemConfigCreator<Player, ItemStack> {


    @Override
    public GuiItem<Player, ItemStack> create(Player player, Map<String, String> args, GuiItemStackConfig config) {
        ItemStackConfig itemStackConfig = config.build(player,args);
        return new GuiItemConfigBukkit(itemStackConfig);
    }
}
