package net.wdsj.mcserver.gui.common.repo;

import mc233.cn.wdsjlib.bukkit.repository.HeadRepository;
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder;
import net.wdsj.mcserver.gui.common.Gui;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.GuiData;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.item.GuiItemCommon;
import net.wdsj.servercore.compatible.XMaterial;
import net.wdsj.servercore.eunm.inventory.InventoryAction;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/1 13:52
 */
public class GuiItemRepository {

    public static final GuiItemBase PLACEHOLDER_BLACK_GLASS_PANE = new GuiItemCommon<>(new ItemCommonBuilder(XMaterial.BLACK_STAINED_GLASS_PANE).setDisplay("§7§oWdsj.net"));

    public static final GuiItemBase QUIT_MENU_FENCE = new GuiItemCommon<>(new ItemCommonBuilder(XMaterial.OAK_FENCE_GATE).setDisplay("§c✈ 关闭")).addActionExecutor(InventoryAction.LEFT, o -> GuiManager.close(o));
    ;
    public static final GuiItemBase QUIT_MENU_ARROW = new GuiItemCommon(ItemCommonBuilder.createHeadFromRepo(HeadRepository.WHITE_ARROW_RIGHT_DOWN.getKey()).setDisplay("§c✈ 关闭")).addActionExecutor(InventoryAction.LEFT, o -> GuiManager.close((o)));
    ;

    public static final GuiItemBase BACK_MENU_FENCE = new GuiItemCommon(new ItemCommonBuilder(XMaterial.OAK_FENCE_GATE).setDisplay("§c✈ 返回")).addActionExecutor(InventoryAction.LEFT, o -> {
        if (!GuiManager.openHistory(o, true, 2)) {
            GuiManager.close(o);
        }
    });

    public static final GuiItemBase BACK_MENU_RIGHT = new GuiItemCommon(ItemCommonBuilder.createHeadFromRepo(HeadRepository.WHITE_ARROW_RIGHT.getKey()).setDisplay("§c✈ 返回")).addActionExecutor(InventoryAction.LEFT, o -> {
        if (!GuiManager.openHistory(o, true, 2)) {
            GuiManager.close(o);
        }
    });

    public static final GuiItemBase PAGE_PRE = new GuiItemCommon(ItemCommonBuilder.createHeadFromRepo(HeadRepository.WHITE_ARROW_LEFT.getKey()).setDisplay("§e◀ 上一页"));

    public static final GuiItemBase PAGE_NEXT = new GuiItemCommon(ItemCommonBuilder.createHeadFromRepo(HeadRepository.WHITE_ARROW_RIGHT.getKey()).setDisplay("§e下一页 ▶"));

    public static final GuiItemBase ERROR = new GuiItemCommon(new ItemCommonBuilder(XMaterial.BARRIER).setDisplay("§cSYSTEM ERROR 系统错误"));


    public static GuiItem get(String name) {
        try {
            return (GuiItem) GuiItemRepository.class.getDeclaredField(name.toUpperCase()).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    ;


}
