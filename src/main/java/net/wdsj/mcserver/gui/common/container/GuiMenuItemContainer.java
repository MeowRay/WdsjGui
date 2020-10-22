package net.wdsj.mcserver.gui.common.container;

import com.google.common.collect.Lists;
import net.wdsj.mcserver.gui.common.builder.GuiItemRenderBuilder;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/9 1:09
 */
public class GuiMenuItemContainer<Handler, Item> {

    private final List<Integer> list;
    private final List<GuiItemRenderBuilder<Handler, Item>> guiitemContainer = new ArrayList<>();

    public GuiMenuItemContainer(List<Integer> list) {
        this.list = list;
    }

    public GuiMenuItemContainer(Integer[] list) {
        this(Lists.newArrayList(list));
    }

    public void addItem(GuiItemRenderBuilder<Handler, Item> guiItem) {
        guiitemContainer.add(guiItem);
    }

    public void removeItem(GuiItemRenderBuilder<Handler, Item> guiItem) {
        guiitemContainer.remove(guiItem);
    }

    public void build(GuiMenu<Handler, Item> guiMenu) {
        Iterator<Integer> iterator = list.iterator();
        for (GuiItemRenderBuilder<Handler, Item> builder : guiitemContainer) {
            if (iterator.hasNext()) {
                guiMenu.addRenderItem(builder.build(iterator.next()));
            } else {
                return;
            }
        }
    }

}
