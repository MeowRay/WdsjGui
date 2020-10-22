package net.wdsj.mcserver.gui.common.executor;

import net.wdsj.servercore.interfaces.Executor;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:14
 */
public interface GuiItemExecutor<Handler> extends Executor<Handler> {

    @Override
    default void execute(Handler handler) {
        actionExecute(handler);
    }

    boolean actionExecute(Handler handler);

}
