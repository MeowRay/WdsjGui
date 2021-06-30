package net.wdsj.mcserver.gui.common.utils

import net.wdsj.mcserver.gui.common.component.page.GuiMenuPage
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic
import net.wdsj.mcserver.gui.common.item.GuiItem
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem
import net.wdsj.mcserver.gui.common.render.RenderItem
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository

/**
 * @author  MeowRay
 * @date  2021/4/26 19:01
 * @version 1.0
 */

class TemplateUtils {
    companion object {
        @JvmStatic
        fun GuiMenuPage<*, *, *>.setUnderEvenlyByPageButton() {
            setPreSlot(guiMenu.inventoryType.size - 6)
            setNextSlot(guiMenu.inventoryType.size - 4)
        }
        @JvmStatic
        fun GuiMenu<*, *>.setLastBackButton() {
          val v =  this as GuiMenu<Any , Any>
            if (this is GuiMenuStatic) {
                (  v as GuiMenuStatic<Any ,Any>).setItem(inventoryType.size-1 , GuiItemRepository.BACK_MENU_RIGHT as GuiItem<Any,Any>)
            }else{
                addRenderItem(GuiMenuRenderItem(RenderItem(inventoryType.size - 1 ,  GuiItemRepository.BACK_MENU_RIGHT as GuiItem<Any,Any>)))
            }
        }
    }
}