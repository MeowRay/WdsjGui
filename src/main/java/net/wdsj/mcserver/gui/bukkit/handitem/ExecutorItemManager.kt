package net.wdsj.mcserver.gui.bukkit.handitem

import de.tr7zw.nbtapi.NBTItem
import javafx.util.Pair
import net.wdsj.common.simpleconfig.ConfigurationSection
import net.wdsj.mcserver.gui.bukkit.handitem.item.ExecutorItem
import net.wdsj.mcserver.gui.common.wrapper.ExecutorItemConfigWrapper
import net.wdsj.servercore.eunm.InteractAction
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/7/3 23:12
 */
object ExecutorItemManager {

    private val executorItemMap: MutableMap<String, ExecutorItem> = HashMap()
    const val NBT_TAG = "executor_item_key"


    @JvmStatic
    fun getExecutorItem(itemStack: ItemStack): Pair<Boolean, ExecutorItem?> {
        val nbtItem = NBTItem(itemStack)
        if (nbtItem.hasNBTData() && nbtItem.hasKey(NBT_TAG)) {
            val key = nbtItem.getString(NBT_TAG)
            if (key != null) {
                val executorItem = executorItemMap[key]
                return if (executorItem != null) {
                    Pair(true, executorItem)
                } else Pair(true, null)
            }
        }
        return Pair(false, null)
    }

    @JvmStatic
    fun enter(player: Player, action: InteractAction, itemStack: ItemStack?) {
        if (itemStack == null || itemStack.type == Material.AIR) return
        val executorItem = getExecutorItem(itemStack)
        if (!executorItem.key) {
            //  event.getPlayer().sendMessage("return Enter");
            return
        }
        executorItem.value?.execute(action, player)
    }

    @JvmStatic
    fun addItem(item: ExecutorItem) {
        executorItemMap[item.key] = item
    }

    @JvmStatic
    fun delItem(item: ExecutorItem) {
        executorItemMap.remove(item.key)
    }

    @JvmStatic
    fun read(section: ConfigurationSection): ExecutorItem {
        val item = ExecutorItemConfigWrapper(section).build()
        addItem(item)
        return item
    }

}