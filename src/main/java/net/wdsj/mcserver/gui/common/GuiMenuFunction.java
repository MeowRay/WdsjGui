package net.wdsj.mcserver.gui.common;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/1 18:32
 */
public interface GuiMenuFunction<Handler> {

    void closeInventory(Handler handler , int windowId);

    default void closeInventory(Handler handler){
        closeInventory(handler, GuiManager.getGuiData(handler).getWindowId());
    }

}
