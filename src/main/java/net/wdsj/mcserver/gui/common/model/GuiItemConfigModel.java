package net.wdsj.mcserver.gui.common.model;

import com.google.common.base.Preconditions;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.config.GuiItemRenderConfig;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderDynamicItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;
import net.wdsj.servercore.common.IteratorCycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:58
 */
public class GuiItemConfigModel<Handler, Item> implements GuiItemModel<Handler, Item> {

    private final Collection<GuiItemRenderConfig> renderConfig;

    public GuiItemConfigModel(Collection<GuiItemRenderConfig> config) {
        Preconditions.checkNotNull(config);
        this.renderConfig = config;
    }

    @Override
    public List<GuiMenuRenderItem<Handler, Item>> getRenderItems(GuiMenu<Handler, Item> guiMenu) {
        List<GuiMenuRenderItem<Handler, Item>> renderItems = new ArrayList<>();
        for (GuiItemRenderConfig config : renderConfig) {
            if (!config.getSlot().isEmpty()) {
                IteratorCycle<Integer> cycle = new IteratorCycle<>(config.getSlot());
                List<GuiItem<?, ?>> guiItem = GuiConfigManager.getGuiItemRender(null,config);
                List<RenderItem<Handler, Item>> collect = guiItem.stream().map(guiItem1 -> new RenderItem<>(cycle.getNext(), (GuiItem<Handler, Item>) guiItem1)).collect(Collectors.toList());
                renderItems.add(new GuiMenuRenderDynamicItem<>(config.getUpdate(), collect.toArray(new RenderItem[0])));
            }
        }
        return renderItems;
    }

}
