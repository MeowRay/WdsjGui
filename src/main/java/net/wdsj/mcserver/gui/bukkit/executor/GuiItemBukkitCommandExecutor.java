package net.wdsj.mcserver.gui.bukkit.executor;

import mc233.cn.wdsjlib.bukkit.utils.BukkitUtils;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.servercore.protocol.ProtocolVersion;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/21 13:04
 */
public class GuiItemBukkitCommandExecutor extends GuiItemBukkitExecutor {

    //private static final boolean async = WdsjServerAPI.getNmsService().getProtocolVersion().getId() <= ProtocolVersion.v1_12_2.getId();

    private final String[] commands;

    public GuiItemBukkitCommandExecutor(boolean async,String... commands) {
        super(async);
        this.commands = commands;
    }

    public GuiItemBukkitCommandExecutor(String... commands) {
        super(true);
        this.commands = commands;
    }


    @Override
    public boolean allowAsyncExecute(Player player) {
            BukkitUtils.executeCommand(player, commands);
        return true;
    }

}
