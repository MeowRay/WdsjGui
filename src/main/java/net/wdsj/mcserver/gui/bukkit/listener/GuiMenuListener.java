package net.wdsj.mcserver.gui.bukkit.listener;

import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.wrapper.CanOpenItem;
import net.wdsj.servercore.common.SimpleParamParse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/11/18 12:55
 */
@Deprecated
public class GuiMenuListener implements Listener {

/*    @EventHandler(ignoreCancelled = true)
    public void on(PlayerCommandPreprocessEvent event) {
        String[] s = event.getMessage().split(" ", 2);
        CanOpenItem commandGuiWrapper = GuiConfigManager.getCommandGuiWrapper(s[0].substring(1).toLowerCase());
        if (commandGuiWrapper != null) {
            event.setCancelled(true);
            if (commandGuiWrapper.requirementCanOpen(event.getPlayer())) {
                Map<String, String> map = new HashMap<>();
                if (s.length>1)
                    for (Map.Entry<String, String> entry : SimpleParamParse.parse(s[1], ";", "=").getMap().entrySet()) {
                        map.put("$" + entry.getKey(), entry.getValue());
                }
                commandGuiWrapper.open(event.getPlayer() , map);
            }
        }
    }*/


}
