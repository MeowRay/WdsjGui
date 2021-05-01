package net.wdsj.mcserver.gui.bukkit.executor;

import net.wdsj.mcserver.gui.common.GuiManager;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemMessageExecutor extends GuiItemBukkitExecutor {

    protected final String message;

    public GuiItemMessageExecutor(String message){

        this.message = message;
    }

    @Override
    public boolean exec(Player player) {
        player.sendMessage(message);
        return true;
    }
}
