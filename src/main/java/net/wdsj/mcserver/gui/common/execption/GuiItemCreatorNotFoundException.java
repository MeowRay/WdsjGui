package net.wdsj.mcserver.gui.common.execption;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/6 16:48
 */
public class GuiItemCreatorNotFoundException extends NullPointerException {

    public GuiItemCreatorNotFoundException(String render) {
        super(String.format("Can't found item '%s' creator", render));
    }

}
