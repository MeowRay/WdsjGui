package net.wdsj.mcserver.gui.bukkit.executor;

import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemBukkitJavaScriptExecutor extends GuiItemBukkitExecutor {

    private final String script;

    public GuiItemBukkitJavaScriptExecutor(String script) {
        this.script = script;
    }

    @Override
    public boolean allowAsyncExecute(Player player) {
        return MenuUtils.scriptExecute(script, player, false);
    }
}
