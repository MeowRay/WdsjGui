package net.wdsj.mcserver.gui.bukkit.model

import com.google.common.collect.ImmutableList
import mc233.cn.wdsjlib.bukkit.gui.CustomGuiManager
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.common.component.page.GuiMenuLoadedPage
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic
import net.wdsj.mcserver.gui.common.item.GuiItem
import net.wdsj.mcserver.gui.common.item.GuiItemBase
import net.wdsj.mcserver.gui.common.item.GuiItemCommon
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository
import net.wdsj.mcserver.gui.common.utils.TemplateUtils.Companion.setUnderEvenlyByPageButton
import net.wdsj.servercore.eunm.inventory.InventoryType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author  MeowRay
 * @date  2021/5/15 22:08
 * @version 1.0
 */
class GuiItemStackOverviewMenu(
    owner: Player,
    val list: List<ItemStack>,
    title: String = CustomGuiManager.getTitleDomainPrefix() + "物品预览"
) : GuiMenuStatic<Player, ItemStack>(owner, InventoryType.GENERIC_9X6, title) {

    val slots = ImmutableList.copyOf(getFreeSlot(0, 45))
    val page = object : GuiMenuLoadedPage<ItemStack, Player, ItemStack>(this, list, 45, 47) {
        override fun getItemRender(slot: Int, t: ItemStack): GuiItemBase<Player, ItemStack> {
            return GuiItemCommon(ItemCommonBuilder.createFromBukkitItemStack(t))
        }

        override fun getFreeSlots(): ImmutableList<Int> {
            return slots
        }
    }

    init {
        page.setUnderEvenlyByPageButton()
        setItem(getInventoryType().size - 1, GuiItemRepository.BACK_MENU_FENCE as GuiItem<Player, ItemStack>)
        page.loadPage(1)
    }

}