package net.wdsj.mcserver.gui.common.config

import net.wdsj.mcserver.gui.common.utils.MenuUtils
import net.wdsj.servercore.config.invoke.annotation.ListInvoke
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:14
 */
data class GuiItemRenderConfig(
    var update: Int = -1,
    var model: String? = null,
    var repo: String? = null,
    var args: MutableMap<String, Any> = HashMap(),
    var slot: List<Int> = ArrayList(),
    var static: Boolean = false,

    @ListInvoke
    var display: List<GuiItemStackConfig> = ArrayList(),

    @ListInvoke
    var displayCondition: List<GuiItemShowConfig> = ArrayList(),

    var action: MutableMap<String, List<String>> = HashMap(),
    var options: MutableMap<String, Any?> = HashMap(),

    var initScript: List<String> = ArrayList(),
) {

    @Transient
    var stringArgs: Map<String, String?>? = null
        private set

    fun init() {
        stringArgs =
            args
                .filter { entry -> entry.value is String }
                .mapValues { entry -> entry.value as String }
        args["options"] = options

        if (initScript.isNotEmpty()) {
            initScript.forEach {
                MenuUtils.scriptExecute(it, mapOf("render" to this).plus(args), null, false)
            }
        }
    }
}