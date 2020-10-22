package net.wdsj.mcserver.gui.common;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/4 14:55
 */
public interface Gui<Handler> {

    int open(Handler handler);

    void close(Handler handler ,int id);

}
