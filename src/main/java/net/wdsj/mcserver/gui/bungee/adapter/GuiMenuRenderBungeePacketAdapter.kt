package net.wdsj.mcserver.gui.bungee.adapter;

import de.exceptionflug.protocolize.items.ItemStack;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.servercore.eunm.inventory.InventoryType;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/22 18:57
 */
public class GuiMenuRenderBungeePacketAdapter implements GuiMenuRenderAdapter<ProxiedPlayer, ItemStack> {

    @Override
    public void build(ProxiedPlayer proxiedPlayer, GuiMenu<ProxiedPlayer, ItemStack> guiMenu) {

    }

    @Override
    public void reBuild(ProxiedPlayer proxiedPlayer, GuiMenu<ProxiedPlayer, ItemStack> guiMenu) {

    }

    @Override
    public int open(ProxiedPlayer proxiedPlayer, GuiMenu<ProxiedPlayer, ItemStack> guiMenu) {
        return 0;
    }

    @Override
    public void render(ProxiedPlayer proxiedPlayer, int windowId, GuiMenu<ProxiedPlayer, ItemStack> guiMenu) {

    }

    @Override
    public void changeTitle(ProxiedPlayer player, InventoryType inventoryType, String title) {

    }

    @Override
    public void close(ProxiedPlayer proxiedPlayer, int windowId) {

    }

}
