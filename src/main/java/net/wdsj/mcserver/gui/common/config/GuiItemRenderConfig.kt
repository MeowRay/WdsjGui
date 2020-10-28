package net.wdsj.mcserver.gui.common.config

import net.wdsj.mcserver.gui.common.utils.MenuUtils
import net.wdsj.servercore.config.invoke.annotation.ListInvoke
import java.util.*

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:14
 */
data class GuiItemRenderConfig(
    var update: Int = -1,
    var model: String? = null,
    var repo: String? = null,
    var args: Map<String, Any> = HashMap(),
    var slot: List<Int> = ArrayList(),

    @ListInvoke
    var display: List<GuiItemStackConfig> = ArrayList(),

    @ListInvoke
    var displayCondition: List<GuiItemShowConfig> = ArrayList(),

    var action: Map<String, List<String>> = HashMap(),
    var options: Map<String, Any?> = HashMap(),
    var initScript: String? = null,
) {

    @Transient
    var stringArgs: Map<String, String?>? = null
        private set

    fun init() {
        stringArgs =
            args
                .filter { entry -> entry.value is String }
                .mapValues { entry -> entry.value as String }
        initScript?.let {
            MenuUtils.scriptExecute(it, mapOf("options" to options ).plus(args), null, false)
        }
    }
}