package net.wdsj.mcserver.gui.bungee.adapter

import de.exceptionflug.protocolize.items.ItemStack
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu
import net.wdsj.servercore.eunm.inventory.InventoryType

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/22 18:57
 */
class GuiMenuRenderBungeePacketAdapter : GuiMenuRenderAdapter<ProxiedPlayer, ItemStack> {

    override fun build(proxiedPlayer: ProxiedPlayer, guiMenu: GuiMenu<ProxiedPlayer, ItemStack>) {}
    override fun reBuild(proxiedPlayer: ProxiedPlayer, guiMenu: GuiMenu<ProxiedPlayer, ItemStack>) {}
    override fun open(proxiedPlayer: ProxiedPlayer, guiMenu: GuiMenu<ProxiedPlayer, ItemStack>): Int {
        return 0
    }

    override fun render(proxiedPlayer: ProxiedPlayer, windowId: Int, guiMenu: GuiMenu<ProxiedPlayer, ItemStack>) {}
    override fun changeTitle(player: ProxiedPlayer, inventoryType: InventoryType, title: String) {}
    override fun close(proxiedPlayer: ProxiedPlayer, windowId: Int) {}

}