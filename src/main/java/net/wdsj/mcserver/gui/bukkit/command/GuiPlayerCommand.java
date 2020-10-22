package net.wdsj.mcserver.gui.bukkit.command;

import mc233.cn.wdsjlib.bukkit.utils.BukkitUtils;
import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.GuiFactory;
import net.wdsj.mcserver.gui.common.wrapper.GuiMenuConfigWrapper;
import net.wdsj.mcserver.gui.common.wrapper.GuiSignConfigWrapper;
import net.wdsj.servercore.common.SimpleParamParse;
import net.wdsj.servercore.common.command.WdsjCommand;
import net.wdsj.servercore.common.command.anntations.Arg;
import net.wdsj.servercore.common.command.anntations.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/10 21:55
 */

public class GuiPlayerCommand implements WdsjCommand<CommandSender> {

    @SubCommand
    public void openMenu(Player player, String menu) {
        GuiMenuConfigWrapper guiMenu = GuiConfigManager.getGuiMenu(menu);
        if (guiMenu != null) {
            if (guiMenu.requirementCanOpen(player)) {
                guiMenu.open(GuiFactory.GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER, player);
            }
        }

    }

    @SubCommand
    public void openSign(Player player, String sign, @Arg(required = false) String args) {
        GuiSignConfigWrapper guiSign = GuiConfigManager.getGuiSign(sign);
        if (guiSign !=null){
            if (guiSign.requirementCanOpen(player)) {
                if (args == null) args = "";
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : SimpleParamParse.parse(args, ";", "=").getMap().entrySet()) {
                    map.put("$" + entry.getKey(), entry.getValue());
                }
                guiSign.open(player, map);
            }
        }

    }

}
