package net.wdsj.mcserver.gui.common.extra.viewexector;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/22 2:03
 */
public interface GuiItemViewExecutorCaller<K> {

    <Handler> GuiItemViewExecutor<Handler> get(K key);

    Class<K> getKeyClass();

}
