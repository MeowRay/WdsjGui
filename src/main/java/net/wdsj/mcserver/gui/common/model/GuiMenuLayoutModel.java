package net.wdsj.mcserver.gui.common.model;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.builder.GuiItemRenderBuilder;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import net.wdsj.servercore.eunm.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 16:35
 */
public class GuiMenuLayoutModel<Handler, Item> implements GuiMenuModel<Handler, Item> {


    private final String title;
    private InventoryType inventoryType;
    private final String[] lines;
    private final TIntObjectHashMap<String[]> boxMap = new TIntObjectHashMap<>();
    private final int length;
    private final    TIntObjectHashMap<String>      layoutMap;


    private final Map<String, GuiItemRenderBuilder<Handler, Item>> itemMap = new THashMap<>();


    public GuiMenuLayoutModel(String title, String... lines) {
        this(title, InventoryType.getChestType(lines.length), lines);
    }

    public GuiMenuLayoutModel(String title, InventoryType inventoryType, String[] lines) {
        this.title = title;
        this.inventoryType = inventoryType;
        this.lines = lines;
        length = lines.length;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] foos = MenuUtils.getFoo(line);
            boxMap.put(i, foos);
        }
        layoutMap = MenuUtils.getLayoutMap(boxMap, length);
    }

    public GuiMenuLayoutModel(String title, String layout) {
        this(title, layout.split("\n"));
    }

    public void registerPlaceholder(String foo, GuiItemRenderBuilder<Handler, Item> renderBuilder) {
        itemMap.put(foo, renderBuilder);
    }

    public void unregisterPlaceholder(String foo) {
        itemMap.remove(foo);
    }

    public GuiItemRenderBuilder<Handler, Item> getPlaceholder(String foo) {
        return itemMap.get(foo);
    }


    @Override
    public <T extends GuiMenu<Handler, Item>> T create(GuiMenuRenderAdapter<Handler, Item> renderAdapter, Handler handler) {

        GuiMenu<Handler, Item> guiMenu = new GuiMenu<Handler, Item>(handler, inventoryType, title) {
            @Override
            public GuiMenuRenderAdapter<Handler, Item> renderAdapter() {
                return renderAdapter;
            }
        };

        getRenderItems(handler).forEach(guiMenu::addRenderItem);

        return (T) guiMenu;
    }

    @Override
    public List<GuiMenuRenderItem<Handler, Item>> getRenderItems(Handler handler) {
        return getRenderItems(handler, itemMap);
    }

    public List<GuiMenuRenderItem<Handler, Item>> getRenderItems(Handler handler, Map<String, GuiItemRenderBuilder<Handler, Item>> itemMap) {
        List<GuiMenuRenderItem<Handler, Item>> list = new ArrayList<>();
        for (int slot : layoutMap.keys()) {
            GuiItemRenderBuilder<Handler, Item> placeholder = itemMap.get(layoutMap.get(slot));
            if (placeholder != null) {
                list.add(placeholder.build(slot));
            }
        }
        return list;
    }

    @Override
    public InventoryType getInventoryType() {
        return inventoryType;
    }


}
