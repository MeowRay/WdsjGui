package net.wdsj.mcserver.gui.bukkit.handitem.item

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimap
import de.tr7zw.nbtapi.NBTItem
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.bukkit.handitem.ExecutorItemManager
import net.wdsj.servercore.eunm.InteractAction
import net.wdsj.servercore.interfaces.Executor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/7/3 23:15
 */
open class ExecutorItem @JvmOverloads constructor(
    builder: ItemCommonBuilder,
    keyGetter: () -> String = { UUID.randomUUID().toString() }
) : CanGrantItem {

    private val executorMap: Multimap<InteractAction, Executor<Player>> = LinkedHashMultimap.create()
    private val commonBuilder: ItemCommonBuilder = builder

    val key: String = keyGetter()

    var canDrop = false
    var canMove = false

    fun execute(action: InteractAction, player: Player) {
        executorMap.get(InteractAction.ALL).forEach {
            it.execute(player)
        }
        when (action) {
            InteractAction.RIGHT_CLICK_AIR, InteractAction.RIGHT_CLICK_BLOCK ->
                executorMap.get(InteractAction.RIGHT).forEach { it.execute(player) }
            InteractAction.LEFT_CLICK_AIR, InteractAction.LEFT_CLICK_BLOCK ->
                executorMap.get(InteractAction.LEFT).forEach { it.execute(player) }
        }

        executorMap.get(action).forEach {
            it.execute(player)
        }
    }

    fun addActionExecutor(action: InteractAction, executor: Executor<Player>) {
        executorMap.put(action, executor)
    }

    fun delActionExecutor(action: InteractAction, executor: Executor<Player>) {
        executorMap.remove(action, executor)
    }

    fun resetAction() {
        executorMap.clear()
    }

    val item: ItemStack
        get() {
            val build: ItemStack = commonBuilder.build()
            val nbtItem = NBTItem( build)
            nbtItem.setString(ExecutorItemManager.NBT_TAG, key)
            return nbtItem.item
        }

    override fun givePlayer(player: Player) {
        player.inventory.addItem(item)
    }

}
