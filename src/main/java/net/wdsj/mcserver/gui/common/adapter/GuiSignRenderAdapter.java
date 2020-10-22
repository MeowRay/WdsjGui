package net.wdsj.mcserver.gui.common.adapter;

import net.wdsj.mcserver.gui.common.gui.sign.GuiSign;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 16:16
 */
public interface GuiSignRenderAdapter<Handler> {

    int open(Handler handler, GuiSign<Handler> guiSign);

    void close(Handler handler, int windowId);
}
