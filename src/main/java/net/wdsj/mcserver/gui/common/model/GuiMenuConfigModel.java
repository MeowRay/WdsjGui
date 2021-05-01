package net.wdsj.mcserver.gui.common.model;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import javafx.util.Pair;
import com.google.common.base.Strings;
import net.wdsj.mcserver.gui.common.container.GuiMenuItemContainer;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.utils.tran.MapArgTrans;
import net.wdsj.mcserver.gui.common.wrapper.GuiMenuConfigWrapper;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.builder.GuiItemRenderBuilder;
import net.wdsj.mcserver.gui.common.config.GuiItemRenderConfig;
import net.wdsj.mcserver.gui.common.config.GuiMenuMainConfig;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderDynamicItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;
import net.wdsj.servercore.common.IteratorGroupCycle;
import net.wdsj.servercore.common.placeholder.PlaceholderManager;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import net.wdsj.servercore.utils.ScriptUtils;
import org.jetbrains.annotations.Nullable;

import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/21 13:17
 */
public class GuiMenuConfigModel<Handler, Item> implements GuiMenuModel<Handler, Item> {


    private final GuiMenuMainConfig menuConfig;
    private GuiMenuLayoutModel<Handler, Item> layoutModel;
    private InventoryType type;
    private final String title;

    private final GuiMenuConfigModel<Handler, Item> parentModel;
    private final Map<GuiMenuItemContainer<Handler, Item>, List<GuiItemRenderConfig>> containerMap = new HashMap<>();

    public GuiMenuConfigModel(GuiMenuMainConfig menuConfig) {
        this.menuConfig = menuConfig;

        if (menuConfig.getType().equalsIgnoreCase("CHEST")) {
            if (!menuConfig.getLayout().isEmpty()) {
                type = InventoryType.getChestType(menuConfig.getLayout().size());
            } else if (menuConfig.getSize() != -1) {
                type = InventoryType.getChestType(menuConfig.getSize() / 9);
            }
        }

        if (menuConfig.isInherit() && menuConfig.getParent() != null) {
            GuiMenuConfigWrapper parentModelWrapper = GuiConfigManager.getGuiMenu(menuConfig.getParent(), true);
            Preconditions.checkNotNull(parentModelWrapper, String.format("菜单模板%s未注册!", menuConfig.getParent()));
            parentModel = parentModelWrapper.getGuiMenuConfigModel();
            if (type == null) {
                type = parentModelWrapper.getGuiMenuConfigModel().getInventoryType();
            }
        } else {
            parentModel = null;
        }

        if (!Strings.isNullOrEmpty(menuConfig.getTitle())) {
            title = menuConfig.getTitle();
        } else if (parentModel != null && parentModel.title != null) {
            title = parentModel.title;
        } else {
            title = "NO DEFINE!";
        }

        if (!menuConfig.getLayout().isEmpty()) {
            layoutModel = new GuiMenuLayoutModel<>(menuConfig.getTitle(), menuConfig.getLayout().toArray(new String[0]));
        }

        for (Map.Entry<String, GuiMenuMainConfig.Container> entry : menuConfig.getContainer().entrySet()) {
            if (!entry.getValue().getLayoutItems().isEmpty()) {
                List<GuiItemRenderConfig> collect = entry.getValue().getLayoutItems().stream().map(s -> {
                    GuiItemRenderConfig config = menuConfig.getItems().get(s);
                    Preconditions.checkNotNull(config , String.format("layoutItem %s most not null",  s));
                    return config;
                }).collect(Collectors.toList());
                containerMap.put(GuiMenuItemContainer.parse(entry.getValue().getRange()), collect);
            } else {
                containerMap.put(GuiMenuItemContainer.parse(entry.getValue().getRange()), entry.getValue().getItems());
            }

        }

        for (Map.Entry<GuiMenuItemContainer<Handler, Item>, List<GuiItemRenderConfig>> entry : containerMap.entrySet()) {
            List<GuiItemRenderConfig> list = entry.getValue();
            for (GuiItemRenderConfig value : list) {
                if (value.getModel() != null) {
                    GuiConfigManager.setMergeModelRenderConfig(value.getModel(), value);
                }
                value.init();
                value.setAction(Maps.transformValues(value.getAction(), strings -> Lists.transform(strings, new MapArgTrans(value.getStringArgs()))));
            }
        }
        for (Map.Entry<String, GuiItemRenderConfig> entry : menuConfig.getItems().entrySet()) {
            GuiItemRenderConfig value = entry.getValue();

            if (value.getModel() != null) {
                GuiConfigManager.setMergeModelRenderConfig(value.getModel(), value);
            }
            value.init();
            value.setAction(Maps.transformValues(value.getAction(), strings -> Lists.transform(strings, new MapArgTrans(value.getStringArgs()))));
        }

    }


