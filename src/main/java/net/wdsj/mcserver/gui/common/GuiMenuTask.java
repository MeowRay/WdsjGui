package net.wdsj.mcserver.gui.common;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/18 17:39
 */
public class GuiMenuTask implements Runnable {

    public long TICK = 0;
    private final long increase;

    public GuiMenuTask(long increase) {
        this.increase = increase;
    }

    @Override
    public void run() {
        TICK += increase;
        GuiManager.refreshAllGuiMenuRender(TICK);
    }

}
