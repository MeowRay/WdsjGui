package net.wdsj.mcserver.gui.common.component.page;


import com.google.common.collect.Lists;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic;

import java.util.Collection;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/6/1 19:27
 */
public abstract class GuiMenuLoadedPage<T, Handler, Item> extends GuiMenuPage<T, Handler, Item> {

    private final List<T> dataList;

    public GuiMenuLoadedPage(GuiMenuStatic<Handler, Item> guiMenu, List<T> dataList, int preSlot, int nextSlot) {
        super(guiMenu, preSlot, nextSlot);
        this.dataList = dataList;
    }

    public GuiMenuLoadedPage(GuiMenuStatic<Handler, Item> guiMenu, Collection<T> dataList, int preSlot, int nextSlot) {
        this(guiMenu, Lists.newArrayList(dataList), preSlot, nextSlot);
    }

    @Override
    public T getContent(int index) {
        return dataList.size() <= index ? null : dataList.get(index);
    }

    @Override
    public int getContentSize() {
        return dataList.size();
    }

}
