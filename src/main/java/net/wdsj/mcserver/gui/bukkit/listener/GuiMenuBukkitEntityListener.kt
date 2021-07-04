package net.wdsj.mcserver.gui.bukkit.listener

import net.wdsj.mcserver.gui.bukkit.GuiBukkit
import net.wdsj.mcserver.gui.bukkit.adapter.GuiMenuRenderBukkitEntityAdapter
import net.wdsj.mcserver.gui.common.GuiManager
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu
import net.wdsj.servercore.WdsjServerAPI
import net.wdsj.servercore.eunm.inventory.InventoryAction
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * @author  MeowRay
 * @date  2021/7/4 18:14
 * @version 1.0
 */
class GuiMenuBukkitEntityListener : Listener {

    @EventHandler
    fun on(event: InventoryCloseEvent) {
        if (event.player is Player) {
            val id = WdsjServerAPI.getNmsService().getPlayerOpenWindowId(event.player as Player)
            val guiData = GuiManager.getGuiData(event.player)
            if (id == guiData.windowId) {
                guiData.setClose()
            }
        }
    }

    @EventHandler
    fun on(event: InventoryClickEvent) {
        if (event.whoClicked is Player) {
            val player = event.whoClicked as Player
            val id = WdsjServerAPI.getNmsService().getPlayerOpenWindowId(player)
            val guiData = GuiManager.getGuiData(player)
            if (id == guiData.windowId && guiData.nowOpen is GuiMenu<Player, *>) {
                val gui = guiData.nowOpen as GuiMenu<Player, *>
                if (gui.renderAdapter is GuiMenuRenderBukkitEntityAdapter) {
                    val adapter = (gui.renderAdapter as GuiMenuRenderBukkitEntityAdapter)
                    val clickType = event.click
                    val type = when {
                        clickType.isLeftClick && clickType.isShiftClick -> InventoryAction.SHIFT_LEFT
                        clickType.isLeftClick -> InventoryAction.LEFT
                        clickType.isRightClick && clickType.isShiftClick -> InventoryAction.SHIFT_RIGHT
                        clickType.isShiftClick -> InventoryAction.RIGHT
                        else -> InventoryAction.UNKNOWN
                    }
                    if (adapter.isCancel(player, type, event.slot)) {
                        event.isCancelled = true
                    }
                    if (adapter.isAsync) {
                        Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance()) {
                            gui.execute(player, type, event.slot)
                        }
                    } else {
                        gui.execute(player, type, event.slot)
                    }
                }
            }
        }
    }


}