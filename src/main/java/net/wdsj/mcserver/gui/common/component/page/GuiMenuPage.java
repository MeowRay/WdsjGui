package net.wdsj.mcserver.gui.common.component.page;


import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.servercore.interfaces.Executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2019/9/25 13:03
 */
public abstract class GuiMenuPage<T, Handler, Item> {

    @Getter
    private final GuiMenuStatic<Handler, Item> guiMenu;

    @Setter
    private int preSlot;

    @Setter
    private int nextSlot;

    @Getter
    private int openPage;

    private final String originTitle;

    public GuiMenuPage(GuiMenuStatic<Handler, Item> guiMenu, int preSlot, int nextSlot) {
        this.guiMenu = guiMenu;
        this.preSlot = preSlot;
        this.nextSlot = nextSlot;
        this.originTitle = guiMenu.getTitle();
    }

    public abstract T getContent(int index);

    public abstract int getContentSize();

    public abstract GuiItemBase<Handler, Item> getItemRender(int slot, T t);

    public GuiItemBase<Handler, Item> getPreItem() {
        return GuiItemRepository.PAGE_PRE.clone();
    }

    public GuiItemBase<Handler, Item> getNextItem() {
        return GuiItemRepository.PAGE_NEXT.clone();
    }

    public String getPageTitle() {
        return originTitle;
    }

    public GuiItemBase<Handler, Item> getEmptyItem() {
        return null;
    }

    public abstract ImmutableList<Integer> getFreeSlots();

    public void loadPage(int page) {
        ImmutableList<Integer> freeSlots = getFreeSlots();
        int size = freeSlots.size();
        int pageCount = size == 0 ? 0 : (int) Math.ceil((double) getContentSize() / (double) size);
        if (pageCount == 0) pageCount++;
        if (page > pageCount) {
            page = pageCount;
        }
        if (page == 0) page = 1;
        openPage = page;

        int endIndex = (page * size);
        if (endIndex >= getContentSize()) {
            endIndex = getContentSize();
        }

        int startIndex = (page * size) - size;

        List<T> renderDataList = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            T content = getContent(i);
            if (content == null) {
                break;
            }
            renderDataList.add(content);
        }

        if (renderDataList.isEmpty()) {
            GuiItemBase<Handler, Item> emptyItem = getEmptyItem();
            if (emptyItem != null && !freeSlots.isEmpty()) {
                guiMenu.setItem(freeSlots.get(0), emptyItem);
            }
        } else {
            Iterator<T> renderDataIterator = renderDataList.iterator();
            for (Integer freeSlot : freeSlots) {
                if (renderDataIterator.hasNext()) {
                    guiMenu.setItem(freeSlot, getItemRender(freeSlot, renderDataIterator.next()));
                } else {
                    guiMenu.delItem(freeSlot);
                }
            }
            if (openPage < pageCount) {
                guiMenu.setItem(nextSlot, getNextItem().removeAllExecutor().addActionExecutor(InventoryAction.LEFT, NextClick()));
            } else {
                guiMenu.delItem(nextSlot);
            }
            if (openPage > 1) {
                guiMenu.setItem(preSlot, getPreItem().removeAllExecutor().addActionExecutor(InventoryAction.LEFT, PreClick()));
            } else {
                guiMenu.delItem(preSlot);
            }
        }
        int finalPageCount = pageCount;

        GuiManager.refreshGuiMenuRender(guiMenu.getOwner(), guiMenu);

        //TODO sleep 200ms?
//        guiMenu.getRenderAdapter().changeTitle(guiMenu.getOwner(), guiMenu.getInventoryType(), getPageTitle().replace("{nowPage}", String.valueOf(openPage)).replace("{maxPage}", String.valueOf(finalPageCount)));

    }

    public Executor<Handler> PreClick() {
        return o -> loadPage(openPage - 1);
    }

    public Executor<Handler> NextClick() {
        return o -> loadPage(openPage + 1);
    }


}
