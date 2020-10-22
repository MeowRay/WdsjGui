package net.wdsj.mcserver.gui.common.executor;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/21 13:06
 */
public interface GuiItemCanAsyncExecutor<Handler> extends GuiItemExecutor<Handler>{

    boolean isAsync();

    <T extends GuiItemCanAsyncExecutor<Handler>> T setAsync(boolean async);


    @Override
    default boolean actionExecute(Handler handler) {
        if (isAsync()) {
           return async(handler);
        } else {
           return sync(handler);
        }
    }

    boolean allowAsyncExecute(Handler handler);

    boolean async(Handler handler);

    boolean sync(Handler handler);

}
