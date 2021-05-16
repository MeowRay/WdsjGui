package net.wdsj.mcserver.gui.bukkit.adapter;

import com.comphenix.packetwrapper.WrapperPlayServerCloseWindow;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 16:26
 */
public class GuiMenuRenderBukkitPacketAdapter implements GuiMenuRenderAdapter<Player, ItemStack> {

    private Map<Integer, ItemStack> views = null;

    @Override
    public void build(Player player, GuiMenu<Player, ItemStack> guiMenu) {
        //  if (views==null){
        //      reBuild(player, guiMenu);
        //  }
    }

    @Override
    public void reBuild(Player player, GuiMenu<Player, ItemStack> guiMenu) {
        //  views = getView(guiMenu, player);
    }

    @Override
    public int open(Player player, GuiMenu<Player, ItemStack> guiMenu) {
        //  final Map<Integer, ItemStack> view = getView(guiMenu, player);
        if (guiMenu.getGuiMenuProp().getOpenSound() != null) {
            GuiBukkit.getInstance().playSound(player, guiMenu.getGuiMenuProp().getOpenSound(), false);
        }
        return WdsjServerAPI.getNmsService().openWindow(player, guiMenu.getTitle(), guiMenu.getInventoryType(), guiMenu.getInventoryType().getSize(), getView(guiMenu, player));
    }

    @Override
    public void render(Player player, int windowId, GuiMenu<Player, ItemStack> guiMenu) {
        WdsjServerAPI.getNmsService().updateWindow(player, windowId, guiMenu.getInventoryType().getSize(), getView(guiMenu, player));
    }

    @Override
    public void changeTitle(Player player, InventoryType inventoryType, String title) {
        final int windowId = GuiManager.getGuiData(player).getWindowId();
        if (windowId != -1)
            WdsjServerAPI.getNmsService().changeWindowTitle(player, inventoryType, windowId, title);
    }

    @Override
    public void close(Player player, int windowId) {
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow();
        if (windowId != -1) {
            closeWindow.setWindowId(windowId);
            closeWindow.sendPacket(player);
        }
    }


    private Map<Integer, ItemStack> getView(GuiMenu<Player, ItemStack> guiItemMap, Player handler) {
        Map<Integer, ItemStack> map = new HashMap<>();
        for (Map.Entry<Integer, GuiItem<Player, ItemStack>> entry : guiItemMap.getGuiItemMap().entrySet()) {
            if (entry.getValue().getItem().getType() == Material.AIR) continue;
            map.put(entry.getKey(), entry.getValue().getItemView(handler));
        }
/*        for (int i = 0; i < guiItemMap.getInventoryType().getSize(); i++) {
            if (!map.containsKey(i)) {
                map.put(i, new ItemStack(Material.AIR));
            }
        }*/
        return map;
    }

}
