package net.wdsj.mcserver.gui.common.model.template;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.model.GuiItemBaseModel;
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository;

import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/3 14:29
 */
public class GuiItemPaneLineModel<Handler, Item> extends GuiItemBaseModel<Handler, Item> {

    private final int line;

    public GuiItemPaneLineModel(GuiMenu<Handler, Item> guiMenu, int line) {
        super(guiMenu);
        this.line = line;
        init();
    }

    public void init(){

        int e = (line * 9) ; // -1
        int s = e - 8;

        if (s >= 0) {
            List<Integer> freeSlot = getGuiMenu().getFreeSlot(s, e);
            for (Integer integer : freeSlot) {
                setItem(integer, paneItem());
            }
        }
    }

    public GuiItemBase<Handler, Item> paneItem() {
        return GuiItemRepository.PLACEHOLDER_BLACK_GLASS_PANE;
    }

}
