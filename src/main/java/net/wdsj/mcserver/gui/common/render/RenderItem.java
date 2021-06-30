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
public class RenderItem<Handler, Item> {

    public RenderItem(int slot, GuiItem<Handler, Item> guiItem) {
        this.slot = slot;
        this.guiItem = guiItem;
    }

    private final int slot;
    private final GuiItem<Handler, Item> guiItem;

}
