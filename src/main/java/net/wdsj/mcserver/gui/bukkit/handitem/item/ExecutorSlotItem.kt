package net.wdsj.mcserver.gui.bukkit.handitem.item

import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/7/10 12:27
 */
class ExecutorSlotItem @JvmOverloads  constructor(
    private val slot: Int,
    builder: ItemCommonBuilder,
    keyGetter: () -> String = { UUID.randomUUID().toString() }
) : ExecutorItem(builder, keyGetter) {
    override fun givePlayer(player: Player) {
        player.inventory.setItem(slot, item)
    }
}