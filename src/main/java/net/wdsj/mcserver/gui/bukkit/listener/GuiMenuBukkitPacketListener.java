package net.wdsj.mcserver.gui.bukkit.listener;

import com.comphenix.packetwrapper.wrapper.WrapperXPlayClientWindowClick;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.Gui;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.GuiData;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.servercore.common.LocalCooldown;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

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
            WrapperXPlayClientWindowClick windowClick = WrapperXPlayClientWindowClick.build(event.getPacket());
            System.out.println("click $windowsId:" + windowClick.getWindowId() + "  Slot:" + windowClick.getSlot());

            //    if (true) {
            //        GuiBukkit.getInstance().getLogger().info(String.format(
            //                "id:%d mode: %s button: %d action: %d slot: %d item:%s", windowClick.getWindowId(), windowClick.getShift().name(), windowClick.getButton(), windowClick.getActionNumber(), windowClick.getSlot(), windowClick.getClickedItem()));
            //        //  return;
            //    }
            final GuiData<Player> guiData = GuiManager.getGuiData(player);
            final Gui<Player> nowOpen = guiData.getNowOpen();

            if (nowOpen != null && guiData.getWindowId() == windowClick.getWindowId()) {
                final InventoryAction inventoryAction = InventoryAction.getInventoryAction(windowClick.getShift().getMode(), windowClick.getButton());
                GuiMenu guiMenu = (GuiMenu<Player, ?>) nowOpen;
                boolean windowOut = guiMenu.getInventoryType().getSize() <= windowClick.getSlot();
                if (windowOut) {
                    Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), () -> {
                        int i = windowClick.getSlot() - guiMenu.getInventoryType().getSize();
                        if (i >= 27) {
                            i = i - 27;
                        } else {
                            i = i + 9;
                        }
                        if (i < 0 || i > 35) return;
                        InventoryClickEvent clickEvent = new InventoryClickEvent(player.getOpenInventory(), InventoryType.SlotType.RESULT, i, ClickType.LEFT, org.bukkit.event.inventory.InventoryAction.PICKUP_ALL);
                        Bukkit.getScheduler().runTask(GuiBukkit.getInstance(), () -> Bukkit.getPluginManager().callEvent(clickEvent));
                        WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);
                        WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), event.getPlayer().getInventory().getItem(i));
                    });
                    return;
                }
                if (inventoryAction.getClickType() == InventoryAction.InventoryClickType.PICKUP) {
                    return;
                }
                BukkitTask task = taskMap.get(player.getEntityId());
                if (task != null) {
                    task.cancel();
                }
                Runnable runnable = () -> {
                    if (!clickCooldown.getPut(player).isCooldown()) {
                        try {
                            if (!windowOut) {
                                if (guiMenu.execute(player, inventoryAction, windowClick.getSlot())) {
                                    if (guiMenu.getGuiMenuProp().getClickSound() != null) {
                                        GuiBukkit.getInstance().playSound(player, guiMenu.getGuiMenuProp().getClickSound(), false);
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
                            Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), () -> {
                                player.updateInventory();
                                guiMenu.renderAdapter().render(player, windowClick.getWindowId(), guiMenu);
                            });
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
               /*             WdsjServerAPI.getNmsService().setWindowSlotItem(player, windowClick.getWindowId(), windowClick.getSlot(), windowClick.getClickedItem());
                            WdsjServerAPI.getNmsService().setWindowSlotItem(player, -1, -1, null);*/
                            Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), () -> {
                                guiMenu.renderAdapter().render(player, windowClick.getWindowId(), guiMenu);
                                player.updateInventory();
                            });
                            break;
                        default:
                            break;
                    }
                    taskMap.remove(player.getEntityId());
                };

                BukkitTask bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), runnable);
                taskMap.put(player.getEntityId(), bukkitTask);

                if (windowOut) {
                    //  event.getPlayer().updateInventory();
                    //   getPlugin().getLogger().info("widnows out  " + windowClick.getSlot());
                   /*
                   InventoryClickEvent inventoryClickEvent = new InventoryClickEvent(player.getOpenInventory(), InventoryType.SlotType.RESULT, windowClick.getSlot(), ClickType.LEFT, org.bukkit.event.inventory.InventoryAction.PICKUP_ALL);
                   Bukkit.getScheduler().runTask(GuiBukkit.getInstance() , () ->{Bukkit.getPluginManager().callEvent(inventoryClickEvent);});*/
                } else {
                    // getPlugin().getLogger().info("widnows in  " + windowClick.getSlot());
                    event.setCancelled(true);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            final GuiData<Player> guiData = GuiManager.getGuiData(player);
            if (guiData.getWindowId() == event.getPacket().getIntegers().read(0)) {
                GuiMenu guiMenu = (GuiMenu) guiData.getNowOpen();
                if (guiData.setClose()) {
                    if (guiMenu.getGuiMenuProp().getCloseSound() != null) {
                        guiMenu.getGuiMenuProp().getCloseSound().play(player);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }


}
