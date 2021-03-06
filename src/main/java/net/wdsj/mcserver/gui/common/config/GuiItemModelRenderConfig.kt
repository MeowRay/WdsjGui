package net.wdsj.mcserver.gui.common.config

import net.wdsj.servercore.config.invoke.annotation.ListInvoke
import java.util.*

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:14
 */
data class GuiItemModelRenderConfig (

    var update: Int = -1,
    var args: Map<String, Any> = HashMap(),
    var requirementArgs: List<String> = ArrayList(),

    @ListInvoke
    var display: List<GuiItemStackConfig> = ArrayList(),
    var action: Map<String, ArrayList<String>> = HashMap(),


    var initScript: List<String> = ArrayList(),

)