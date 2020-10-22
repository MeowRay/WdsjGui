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
public class GuiItemPaneUnderModel<Handler, Item> extends GuiItemPaneLineModel<Handler, Item> {

    public GuiItemPaneUnderModel(GuiMenu<Handler, Item> guiMenu) {
        super(guiMenu, (guiMenu.getInventoryType().getSize() / 9) - 1);
    }

    @Override
    public void init() {
        super.init();
        setItem(getGuiMenu().getInventoryType().getSize() - 1,  footerItem());
    }

    public GuiItemBase<Handler, Item> footerItem() {
        return GuiItemRepository.QUIT_MENU_FENCE;
    }
}
