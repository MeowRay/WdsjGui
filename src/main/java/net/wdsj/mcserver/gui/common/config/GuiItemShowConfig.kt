package net.wdsj.mcserver.gui.common.config

import lombok.Getter
import net.wdsj.servercore.config.invoke.annotation.ListInvoke
import java.util.*

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/29 15:02
 */
@Getter
class GuiItemShowConfig {
    var condition = "false"

    var action: MutableMap<String, List<String>>? =  null

    @ListInvoke
    var item: List<GuiItemStackConfig> = ArrayList()
}