package net.wdsj.mcserver.gui.common.builder;

import com.google.common.collect.Lists;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderDynamicItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;
import net.wdsj.servercore.common.IteratorCycle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 21:47
 */
public class GuiItemRenderBuilder<Handler, Item> {

    private GuiItem<Handler, Item> guiItem;
    private int interval;
    private List<GuiItem<Handler, Item>> guiItems;

    public GuiItemRenderBuilder(GuiItem<Handler, Item> guiItem) {
        this.guiItem = guiItem;
    }

    public GuiItemRenderBuilder(int interval, List<GuiItem<Handler, Item>> guiItems) {
        this.interval = interval;
        this.guiItems = guiItems;
    }

    public GuiMenuRenderItem<Handler, Item> build(Integer... slot) {
        if (guiItem != null) {
            return new GuiMenuRenderItem<>(new RenderItem<>(slot[0], guiItem));
        }
        IteratorCycle<Integer> cycle = new IteratorCycle<>(Lists.newArrayList(slot));
        List<RenderItem<Handler, Item>> list = new ArrayList<>();
        for (GuiItem<Handler, Item> item : guiItems) {
            list.add(new RenderItem<>(cycle.getNext(), item));
        }
        return new GuiMenuRenderDynamicItem<>(interval, list.toArray(new RenderItem[0]));
    }

}
