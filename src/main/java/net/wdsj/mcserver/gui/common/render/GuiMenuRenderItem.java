package net.wdsj.mcserver.gui.common.render;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 13:21
 */
public class GuiMenuRenderItem<Handler, Item> {


    protected RenderItem<Handler, Item> renderItem;

    public GuiMenuRenderItem(RenderItem<Handler, Item> renderItem) {
        this.renderItem = renderItem;
    }

    public RenderItem<Handler, Item> getRenderItem() {
        return renderItem;
    }

    public RenderItem<Handler, Item> getRenderItem(long serverTick) {
        return getRenderItem();
    }





}
