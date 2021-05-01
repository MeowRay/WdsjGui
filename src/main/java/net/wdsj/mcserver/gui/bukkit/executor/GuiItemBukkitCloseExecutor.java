package net.wdsj.mcserver.gui.bukkit.executor;

import com.comphenix.packetwrapper.WrapperPlayServerCloseWindow;
import net.wdsj.mcserver.gui.common.GuiManager;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemBukkitCloseExecutor extends GuiItemBukkitExecutor {

    @Override
    public boolean exec(Player player) {
        closeInventory(player);
        return true;
    }

    public void closeInventory(Player player) {
        GuiManager.close(player);
        /*
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow();
        int windowId = GuiManager.getGuiData(player).getWindowId();
        if (windowId != -1) {
            closeWindow.setWindowId(windowId);
            closeWindow.sendPacket(player);
        }*/
    }
}
