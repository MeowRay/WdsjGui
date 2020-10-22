package net.wdsj.mcserver.gui.common.executor;

import net.wdsj.servercore.interfaces.Executor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:07
 */
public class GuiItemExecutorCollection<Handler> implements Executor<Handler> {

    List<GuiItemExecutor<Handler>> executorList = new ArrayList<>();

    public GuiItemExecutorCollection() {

    }

    public void addExecutor(GuiItemExecutor<Handler> executor) {
        executorList.add(executor);
    }

    @Override
    public void execute(Handler handler) {
        new Thread(() -> {
            for (GuiItemExecutor<Handler> executor : executorList) {
                if (! executor.actionExecute(handler)) {
                    return;
                }
            }
        }).start();
    }
}
