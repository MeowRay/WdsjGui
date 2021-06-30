package net.wdsj.mcserver.gui.common.component.page

import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/5/17 21:20
 */
abstract class GuiMenuDynamicLoadPage2<T, Handler, Item>(
    guiMenu: GuiMenuStatic<Handler, Item>, preSlot: Int,
    nextSlot: Int
) : GuiMenuPage<T, Handler, Item>(
    guiMenu, preSlot, nextSlot
) {

    private val items = mutableMapOf<Int, T>()

    private var itemCount = 0
    private var end = false

    override fun getContent(index: Int): T? {
        val dynamicContent = getDynamicContent(index)
        if (dynamicContent == null) {
            itemCount = index
            end = true
        } else items[index] = dynamicContent
        return dynamicContent
    }

    abstract fun getDynamicContent(index: Int): T?

    override fun getContentSize(): Int {
        return if (!end) freeSlots.size + 1 else itemCount
    }

}