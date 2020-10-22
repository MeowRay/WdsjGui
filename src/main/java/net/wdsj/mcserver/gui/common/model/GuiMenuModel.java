package net.wdsj.mcserver.gui.common.model;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.servercore.eunm.inventory.InventoryType;

import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 16:38
 */
public interface GuiMenuModel<Handler, Item> {


    <T extends GuiMenu<Handler, Item>> T create(GuiMenuRenderAdapter<Handler, Item> renderAdapter, Handler handler);

    default GuiMenu<Handler, Item> createGuiMenu(GuiMenuRenderAdapter<Handler, Item> renderAdapter, Handler owner, InventoryType inventoryType, String title) {
        return new GuiMenu<Handler, Item>(owner, inventoryType, title) {
            @Override
            public GuiMenuRenderAdapter<Handler, Item> renderAdapter() {
                return renderAdapter;
            }
        };
    }

    List<GuiMenuRenderItem<Handler, Item>>  getRenderItems(Handler handler);

    InventoryType getInventoryType();


}
