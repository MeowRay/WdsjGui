package net.wdsj.mcserver.gui.bukkit.executor;

import mc233.cn.wdsjlib.bukkit.utils.extensions.PlayerExtensionKt;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemLanguageMessageExecutor extends GuiItemMessageExecutor {

    public GuiItemLanguageMessageExecutor(String key) {
        super(key);
    }

    @Override
    public boolean exec(Player player) {
        PlayerExtensionKt.sendLangMessage(player, message);
        return true;
    }
}
