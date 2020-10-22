package net.wdsj.mcserver.gui.common.model;

import com.google.common.collect.Lists;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;

import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/1 2:18
 */
public abstract class GuiItemBaseModel<Handler, Item> implements GuiItemModel<Handler, Item> {

    public TIntObjectHashMap<GuiMenuRenderItem<Handler, Item>> map = new TIntObjectHashMap<>();

    @Getter
    private final GuiMenu<Handler, Item> guiMenu;

    public GuiItemBaseModel(GuiMenu<Handler, Item> guiItem) {
        this.guiMenu = guiItem;
    }

    public int getSize() {
        return guiMenu.getInventoryType().getSize();
    }

    public void setItem(int slot, GuiItem<Handler, Item> guiItem) {
        if (guiItem== null) {
            delItem(slot);
            return;
        }
        setItem(slot, new GuiMenuRenderItem<>(new RenderItem<>(slot, guiItem)));
    }

    public void setItem(int slot, GuiMenuRenderItem<Handler, Item> guiItem) {
        map.put(slot, guiItem);
    }

    public void delItem(int slot) {
        map.remove(slot);
    }


    @Override
    public List<GuiMenuRenderItem<Handler, Item>> getRenderItems(GuiMenu<Handler, Item> guiMenu) {
        return Lists.newArrayList(map.valueCollection());
    }
}
