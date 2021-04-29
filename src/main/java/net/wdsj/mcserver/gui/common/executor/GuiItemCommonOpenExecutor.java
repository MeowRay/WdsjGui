package net.wdsj.mcserver.gui.common.executor;

import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.wrapper.GuiMenuConfigWrapper;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/3 16:36
 */
public class GuiItemCommonOpenExecutor<Handler> implements GuiItemExecutor<Handler> {

    private final String menu;
    private final GuiMenuRenderAdapter<Handler, ?> adapter;

    public GuiItemCommonOpenExecutor(String menu, GuiMenuRenderAdapter<Handler, ?> adapter) {
        this.menu = menu;
        this.adapter = adapter;
    }

    @Override
    public boolean actionExecute(Handler handler) {
        GuiMenuConfigWrapper guiMenu = GuiConfigManager.getGuiMenu(menu, true);
        if (guiMenu != null) {
            guiMenu.open(adapter, handler);
        }
        return true;
    }
}
