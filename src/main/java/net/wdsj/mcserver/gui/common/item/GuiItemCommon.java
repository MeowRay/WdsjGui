package net.wdsj.mcserver.gui.common.item;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 16:29
 */
public class GuiItemCommon<Handler, Item> extends GuiItemBase<Handler, Item> {


    private ItemCommonBuilder builder;

    public GuiItemCommon(ItemCommonBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Item getItem() {
        return builder.build();
    }

    @SneakyThrows
    @Override
    public Item getItemView(Handler handler) {
        ItemCommonBuilder clone = builder.clone();
        clone.setDisplay(replace(handler, clone.getDisplay()));
        clone.setLores(Lists.transform(clone.getLores(), s -> replace(handler, s)));
        return clone.build();
    }

    @Override
    public GuiItemBase<Handler, Item> clone() {
        GuiItemCommon<Handler, Item> clone = (GuiItemCommon<Handler, Item>) super.clone();
        try {
            clone.builder = builder.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
