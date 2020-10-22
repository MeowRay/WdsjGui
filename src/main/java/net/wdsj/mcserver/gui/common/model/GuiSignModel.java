package net.wdsj.mcserver.gui.common.model;

import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.gui.sign.GuiSign;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.servercore.eunm.inventory.InventoryType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 16:38
 */
public interface GuiSignModel<Handler> {

    GuiSign<Handler> create(Handler handler, Map<String, String> replaceMap);

    default GuiSign<Handler> create(Handler handler) {
        return create(handler, new HashMap<>());
    }


}
