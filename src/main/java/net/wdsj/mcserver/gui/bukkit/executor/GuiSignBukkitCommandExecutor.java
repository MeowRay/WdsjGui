package net.wdsj.mcserver.gui.bukkit.executor;

import com.google.common.collect.Lists;
import mc233.cn.wdsjlib.bukkit.utils.BukkitUtils;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.executor.GuiSignExecutor;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.servercore.common.placeholder.PlaceholderManager;
import net.wdsj.servercore.guava.Collections2Trans;
import net.wdsj.servercore.protocol.ProtocolVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/6 15:05
 */
public class GuiSignBukkitCommandExecutor implements GuiSignExecutor<Player> {

    private final String commands;
    private final boolean con;

    public GuiSignBukkitCommandExecutor(String commands, boolean con) {
        this.commands = commands;
        this.con = con;
    }

    @Override
    public boolean execute(Player player, String[] lines) {
        String replace = commands
                .replace("$line1", lines[0])
                .replace("$line2", lines[1])
                .replace("$line3", lines[2])
                .replace("$line4", lines[3]);
        if (Bukkit.isPrimaryThread() || GuiBukkit.isDefaultAsync()) {
            BukkitUtils.executeCommand(player, PlaceholderManager.replace(player, replace));
        } else {
            Bukkit.getScheduler().runTask(GuiBukkit.getInstance(), () ->  BukkitUtils.executeCommand(player, PlaceholderManager.replace(player, replace)));
        }
        return con;
    }


}
