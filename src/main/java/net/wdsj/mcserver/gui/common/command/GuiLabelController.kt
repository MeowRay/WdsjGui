package net.wdsj.mcserver.gui.common.command

import net.wdsj.mcserver.gui.common.wrapper.CanOpenItem

/**
 * @author  MeowRay
 * @date  2021/7/7 19:36
 * @version 1.0
 */
interface GuiLabelController {

    fun register(label: String, item: CanOpenItem)

    fun unregister(label :String )

    fun unregisterAll()
}