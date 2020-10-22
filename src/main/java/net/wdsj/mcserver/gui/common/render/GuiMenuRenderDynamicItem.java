package net.wdsj.mcserver.gui.common.render;

import lombok.Getter;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 13:21
 */
public class GuiMenuRenderDynamicItem<Handler, Item> extends GuiMenuRenderItem<Handler, Item> {

    @Getter
    private final int interval;
    private final RenderItem<Handler, Item>[] renderItems;

    private Iterator<RenderItem<Handler, Item>> renderItemIterator;


    public GuiMenuRenderDynamicItem(int interval, RenderItem<Handler, Item>... renderItems) {
        super(renderItems[0]);
        if (interval <= 0) {
            this.interval = 0;
        } else {
            this.interval = interval % 2 != 0 ? interval + 1 : interval;
        }
        this.renderItems = renderItems;
        newIterator();
    }

    @Override
    public RenderItem<Handler, Item> getRenderItem(long serverTick) {
        if (renderItemIterator == null || interval == 0) {
            renderItem = renderItems[0];
        } else {
            if (renderItem == null || serverTick % interval == 0) {
                if (!renderItemIterator.hasNext()) {
                    newIterator();
                }
                renderItem = renderItemIterator.next();
            }
        }
        return renderItem;
    }


    public void newIterator() {
        renderItemIterator = Arrays.stream(renderItems).iterator();
    }

}
