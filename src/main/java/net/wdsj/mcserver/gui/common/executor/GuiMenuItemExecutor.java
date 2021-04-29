package net.wdsj.mcserver.gui.common.executor;

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.servercore.interfaces.Executor;

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/4/27 13:12
 */
public interface GuiMenuItemExecutor<Handler, Item> extends Executor<Handler> {

    default void execute(Handler handler) {

    }

    default void execute(GuiMenu<Handler, Item> menu, Handler handler) {

    }
}
