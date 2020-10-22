package net.wdsj.mcserver.gui.bukkit.item;

import mc233.cn.wdsjlib.bukkit.utils.ItemBuilder;
import mc233.cn.wdsjlib.bukkit.utils.ItemUtils;
import mc233.cn.wdsjlib.global.config.ItemStackConfig;
import net.wdsj.servercore.compatible.XMaterial;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/19 22:04
 */
public class GuiItemConfigBukkit extends GuiItemBukkit {

    public GuiItemConfigBukkit(ItemStackConfig itemStackConfig) {
        super(new ItemBuilder(ItemUtils.readConfigItem(itemStackConfig, XMaterial.STONE)));
    }




}