    @Override
    public <T extends GuiMenu<Handler, Item>> T create(GuiMenuRenderAdapter<Handler, Item> renderAdapter, Handler handler) {
        GuiMenu<Handler, Item> guiMenu = new GuiMenu<Handler, Item>(handler, type, PlaceholderManager.replace(handler, title)) {
            @Override
            public GuiMenuRenderAdapter<Handler, Item> renderAdapter() {
                return renderAdapter;
            }
        };

        //    Stopwatch stopwatch = Stopwatch.createStarted();
        List<GuiMenuRenderItem<Handler, Item>> renderItems = getRenderItems(handler);
        //  System.out.println("get render items "+ stopwatch.elapsed(TimeUnit.MILLISECONDS));
        for (GuiMenuRenderItem<Handler, Item> renderItem : renderItems) {
            guiMenu.addRenderItem(renderItem);
        }

        if (!com.google.common.base.Strings.isNullOrEmpty(menuConfig.getConstruct())) {
            try {
                ScriptUtils.getValue(menuConfig.getConstruct(), new Pair("player", handler), new Pair<>("menu", guiMenu));
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
        //   System.out.println("construct "+ stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return (T) guiMenu;
    }


    @Override
    public List<GuiMenuRenderItem<Handler, Item>> getRenderItems(Handler handler) {
        List<GuiMenuRenderItem<Handler, Item>> list = new ArrayList<>();
        Map<String, GuiMenuRenderItem<Handler, Item>> staticGuiItemMap = new THashMap<>();
        Map<String, GuiItemRenderBuilder<Handler, Item>> fooGuiItemMap = new THashMap<>();


        for (Map.Entry<String, GuiItemRenderConfig> entry : menuConfig.getItems().entrySet()) {
            GuiItemRenderConfig config = entry.getValue();
            List<GuiItem<?, ?>> guiItem = GuiConfigManager.getGuiItemRender(handler, config);
            if (guiItem != null && !guiItem.isEmpty()) {
                if (!entry.getValue().getSlot().isEmpty()) {
                    TIntObjectHashMap<Collection<?>> intObjectHashMap = new TIntObjectHashMap<>();
                    intObjectHashMap.put(1, guiItem);
                    intObjectHashMap.put(2, config.getSlot());
                    IteratorGroupCycle groupCycle = new IteratorGroupCycle(intObjectHashMap);
                    List<RenderItem<Handler, Item>> collect = new ArrayList<>();
                    for (TIntObjectHashMap<Object> objectTIntObjectHashMap : groupCycle.getAll()) {
                        GuiItem<Handler, Item> guiItem1 = (GuiItem<Handler, Item>) objectTIntObjectHashMap.get(1);
                        int slot = (int) objectTIntObjectHashMap.get(2);
                        collect.add(new RenderItem<>(slot, guiItem1));
                    }
                    staticGuiItemMap.put(entry.getKey(), new GuiMenuRenderDynamicItem<>(config.getUpdate(), collect.toArray(new RenderItem[0])));
                } else {
                    if (guiItem.size() == 1) {
                        fooGuiItemMap.put(entry.getKey(), new GuiItemRenderBuilder<>((GuiItem<Handler, Item>) guiItem.get(0)));
                    } else {
                        fooGuiItemMap.put(entry.getKey(), new GuiItemRenderBuilder<>(config.getUpdate(), Lists.transform(guiItem, guiItem12 -> (GuiItem<Handler, Item>) guiItem12)));
                    }
                }
            }
        }

        for (Map.Entry<GuiMenuItemContainer<Handler, Item>, List<GuiItemRenderConfig>> entry : containerMap.entrySet()) {
            List<GuiItemRenderConfig> value = entry.getValue();
            GuiMenuItemContainer<Handler, Item> container = entry.getKey().clone();
            for (GuiItemRenderConfig config : value) {
                List<GuiItem<?, ?>> guiItem = GuiConfigManager.getGuiItemRender(handler, config);
                if (guiItem.size() == 1) {
                    container.addItem(new GuiItemRenderBuilder<>((GuiItem<Handler, Item>) guiItem.get(0)));
                } else {
                    container.addItem(new GuiItemRenderBuilder<>(config.getUpdate(), Lists.transform(guiItem, guiItem12 -> (GuiItem<Handler, Item>) guiItem12)));
                }
            }
            list.addAll(container.getRenderItems());
        }

        if (parentModel != null) {
            list.addAll(parentModel.getRenderItems(handler));
        }

        if (layoutModel != null) {
            list.addAll(layoutModel.getRenderItems(handler, fooGuiItemMap));
        }

        for (Map.Entry<String, GuiMenuRenderItem<Handler, Item>> entry : staticGuiItemMap.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }

    @Override
    public InventoryType getInventoryType() {
        return type;
    }
}
