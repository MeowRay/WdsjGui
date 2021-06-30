package net.wdsj.mcserver.gui.common.creator;

import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder;
import mc233.cn.wdsjlib.global.config.ItemStackConfig;
import net.wdsj.mcserver.gui.common.config.GuiItemStackConfig;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.item.GuiItemCommon;
import net.wdsj.servercore.compatible.XMaterial;

import java.util.Map;

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/6/28 15:04
 */
public class GuiItemCommonConfigCreator<Handler, Item> implements GuiItemConfigCreator<Handler, Item> {
    @Override
    public GuiItem<Handler, Item> create(Handler handler, Map<String, String> args, GuiItemStackConfig config) {
        ItemStackConfig itemStackConfig = config.build(handler, args);
        return new GuiItemCommon<>(ItemCommonBuilder.createFromConfig(itemStackConfig, XMaterial.STONE));
    }
}
