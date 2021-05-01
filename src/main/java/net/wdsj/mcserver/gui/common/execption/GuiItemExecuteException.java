package net.wdsj.mcserver.gui.common.execption;

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/5/1 16:05
 */
public class GuiItemExecuteException extends RuntimeException {

    public GuiItemExecuteException(String message, Throwable e) {
        super(message, e);
    }
    public GuiItemExecuteException(Throwable e) {
        super( e);
    }
}
