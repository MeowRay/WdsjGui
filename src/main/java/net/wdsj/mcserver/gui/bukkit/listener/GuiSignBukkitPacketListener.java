package net.wdsj.mcserver.gui.bukkit.listener;

import com.comphenix.packetwrapper.WrapperPlayClientUpdateSign;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.wdsj.mcserver.gui.common.GuiData;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.gui.sign.GuiSign;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/4 15:20
 */
public class GuiSignBukkitPacketListener extends PacketAdapter {

    public GuiSignBukkitPacketListener(Plugin plugin) {
        super(plugin, PacketType.Play.Client.UPDATE_SIGN);
        params().optionAsync();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.isCancelled()) return;
        WrapperPlayClientUpdateSign playClientUpdateSign = new WrapperPlayClientUpdateSign(event.getPacket());
        GuiData<Player> guiData = GuiManager.getGuiData(event.getPlayer());

        if (guiData.getWindowId() == playClientUpdateSign.getLocation().hashCode()) {
            GuiSign<Player> guiSign = (GuiSign<Player>) guiData.getNowOpen();
            guiSign.execute(event.getPlayer(), playClientUpdateSign.getLines());
            guiData.setClose();
            event.setCancelled(true);
            try {
                Location location = playClientUpdateSign.getLocation().toLocation(event.getPlayer().getWorld());
                Block block = location.getBlock();
                event.getPlayer().sendBlockChange(playClientUpdateSign.getLocation().toLocation(location.getWorld()), block.getType(), (byte) 0);
            } catch (Exception e) {

            }
        }


    }
}
