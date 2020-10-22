package net.wdsj.mcserver.gui.common.creator;

import net.wdsj.mcserver.gui.common.executor.GuiItemExecutor;
import net.wdsj.mcserver.gui.common.executor.GuiSignExecutor;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 23:14
 */
public interface GuiSignExecutorCreator<Handler> {

    GuiSignExecutor<Handler> create(String args);

}
