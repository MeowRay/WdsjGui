package net.wdsj.mcserver.gui.common.gui.menu;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.wdsj.mcserver.gui.common.builder.GuiItemRenderBuilder;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;
import net.wdsj.servercore.eunm.inventory.InventoryType;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/18 12:58
 */
public  class GuiMenuStatic<Handler, Item> extends GuiMenu<Handler, Item> {

    private final TIntObjectHashMap<GuiMenuRenderItem<Handler, Item>> staticGuiItemMap = new TIntObjectHashMap<>();

    public GuiMenuStatic(Handler owner, InventoryType inventoryType, String title) {
        super(owner, inventoryType, title);
    }

    public void setItem(int slot, GuiItem<Handler, Item> guiItem) {
        delItem(slot);
        GuiMenuRenderItem<Handler, Item> handlerItemGuiMenuRenderItem = new GuiMenuRenderItem<>(new RenderItem<>(slot, guiItem));
        staticGuiItemMap.put(slot, handlerItemGuiMenuRenderItem);
        addRenderItem(handlerItemGuiMenuRenderItem);

    }

    public void delItem(int slot) {
        final GuiMenuRenderItem<Handler, Item> remove = staticGuiItemMap.remove(slot);
        if (remove != null) delRenderItem(remove);
    }


}
