package net.wdsj.mcserver.gui.common.model;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;

import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/18 17:30
 */
public interface GuiItemModel<Handler, Item> {

    List<GuiMenuRenderItem<Handler, Item>> getRenderItems(GuiMenu<Handler, Item> guiMenu);

}
