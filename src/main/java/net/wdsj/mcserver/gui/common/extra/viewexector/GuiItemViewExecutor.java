package net.wdsj.mcserver.gui.common.extra.viewexector;

import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.servercore.interfaces.Executor;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/10 23:48
 */
public interface GuiItemViewExecutor<Handler> extends Executor<Handler> {

    ItemCommonBuilder getViewBuilder(Handler handler);

    GuiItem<Handler , ?> getItem(Handler handler);

}
