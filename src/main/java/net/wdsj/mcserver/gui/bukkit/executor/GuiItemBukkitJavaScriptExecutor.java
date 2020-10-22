package net.wdsj.mcserver.gui.bukkit.executor;

import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemBukkitJavaScriptExecutor extends GuiItemBukkitExecutor {

    private final String script;
    private final Map<String, Object> objectMap;

    public GuiItemBukkitJavaScriptExecutor(String script, Map<String, Object> objectMap) {
        this.script = script;
        this.objectMap = objectMap;
    }

    @Override
    public boolean allowAsyncExecute(Player player) {
        return MenuUtils.scriptExecute(script, objectMap, player, false);
    }
}
