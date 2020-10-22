package net.wdsj.mcserver.gui.common.render;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.wdsj.servercore.common.IteratorCycle;

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

    private IteratorCycle<RenderItem<Handler, Item>> renderItemIteratorCycle;


    @SafeVarargs
    public GuiMenuRenderDynamicItem(int interval, RenderItem<Handler, Item>... renderItems) {
        super(renderItems[0]);
        if (interval <= 0) {
            this.interval = 0;
        } else {
            this.interval = interval % 2 != 0 ? interval + 1 : interval;
        }

        this.renderItems = renderItems;
        if (renderItems.length > 1) {
            this.renderItemIteratorCycle = new IteratorCycle<>(Lists.newArrayList(renderItems));
        }

    }

    @Override
    public RenderItem<Handler, Item> getRenderItem(long serverTick) {
        if (renderItemIteratorCycle == null || interval == 0) {
            renderItem = renderItems[0];
        } else {
            if (renderItem == null || serverTick % interval == 0) {
                return renderItemIteratorCycle.getNext();
            }
        }
        return renderItem;
    }


    public void newIterator() {
        renderItemIteratorCycle.newIterator();
    }

}
