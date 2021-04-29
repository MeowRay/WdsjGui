package net.wdsj.mcserver.gui.common.utils

import net.wdsj.mcserver.gui.common.component.page.GuiMenuPage

/**
 * @author  MeowRay
 * @date  2021/4/26 19:01
 * @version 1.0
 */

fun GuiMenuPage<*, *, *>.setUnderEvenlyByPageButton() {
    setPreSlot(guiMenu.inventoryType.size - 6)
    setNextSlot(guiMenu.inventoryType.size - 4)
}