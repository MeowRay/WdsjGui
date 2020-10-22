package net.wdsj.mcserver.gui.common.executor;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/4 19:41
 */
public interface GuiSignExecutor<Handler> {


    boolean execute(Handler handler, String[] lines);

}
