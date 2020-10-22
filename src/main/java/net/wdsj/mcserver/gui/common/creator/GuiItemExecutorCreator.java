package net.wdsj.mcserver.gui.common.creator;

import net.wdsj.mcserver.gui.common.executor.GuiItemExecutor;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 23:14
 */
public interface GuiItemExecutorCreator<Handler> {

      GuiItemExecutor<Handler> create(String args, Map<String, Object> argsMap) ;


}
