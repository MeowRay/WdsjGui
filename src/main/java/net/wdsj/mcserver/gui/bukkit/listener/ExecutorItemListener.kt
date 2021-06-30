package net.wdsj.mcserver.gui.bukkit.listener

import net.wdsj.mcserver.gui.bukkit.handitem.ExecutorItemManager
import net.wdsj.servercore.eunm.InteractAction
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * @author  MeowRay
 * @date  2021/6/12 22:49
 * @version 1.0
 */
class ExecutorItemListener : Listener {

    @EventHandler(ignoreCancelled = true)
    fun on(event: InventoryClickEvent) {
        if (event.currentItem != null && event.currentItem.type != Material.AIR) {
            val executorItem = ExecutorItemManager.getExecutorItem(event.currentItem)
            if (executorItem.key) {
                if (executorItem.value == null) {
                } else {
                    if (!executorItem.value!!.canMove) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler() //ignoreCancelled = true
    fun on(event: PlayerInteractEvent) {
        ExecutorItemManager.enter(event.player, InteractAction.of(event.action), event.item)
    }



    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerDropItemEvent) {
        val executorItem = ExecutorItemManager.getExecutorItem(event.itemDrop.itemStack)
        if (executorItem.key) {
            if (executorItem.value == null) {
            } else {
                if (!executorItem.value!!.canDrop) {
                    event.isCancelled = true
                }
            }
        }
    }



}