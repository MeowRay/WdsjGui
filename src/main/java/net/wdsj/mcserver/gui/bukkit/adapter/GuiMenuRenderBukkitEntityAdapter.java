package net.wdsj.mcserver.gui.bukkit.adapter;

import com.google.common.collect.Maps;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 16:26
 */
public class GuiMenuRenderBukkitEntityAdapter implements GuiMenuRenderAdapter<Player, ItemStack> {


    private final Inventory inventory;
    private boolean first = true;

    public GuiMenuRenderBukkitEntityAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void build(Player player, GuiMenu<Player, ItemStack> guiMenu) {
        if (first) {
            reBuild(player, guiMenu);
            first = false;
        }
    }

    @Override
    public void reBuild(Player player, GuiMenu<Player, ItemStack> guiMenu) {
        final Map<Integer, ItemStack> view = getView(guiMenu, player);
        for (Map.Entry<Integer, ItemStack> integerItemStackEntry : view.entrySet()) {
            inventory.setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
        }
    }

    @Override
    public int open(Player player, GuiMenu<Player, ItemStack> guiMenu) {
        if (player != null) {
            if (player.getOpenInventory().getTopInventory() != inventory) {
                player.openInventory(inventory);
            }
            return WdsjServerAPI.getNmsService().getPlayerOpenWindowId(player);
        }
        return -1;
    }

    @Override
    public void render(Player player, int windowId, GuiMenu<Player, ItemStack> guiMenu) {
        player.updateInventory();
    }


    @Override
    public void changeTitle(Player player, InventoryType inventoryType, String title) {
        final int windowId = GuiManager.getGuiData(player).getWindowId();
        if (windowId != -1)
            WdsjServerAPI.getNmsService().changeWindowTitle(player, inventoryType, windowId, title);
    }

    @Override
    public void close(Player player, int windowId) {
        player.closeInventory();
    }


    private Map<Integer, ItemStack> getView(GuiMenu<Player, ItemStack> guiItemMap, Player handler) {
        return Maps.transformValues(guiItemMap.getGuiItemMap(), playerItemStackGuiItem -> playerItemStackGuiItem.getItemView(handler));
    }

}
