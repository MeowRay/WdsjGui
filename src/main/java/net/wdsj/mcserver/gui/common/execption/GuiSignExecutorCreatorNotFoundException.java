package net.wdsj.mcserver.gui.common.execption;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/6 16:48
 */
public class GuiSignExecutorCreatorNotFoundException extends NullPointerException {

    public GuiSignExecutorCreatorNotFoundException() {
        super("找不到Sign creator!");
    }

}
