package net.wdsj.mcserver.gui.bukkit.adapter;

import com.comphenix.packetwrapper.*;
import com.comphenix.packetwrapper.common.WrapperCommonPlayServerSignUpdate;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import mc233.cn.wdsjlib.bukkit.utils.BukkitUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.gui.sign.GuiSign;
import net.wdsj.mcserver.gui.common.adapter.GuiSignRenderAdapter;
import net.wdsj.servercore.compatible.XMaterial;
import net.wdsj.servercore.protocol.ProtocolVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/4 19:22
 */
public class GuiSignRenderBukkitPacketAdapter implements GuiSignRenderAdapter<Player> {

    private static final Material material = XMaterial.OAK_WALL_SIGN.parseMaterial();
    private static final int dis = Bukkit.getServer().getViewDistance() * 12;

    @Override
    public int open(Player player, GuiSign<Player> guiSign) {
        Location location = player.getLocation();
        int fY = location.getBlockY() + dis;
        if (fY > 254) {
            fY = location.getBlockY() - dis;
        }

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), fY, location.getBlockZ());


        if (BukkitUtils.getPlayerVersion(player) == ProtocolVersion.v1_16_1.getId()){
            player.sendMessage("§c§l警告！！§n1.16.1客户端存在木牌BUG，提示信息将不会显示");
            player.sendMessage("§c§l警告！！§n建议请使用更高或者低版本再使用木牌输入功能");
            player.sendMessage("§4§l警告！！§n输入的第一排内容为设置内容！");
            player.sendMessage("§4§l警告！！§n输入的第一排内容为设置内容！");
        }


     /*   WrapperPlayServerBlockChange blockChange = new WrapperPlayServerBlockChange();
        blockChange.setLocation(blockPosition);
        blockChange.setBlockData(XMaterial.OAK_WALL_SIGN);
        blockChange.sendPacket(player);*/
        player.sendBlockChange(blockPosition.toLocation(player.getWorld()), material, (byte) 0);


        WrapperCommonPlayServerSignUpdate updateSign = new WrapperCommonPlayServerSignUpdate();
        updateSign.setLines(guiSign.getLines());
        updateSign.setLocation(blockPosition);
        updateSign.sendPacket(player);

        WrapperPlayServerOpenSignEditor openSignEditor = new WrapperPlayServerOpenSignEditor();
        openSignEditor.setLocation(blockPosition);
        openSignEditor.sendPacket(player);

        return blockPosition.hashCode();
    }

    @Override
    public void close(Player player, int windowId) {
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow();
        closeWindow.setWindowId(0);
        closeWindow.sendPacket(player);
    }
}
