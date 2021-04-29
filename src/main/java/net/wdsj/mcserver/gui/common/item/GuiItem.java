package net.wdsj.mcserver.gui.common.item;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.servercore.eunm.inventory.InventoryAction;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/11 15:21
 */
public interface GuiItem<Handler,Item>  extends Cloneable {

    Item getItem();

    Item getItemView(Handler handler);

    void execute(GuiMenu<Handler,Item> menu ,Handler handler , InventoryAction inventoryAction);


}
