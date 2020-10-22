package net.wdsj.mcserver.gui.common.creator;

import net.wdsj.mcserver.gui.common.config.GuiItemStackConfig;
import net.wdsj.mcserver.gui.common.item.GuiItem;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 18:59
 */
public interface GuiItemConfigCreator<Handler, Item> {

    GuiItem<Handler, Item> create(Handler handler,Map<String, String> args, GuiItemStackConfig config);

}
