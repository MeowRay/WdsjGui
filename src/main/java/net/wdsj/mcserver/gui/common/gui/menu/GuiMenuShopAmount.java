package net.wdsj.mcserver.gui.common.gui.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import mc233.cn.wdsjlib.bukkit.gui.CustomGuiManager;
import mc233.cn.wdsjlib.global.api.eco.EcoAPI;
import mc233.cn.wdsjlib.global.api.eco.EcoHandler;
import mc233.cn.wdsjlib.global.api.eco.data.EcoData;
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.gui.sign.GuiSign;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.item.GuiItemCommon;
import net.wdsj.servercore.compatible.XMaterial;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import net.wdsj.servercore.utils.MinecraftUtils;
import net.wdsj.servercore.utils.ThreadUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/2 15:18
 */
public abstract class GuiMenuShopAmount<T extends GuiMenuShopAmount.Commodity<?>, Handler, Item> extends GuiMenuShop<T, Handler, Item> {

    public GuiMenuShopAmount(Handler owner, Collection<T> items, int line, String title) {
        super(owner, items, line, title);
    }

    @Override
    public GuiItemBase<Handler, Item> buildCommodity(ItemCommonBuilder builder, T commodity) {
        return new GuiItemCommon<Handler, Item>(builder).addActionExecutor(InventoryAction.LEFT, handler -> {
            if (commodity.getAmounts().length == 0) {
                GuiSign<Handler> guiSign = new GuiSign<>(handler, new String[]{"", "请在上方输入购买", commodity.getName(), "的数量"}, (handler1, lines) -> {
                    String amountStr = lines[0];
                    if (!amountStr.isEmpty()) {
                        if (NumberUtils.isNumber(amountStr)) {
                            int amount = NumberUtils.toInt(amountStr);
                            buy(handler, commodity, amount);
                        }
                    }
                    ThreadUtils.delayExecute(new TimerTask() {
                        @Override
                        public void run() {
                            GuiManager.open(handler, GuiMenuShopAmount.this);
                        }
                    }, 2, TimeUnit.SECONDS);
                    return false;
                });
                GuiManager.open(handler, guiSign);
            } else {
                GuiMenuStatic<Handler, Item> guiMenuStatic = new GuiMenuStatic<>(owner, InventoryType.GENERIC_9X3, CustomGuiManager.getTitleDomainPrefix() + "购买物品");
                List<Integer> freeSlot = getFreeSlot(9, 17);
                Iterator<Integer> iterator = freeSlot.iterator();
                for (int amount : commodity.getAmounts()) {
                    if (iterator.hasNext()) {
                        guiMenuStatic.setItem(iterator.next(), new GuiItemCommon<>(new ItemCommonBuilder(XMaterial.PAPER).setDisplay(String.format("§e购买%d%s%s", amount, commodity.getUnit(), commodity.getName())).setAmount(amount).addLore("", "§e左键确认购买"))
                                .addActionExecutor(InventoryAction.LEFT, (h) -> {
                                    buy(handler, commodity, amount);
                                    ThreadUtils.delayExecute(new TimerTask() {
                                        @Override
                                        public void run() {
                                            GuiManager.open(handler, GuiMenuShopAmount.this);
                                        }
                                    }, 2, TimeUnit.SECONDS);
                                }));
                    } else {
                        break;
                    }
                }
                GuiManager.open(handler, guiMenuStatic);
            }

        });
    }

    public boolean buy(Handler handler, T commodity, int amount) {
        if (amount > 0) {
            if (EcoAPI.getInstance().take(MinecraftUtils.getHandlerName(handler), commodity.getEcoHandler(), commodity.getPrice(), amount, commodity.getServers(), String.format("buy %d amount: %s", amount, commodity.getName()))) {
                success(handler, amount, commodity);
                return true;
            } else {
                fail(handler, amount, commodity);
            }
        }
        return false;
    }


    @Override
    public ItemCommonBuilder formatBuilder(ItemCommonBuilder builder, T commodity) {
        return builder.addLore("", "§e" + commodity.getPrice().getPriceDisplay() + "§6" + commodity.getEcoHandler().getDisplayName() + " §b/" + commodity.getUnit());
    }

    @Override
    public void success(Handler handler, T commodity) {
        success(handler, 1, commodity);
    }

    public void fail(Handler handler, int i, T commodity) {
        super.fail(handler, commodity);
    }

    public abstract void success(Handler handler, int amount, T commodity);


    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Commodity<V> extends GuiMenuShop.Commodity<V> {

        private int[] amounts = new int[0];
        private String unit = "个";

        public Commodity(V value, @NonNull String name, @NonNull EcoHandler<?> ecoHandler, @NonNull EcoData price, List<String> servers) {
            super(value, name, ecoHandler, price, servers);
        }

        public Commodity(V value, String name, String type, EcoData price, List<String> servers) {
            super(value, name, type, price, servers);
        }

        public Commodity(V value, String name, String type, EcoData price) {
            super(value, name, type, price);
        }


    }


}
