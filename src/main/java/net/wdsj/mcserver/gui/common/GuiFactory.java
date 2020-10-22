package net.wdsj.mcserver.gui.common;

import net.wdsj.mcserver.gui.bukkit.adapter.GuiMenuRenderBukkitEntityAdapter;
import net.wdsj.mcserver.gui.bukkit.adapter.GuiMenuRenderBukkitPacketAdapter;
import net.wdsj.mcserver.gui.bukkit.adapter.GuiSignRenderBukkitPacketAdapter;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.adapter.GuiSignRenderAdapter;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/19 18:56
 */
public class GuiFactory {


    public static final GuiMenuRenderAdapter<Player, ItemStack> GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER = new GuiMenuRenderBukkitPacketAdapter();
    public static final GuiSignRenderAdapter<Player> GUISIGN_RENDER_BUKKIT_PACKET_ADAPTER = new GuiSignRenderBukkitPacketAdapter();

    public static GuiMenu<Player, ItemStack> createBukkitPacketGuiMenu(Player player, InventoryType type, String title) {
        return new GuiMenu<Player, ItemStack>(player, type, title) {

            @Override
            public GuiMenuRenderAdapter<Player, ItemStack> renderAdapter() {
                return GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER;
            }
        };
    }

    public static GuiMenuStatic<Player, ItemStack> createBukkitPacketGuiMenuStatic(Player player, InventoryType type, String title) {
        return new GuiMenuStatic<Player, ItemStack>(player, type, title) {
            @Override
            public GuiMenuRenderAdapter<Player, ItemStack> renderAdapter() {
                return GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER;
            }
        };
    }

    public static GuiMenuRenderBukkitEntityAdapter getNewEntityInventoryAdapter(org.bukkit.event.inventory.InventoryType inventoryType) {
        return new GuiMenuRenderBukkitEntityAdapter(Bukkit.createInventory(null, inventoryType));
    }


    public static GuiSignRenderAdapter<?> getDefaultSignRenderAdapter() {
        if (WdsjServerAPI.getServerType().isBukkit()) {
            return GUISIGN_RENDER_BUKKIT_PACKET_ADAPTER;
        }
        return null;
    }


    public static GuiMenuRenderAdapter<?, ?> getDefaultMenuRenderAdapter() {
        if (WdsjServerAPI.getServerType().isBukkit()) {
            return GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER;
        }
        return null;
    }


}
