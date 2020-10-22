package net.wdsj.mcserver.gui.common.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.wdsj.mcserver.gui.common.item.GuiItem;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 13:30
 */

@Getter
@AllArgsConstructor
public class RenderItem<Handler, Item> {

    private final int slot;
    private final GuiItem<Handler, Item> guiItem;

}
