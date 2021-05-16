package net.wdsj.mcserver.gui.common.container;

import com.google.common.collect.Lists;
import net.wdsj.mcserver.gui.common.builder.GuiItemRenderBuilder;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.utils.Utils;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/9 1:09
 */
public class GuiMenuItemContainer<Handler, Item> implements Cloneable {

    private final List<Integer> list;
    private final List<GuiItemRenderBuilder<Handler, Item>> guiitemContainer = new ArrayList<>();

    public GuiMenuItemContainer(List<Integer> list) {
        this.list = list;
    }

    public GuiMenuItemContainer(Integer[] list) {
        this(Lists.newArrayList(list));
    }

    public void addItem(GuiItemRenderBuilder<Handler, Item> guiItem) {
        guiitemContainer.add(guiItem);
    }

    public void removeItem(GuiItemRenderBuilder<Handler, Item> guiItem) {
        guiitemContainer.remove(guiItem);
    }

    public void build(GuiMenu<Handler, Item> guiMenu) {
        getRenderItems().forEach(guiMenu::addRenderItem);
    }

    public List<GuiMenuRenderItem<Handler, Item>> getRenderItems() {
        List<GuiMenuRenderItem<Handler, Item>> result = new ArrayList<>();
        Iterator<Integer> iterator = list.iterator();
        for (GuiItemRenderBuilder<Handler, Item> builder : guiitemContainer) {
            if (iterator.hasNext()) {
                result.add(builder.build(iterator.next()));
            } else {
                break;
            }
        }
        return result;
    }

    @Override
    public GuiMenuItemContainer<Handler, Item> clone() {
        return new GuiMenuItemContainer<>(list);
    }

    public static <H, I> GuiMenuItemContainer<H, I> parse(String str) {
        List<Integer> list = new ArrayList<>();
        String[] split1 = str.split(",");
        for (String group : split1) {
            String[] split = group.split("-", 2);
            if (split.length > 1) {
                String[] c1 = split[0].split(":", 2);
                String[] c2 = split[1].split(":", 2);
                if (c1.length > 1 && c2.length > 1) {
                    list.addAll(Utils.INSTANCE.getInventoryRange(Integer.parseInt(c1[0]), Integer.parseInt(c1[1]), Integer.parseInt(c2[0]), Integer.parseInt(c2[1])));
                }
            } else if (split.length == 1) {
                String[] c1 = split[0].split(":", 2);
                int y = Integer.parseInt(c1[0]);
                int x = Integer.parseInt(c1[1]);
                list.add((y == 1 ? x : (y - 1) * 9 + x) - 1);

            }
        }
        return new GuiMenuItemContainer<>(list);
    }

}
