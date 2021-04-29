package net.wdsj.mcserver.gui.common.gui.menu;

import gnu.trove.map.hash.THashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.wdsj.mcserver.gui.common.Gui;
import net.wdsj.mcserver.gui.common.GuiData;
import net.wdsj.mcserver.gui.common.GuiFactory;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.gui.GuiMenuProp;
import net.wdsj.mcserver.gui.common.model.GuiItemModel;
import net.wdsj.mcserver.gui.common.model.GuiMenuModel;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;
import net.wdsj.servercore.utils.ThreadUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/11 15:18
 */
@Getter
@Accessors(chain = true)
public class GuiMenu<Handler, Item> implements Gui<Handler> {

    @Setter
    private int minInterval = 4;

    protected Map<Integer, GuiItem<Handler, Item>> guiItemMap;
    protected List<GuiMenuRenderItem<Handler, Item>> guiMenuRenderItems;

    protected final int windowId = GuiManager.applicationWindowId();

    @Setter
    private GuiMenuProp guiMenuProp = new GuiMenuProp();

    protected final InventoryType inventoryType;
    @Setter
    protected String title;
    protected final Handler owner;


    private final GuiMenuRenderAdapter<Handler, Item> renderAdapter = (GuiMenuRenderAdapter<Handler, Item>) GuiFactory.getDefaultMenuRenderAdapter();

    public GuiMenu(Handler owner, InventoryType inventoryType, String title) {
        this.guiItemMap = new THashMap<>(inventoryType.getSize());
        this.guiMenuRenderItems = new ArrayList<>(inventoryType.getSize());
        this.inventoryType = inventoryType;
        this.title = title;
        this.owner = owner;
    }

    public void addRenderItem(GuiMenuRenderItem<Handler, Item> guiMenuRenderItem) {
        guiMenuRenderItems.add(guiMenuRenderItem);
    }

    public void delRenderItem(GuiMenuRenderItem<Handler, Item> guiMenuRenderItem) {
        guiMenuRenderItems.remove(guiMenuRenderItem);
    }

    public void loadGuiMenuModel(GuiItemModel<Handler, Item> menuModel) {
        for (GuiMenuRenderItem<Handler, Item> renderItem : menuModel.getRenderItems(this)) {
            addRenderItem(renderItem);
        }
    }

    public void loadGuiMenuModel(GuiMenuModel<Handler, Item> menuModel) {
        for (GuiMenuRenderItem<Handler, Item> renderItem : menuModel.getRenderItems(null)) {
            addRenderItem(renderItem);
        }
    }

    public boolean execute(Handler handler, InventoryAction inventoryAction, int slot) {
        GuiItem<Handler, Item> handlerItemGuiItem = guiItemMap.get(slot);
        if (handlerItemGuiItem != null) {
            handlerItemGuiItem.execute(this, handler, inventoryAction);
            return true;
        }
        return false;
    }


    public boolean refreshRenderContent(long serverTick) {
        return refreshRenderContent(serverTick, false);
    }

    /**
     * @return 返回GuiItemMap是否更改
     */
    public boolean refreshRenderContent(long serverTick, boolean force) {
        if (force || (minInterval != -1 && serverTick % minInterval == 0) || serverTick == 0) {
            Map<Integer, GuiItem<Handler, Item>> newItemMap = new THashMap<>(inventoryType.getSize());
            for (GuiMenuRenderItem<Handler, Item> guiMenuRenderItem : new ArrayList<>(guiMenuRenderItems)) {
                RenderItem<Handler, Item> renderItem = guiMenuRenderItem.getRenderItem(serverTick);
                GuiItem<Handler, Item> guiItem = renderItem.getGuiItem();
                int slot = renderItem.getSlot();
                newItemMap.put(slot, guiItem);
            }
            if (!force && newItemMap.equals(guiItemMap)) {
                return false;
            } else {
                this.guiItemMap = newItemMap;
            }
            return true;
        }
        return false;
    }


    public List<Integer> getFreeSlot() {
        return getFreeSlot(0, inventoryType.getSize() - 1);
    }

    public List<Integer> getFreeSlot(int start, int end) {
        if (end >= inventoryType.getSize()) end = inventoryType.getSize() - 1;
        List<Integer> list = new ArrayList<>();
        final Map<GuiMenuRenderItem<Handler, Item>, Integer> guiMenuRenderItemIntegerMap = new THashMap<>();
        for (GuiMenuRenderItem<Handler, Item> guiMenuRenderItem : guiMenuRenderItems) {
            guiMenuRenderItemIntegerMap.put(guiMenuRenderItem, guiMenuRenderItem.getRenderItem().getSlot());
        }
        for (int i = start; i <= end; i++) {
            if (!guiMenuRenderItemIntegerMap.containsValue(i)) {
                list.add(i);
            }
        }
        return list;
    }

    public List<Integer> getFreeSlot(int minX, int minY, int maxX, int maxY) {
        List<Integer> list = new ArrayList<>();
        maxY = Math.max(maxY, minY);
        minX--;
        maxX--;

        for (int i = minY; i <= maxY; i++) {
            int minx = i == 1 ? minX : minX + 9 * (i - 1);
            int maxx = i == 1 ? maxX : maxX + 9 * (i - 1);
            list.addAll(getFreeSlot(minx, maxx));
        }
        return list;
    }

    public void changeTitle(String title) {
        this.title = title;
        for (GuiData<Handler> menuActiveHandler : GuiManager.getGuiActiveDataHandlers(this)) {
            renderAdapter().changeTitle(menuActiveHandler.getHandler(), inventoryType, title);
        }
    }

    public void changeTitleMessage(String title) {
        for (GuiData<Handler> menuActiveHandler : GuiManager.getGuiActiveDataHandlers(this)) {
            renderAdapter().changeTitle(menuActiveHandler.getHandler(), inventoryType, title);
        }
        ThreadUtils.delayExecute(new TimerTask() {
            @Override
            public void run() {
                for (GuiData<Handler> menuActiveHandler : GuiManager.getGuiActiveDataHandlers(GuiMenu.this)) {
                    renderAdapter().changeTitle(menuActiveHandler.getHandler(), inventoryType, GuiMenu.this.title);
                }
            }
        } , 2, TimeUnit.SECONDS);
    }

    public GuiMenuRenderAdapter<Handler, Item> renderAdapter() {
        return renderAdapter;
    }

    @Override
    public int open(Handler handler) {
        refreshRenderContent(0);
        renderAdapter().build(handler, this);
        return renderAdapter().open(handler, this);
    }

    @Override
    public void close(Handler handler, int id) {
        renderAdapter().close(handler, id);
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public Handler getOwner() {
        return owner;
    }
}
