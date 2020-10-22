package net.wdsj.mcserver.gui.common.adapter;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.servercore.eunm.inventory.InventoryType;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 16:16
 */
public interface GuiMenuRenderAdapter<Handler, Item> {

    void build(Handler handler, GuiMenu<Handler, Item> guiMenu);

    void reBuild(Handler handler, GuiMenu<Handler, Item> guiMenu);

    int open(Handler handler, GuiMenu<Handler, Item> guiMenu);

    void render(Handler handler, int windowId, GuiMenu<Handler, Item> guiMenu);

    void changeTitle(Handler player, InventoryType inventoryType, String title);

    void close(Handler handler , int windowId);
}
