package net.wdsj.mcserver.gui.common.config;

import com.google.common.collect.Lists;
import lombok.Getter;
import mc233.cn.wdsjlib.global.config.ItemStackConfig;
import net.wdsj.servercore.common.placeholder.PlaceholderManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:54
 */
@Getter
public class GuiItemStackConfig {

    private String id;
    private String material;
    private String durability;
    private String amount;
    private String name;
    private List<String> lore = new ArrayList<>();

    private String head;
    private String house;
    private String skin;

    private List<String> enchant = new ArrayList<>();
    private List<String> flag = new ArrayList<>();


    //TODO SHIT
    public ItemStackConfig build(Object handler, Map<String, String> map) {
       // System.out.printf(" debug > material: %s name: %s lore: %s%n", material, name, ArrayUtils.toString(lore));
        ItemStackConfig itemStackConfig = new ItemStackConfig();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String foo = "$" + entry.getKey();
            if (id != null)
                id = id.replace(foo, entry.getValue());
            if (material != null)
                material = material.replace(foo, entry.getValue());
            if (durability != null)
                durability = durability.replace(foo, entry.getValue());
            if (amount != null)
                amount = amount.replace(foo, entry.getValue());
            if (name != null)
                name = name.replace(foo, entry.getValue());
            if (head != null)
                head = PlaceholderManager.replace(handler, head.replace(foo, entry.getValue()));
            if (house != null)
                house = house.replace(foo, entry.getValue());
            if (skin != null)
                skin = PlaceholderManager.replace(handler, skin.replace(foo, entry.getValue()));
            if (lore != null)
                itemStackConfig.setLore(Lists.transform(lore, s -> s.replace(foo, entry.getValue())));
            if (enchant != null)
                itemStackConfig.setEnchant(Lists.transform(enchant, s -> s.replace(foo, entry.getValue())));
            if (flag != null)
                itemStackConfig.setFlag(Lists.transform(flag, s -> s.replace(foo, entry.getValue())));

        }
        itemStackConfig.setId(id == null ? null : Integer.parseInt(id));
        itemStackConfig.setMaterial(material == null ? null : material);
        itemStackConfig.setDurability(durability == null ? null : Integer.parseInt(durability));
        itemStackConfig.setAmount(amount == null ? null : Integer.parseInt(amount));
        itemStackConfig.setName(name == null ? null : name);
        itemStackConfig.setHead(head == null ? null : head);
        itemStackConfig.setHouse(house == null ? null : house);
        itemStackConfig.setSkin(skin == null ? null : skin);

        itemStackConfig.setLore(lore);
        itemStackConfig.setEnchant(enchant);
        itemStackConfig.setFlag(flag);
        return itemStackConfig;
    }

}
