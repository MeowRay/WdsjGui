package net.wdsj.mcserver.gui.bukkit.executor;

import mc233.cn.wdsjlib.bukkit.WdsjLib;
import mc233.cn.wdsjlib.bukkit.api.wrapper.TitleWrapper;
import net.wdsj.servercore.common.SimpleParamParse;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemBukkitTitleExecutor extends GuiItemBukkitExecutor {

    private final TitleWrapper titleWrapper;

    public GuiItemBukkitTitleExecutor(String text) {
        SimpleParamParse parse = SimpleParamParse.parse(text, ";", "=");
        titleWrapper = new TitleWrapper("");
        parse.apply(titleWrapper);
    }

    public GuiItemBukkitTitleExecutor(TitleWrapper titleWrapper) {
        this.titleWrapper = titleWrapper;
    }

    @Override
    public boolean exec(Player player) {
        WdsjLib.getInstance().getTitleAPI().sendTitle(player, titleWrapper);
        return true;
    }
}
