package net.wdsj.mcserver.gui.common.model.template;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.model.GuiItemBaseModel;
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/1 2:13
 */
public class GuiItemPaneFrameModel<Handler, Item> extends GuiItemBaseModel<Handler, Item> {

    private static final int[] ALL = new int[]{9, 17, 18, 26, 27, 35, 36, 44, 45, 53};

    public GuiItemPaneFrameModel(GuiMenu<Handler, Item> guiItem) {
        super(guiItem);
        init();
    }


    public void init() {
        setItem(0, headerItem());
        for (Integer integer : getGuiMenu().getFreeSlot(1, 8)) {
            setItem(integer, paneItem());
        }
        int size = getSize();
        for (int i : ALL) {
            if (size - 1 == i) {
                setItem(i, footerItem());
                for (Integer integer : getGuiMenu().getFreeSlot(size - 9, size - 2)) {
                    setItem(integer, paneItem());
                }
                return;
            }
            setItem(i, paneItem());
        }
    }


    public GuiItemBase<Handler, Item> headerItem() {
        return paneItem();
    }


    public GuiItemBase<Handler, Item> footerItem() {
        return GuiItemRepository.QUIT_MENU_FENCE;
    }

    public GuiItemBase<Handler, Item> paneItem() {
        return GuiItemRepository.PLACEHOLDER_BLACK_GLASS_PANE;
    }


}
