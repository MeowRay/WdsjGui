package net.wdsj.mcserver.gui.common.wrapper

import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.common.simpleconfig.ConfigurationSection
import net.wdsj.mcserver.gui.bukkit.handitem.item.ExecutorItem
import net.wdsj.mcserver.gui.bukkit.handitem.item.ExecutorSlotItem
import net.wdsj.mcserver.gui.common.GuiConfigManager
import net.wdsj.mcserver.gui.common.config.ExecutorItemConfig
import net.wdsj.mcserver.gui.common.executor.GuiItemExecutorCollection
import net.wdsj.mcserver.gui.common.utils.MenuUtils
import net.wdsj.servercore.compatible.XMaterial
import net.wdsj.servercore.eunm.InteractAction
import net.wdsj.servercore.utils.extensions.invokeOrNew
import org.bukkit.entity.Player

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/6/12 23:17
 */
class ExecutorItemConfigWrapper(val section: ConfigurationSection) {


    val config: ExecutorItemConfig = section.invokeOrNew(ExecutorItemConfig::class.java)

    val itemBuilder = ItemCommonBuilder.createFromConfig(config.item, XMaterial.AIR)

    val permissionBypass: Boolean = config.requirement == "true"

    val action = config.action.mapKeys {
        InteractAction.mathInteractAction(it.key.toUpperCase())
            .orElseThrow { error("找不到InteractAction ${it.key}") }
    }

    val actionExecutorMap: Map<InteractAction, GuiItemExecutorCollection<Player>> = action.mapValues {
        GuiConfigManager.createExecuteCollection(it.value, emptyMap())
    }

    init {
        if (itemBuilder.xMaterial == XMaterial.AIR) {
            error("Executor material Not Found")
        }
    }

    fun build(): ExecutorItem {
        return ExecutorSlotItem(config.slot, itemBuilder) { config.uniqueId }.apply {
            canDrop = config.canDrop
            canMove = config.canMove
            actionExecutorMap.forEach {
                addActionExecutor(it.key, it.value)
            }
        }
    }

    fun open() {


    }


    fun requirementCanOpen(handler: Any): Boolean {
        return permissionBypass || MenuUtils.scriptExecute(config.requirement, handler, false)
    }


}