package net.wdsj.mcserver.gui.common.gui.menu;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mc233.cn.wdsjlib.global.api.eco.EcoAPI;
import mc233.cn.wdsjlib.global.api.eco.EcoHandler;
import mc233.cn.wdsjlib.global.api.eco.EcoPointsHandler;
import mc233.cn.wdsjlib.global.api.eco.data.EcoData;
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder;
import net.wdsj.mcserver.gui.common.component.page.GuiMenuLoadedPage;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.item.GuiItemCommon;
import net.wdsj.mcserver.gui.common.model.template.GuiItemPaneUnderModel;
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import net.wdsj.servercore.message.ServerMessages;
import net.wdsj.servercore.utils.MinecraftUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/1 21:44
 */
public abstract class GuiMenuShop<T extends GuiMenuShop.Commodity< ?>, Handler, Item> extends GuiMenuStatic<Handler, Item> {

    private final Collection<T> items;

    public GuiMenuShop(Handler owner, Collection<T> items, int line, String title) {
        super(owner, InventoryType.getChestType(line), title);
        this.items = items;
        init();
        ImmutableList<Integer> freeSlots = getFreeSlots();
        GuiMenuLoadedPage<T, Handler, Item> guiMenuLoadedPage = new GuiMenuLoadedPage<T, Handler, Item>(this, items, getInventoryType().getSize() - 6, getInventoryType().getSize() - 4) {
            @Override
            public GuiItemBase<Handler, Item> getItemRender(int slot, T commodity) {
                ItemCommonBuilder builder = formatBuilder(getCommodityView(commodity), commodity);
                return buildCommodity(builder, commodity);
            }

            @Override
            public ImmutableList<Integer> getFreeSlots() {
                return freeSlots;
            }
        };
        guiMenuLoadedPage.loadPage(1);
        setItem(getInventoryType().getSize() - 1, footerItem());
    }

    public void init() {
        loadGuiMenuModel(new GuiItemPaneUnderModel<>(this));

    }


    public GuiItemBase<Handler, Item> footerItem() {
        return GuiItemRepository.BACK_MENU_RIGHT;
    }

    public ItemCommonBuilder formatBuilder(ItemCommonBuilder builder, T commodity) {
        return builder.addLore("", "§e" + commodity.getPrice().getPriceDisplay() + "§6" + commodity.getEcoHandler().getDisplayName());
    }

    public GuiItemBase<Handler, Item> buildCommodity(ItemCommonBuilder builder, T commodity) {
        return new GuiItemCommon<Handler, Item>(builder).addActionExecutor(InventoryAction.LEFT, handler -> {

            if (EcoAPI.getInstance().take(MinecraftUtils.getHandlerName(handler), commodity.getEcoHandler(), commodity.getPrice(), 1, commodity.getServers(), String.format("buy %d",  commodity.getName()))) {
                success(handler, commodity);
            }
       /*     if (commodity.getEcoHandler().take(MinecraftUtils.getHandlerName(handler), commodity.getPrice(), commodity.getServers(), "buy " + commodity.getName())) {
            success(handler, commodity);
            }*/
        });
    }

    public abstract ItemCommonBuilder getCommodityView(T commodity);

    public abstract void success(Handler handler, T commodity);

    public void fail(Handler handler, T commodity) {
        if (commodity.getEcoHandler() instanceof EcoPointsHandler) {
            MinecraftUtils.sendTextComponentMessage(handler, ServerMessages.Component.TAKE_POINT_FAIL);
        } else {
            MinecraftUtils.sendMessage(handler, "§c" + commodity.getEcoHandler().getDisplayName() + "不足!");
        }
    }

    public ImmutableList<Integer> getFreeSlots() {
        return ImmutableList.copyOf(getFreeSlot(0, getInventoryType().getSize() - 9));
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    @Getter
    public static class Commodity<V > {

        private final V value;

        @NonNull
        private final String name;

        @NonNull
        private final EcoHandler<?>  ecoHandler;

        @NonNull
        private final EcoData price;

        private List<String> servers = null;


        public Commodity(V value, String name, String type, EcoData price, List<String> servers) {
            this.value = value;
            this.name = name;
            this.ecoHandler = EcoAPI.getInstance().getEcoHandler(type);
            this.servers = servers;
            this.price = price;
        }

        public Commodity(V value, String name, String type, EcoData price) {
            this(value, name, type, price, null);
        }


    }
}
