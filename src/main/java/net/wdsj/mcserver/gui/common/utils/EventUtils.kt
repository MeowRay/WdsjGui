package net.wdsj.mcserver.gui.common.utils

import com.google.common.eventbus.Subscribe
import net.wdsj.mcserver.gui.common.Gui
import net.wdsj.mcserver.gui.common.event.GuiCloseEvent
import java.util.*

/**
 * @author  MeowRay
 * @date  2021/7/4 19:24
 * @version 1.0
 */
object EventUtils {

    val closeListenerMap = WeakHashMap<Gui<*>, Gui<*>.() -> Unit>()

    fun <M : Gui<*>> M.registerCloseListener(block: M.() -> Unit) {
        closeListenerMap[this] = block as (Gui<*>.() -> Unit)?
    }

}

object GuiEventListener {

    @Subscribe
    fun on(event: GuiCloseEvent<*>) {
        EventUtils.closeListenerMap[event.gui]?.let {
            it(event.gui)
        }
    }

}
