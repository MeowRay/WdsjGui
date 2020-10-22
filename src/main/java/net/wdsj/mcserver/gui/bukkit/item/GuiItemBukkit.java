package net.wdsj.mcserver.gui.bukkit.item;

import mc233.cn.wdsjlib.bukkit.utils.ItemBuilder;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/19 22:04
 */
public class GuiItemBukkit extends GuiItemBase<Player, ItemStack> {

    private  ItemBuilder builder;

    public GuiItemBukkit(ItemBuilder builder){
        this.builder = builder;
    }

    @Override
    public ItemStack getItem() {
        return builder.build();
    }

    @Override
    public ItemStack getItemView(Player player) {
        final ItemStack itemStack = builder.build();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (meta.getDisplayName() != null) {
                meta.setDisplayName(replace(player, meta.getDisplayName()));
            }
            if (meta.hasLore()) {
                List<String> lore = new ArrayList<>(meta.getLore());
                for (int i = 0; i < meta.getLore().size(); i++) {
                    lore.set(i, replace(player, lore.get(i)));
                }
                meta.setLore(lore);
            }
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public GuiItemBase<Player, ItemStack> clone() {
        GuiItemBukkit clone = (GuiItemBukkit) super.clone();
        clone.builder = builder.clone();
        return clone;
    }
}
