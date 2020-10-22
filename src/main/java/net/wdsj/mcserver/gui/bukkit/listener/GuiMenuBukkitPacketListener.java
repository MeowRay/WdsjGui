package net.wdsj.mcserver.gui.bukkit.listener;

import com.comphenix.packetwrapper.WrapperPlayClientWindowClick;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketPostAdapter;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import mc233.cn.wdsjlib.bukkit.api.wrapper.SoundWrapper;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.Gui;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.GuiData;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.servercore.common.LocalCooldown;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 22:10
 */
public class GuiMenuBukkitPacketListener extends PacketAdapter {

    private final LocalCooldown<Player> clickCooldown = new LocalCooldown<>(300, TimeUnit.MILLISECONDS);
    private final TIntObjectHashMap<BukkitTask> taskMap = new TIntObjectHashMap<>();

    public GuiMenuBukkitPacketListener(Plugin plugin) {
        super(plugin, PacketType.Play.Client.WINDOW_CLICK, PacketType.Play.Client.CLOSE_WINDOW);
        params().optionAsync();
    }


    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {
            WrapperPlayClientWindowClick windowClick = new WrapperPlayClientWindowClick(event.getPacket());
            //   if (true) {
            //       MenuBukkit.getInstance().getLogger().info(String.format(
            //               "id:%d mode: %s button: %d action: %d slot: %d item:%s", windowClick.getWindowId(), windowClick.getShift().name(), windowClick.getButton(), windowClick.getActionNumber(), windowClick.getSlot(), windowClick.getClickedItem()));
            //       //  return;
            //   }
            final GuiData<Player> guiData = GuiManager.getGuiData(player);
            final Gui<Player> nowOpen = guiData.getNowOpen();

            if (nowOpen != null && guiData.getWindowId() == windowClick.getWindowId()) {
                final InventoryAction inventoryAction = InventoryAction.getInventoryAction(windowClick.getShift().getMode(), windowClick.getButton());
                GuiMenu guiMenu = (GuiMenu<Player, ?>) nowOpen;
                if (inventoryAction.getClickType() == InventoryAction.InventoryClickType.PICKUP && guiMenu.getInventoryType().getSize() <= windowClick.getSlot()) {
                    return;
                }
                BukkitTask task = taskMap.get(player.getEntityId());
                if (task != null) {
                    task.cancel();
                }
                Runnable runnable = () -> {
                    if (!clickCooldown.getPut(player).isCooldown()) {
                        boolean windowOut = false;
                        if (guiMenu.getInventoryType().getSize() <= windowClick.getSlot()) {
                            windowOut = true;
                        }
                        try {
                            if (!windowOut) {
                                if (guiMenu.execute(player, inventoryAction, windowClick.getSlot())) {
                                    if (guiMenu.getGuiMenuProp().getClickSound() != null) {
                                        GuiBukkit.getInstance().playSound(player , guiMenu.getGuiMenuProp().getClickSound() , false);
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            GuiBukkit.getInstance().getLogger().warning(String.format("%s 执行GuiMenu出现出错！", event.getPlayer().getName()));
                            player.sendMessage("§c执行WdsjMenu时出现严重错误，请联系腐竹 QQ1213530749 并且说明详细！");
                            e.printStackTrace();
                            GuiBukkit.getInstance().close(player, windowClick.getWindowId());
                        }
                    }

                    if (inventoryAction == null) {
                        WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                        //  nowOpen.render(player);
                        return;
                    }
                    switch (inventoryAction.getClickType()) {
                        case QUICK_MOVE:
                            player.updateInventory();
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), windowClick.getClickedItem());
                            break;
                        case PICKUP:
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), windowClick.getClickedItem());
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                            guiMenu.renderAdapter().render(player, windowClick.getWindowId(), guiMenu);

                            //WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), windowClick.getClickedItem());
                            break;
                        case PICKUP_ALL:
                            guiMenu.renderAdapter().render(player, windowClick.getWindowId(), guiMenu);
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                            // WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), windowClick.getClickedItem());
                            break;
                        case THROW:
                            guiMenu.renderAdapter().render(player, windowClick.getWindowId(), guiMenu);
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                            break;
                        case CLONE:
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                            break;
                        case SWAP:
                            //  nowOpen.renderAdapter().render(player, windowClick.getWindowId(), nowOpen);
                            player.updateInventory();
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), windowClick.getClickedItem());
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                            break;
                        default:
                            break;
                    }
                    taskMap.remove(player.getEntityId());
                };

                BukkitTask bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), runnable);
                taskMap.put(player.getEntityId(), bukkitTask);
                event.setCancelled(true);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            final GuiData<Player> guiData = GuiManager.getGuiData(player);
            if (guiData.getWindowId() == event.getPacket().getIntegers().read(0)) {
                GuiMenu guiMenu = (GuiMenu) guiData.getNowOpen();
                if (guiData.close()) {
                    if (guiMenu.getGuiMenuProp().getCloseSound() != null) {
                        guiMenu.getGuiMenuProp().getCloseSound().play(player);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }


}
