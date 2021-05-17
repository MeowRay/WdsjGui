package net.wdsj.mcserver.gui.common.utils

import mc233.cn.wdsjlib.bukkit.WdsjLib
import mc233.cn.wdsjlib.bukkit.utils.PlayerUtils
import mc233.cn.wdsjlib.bukkit.utils.extensions.getCompatibleStorageContents
import net.wdsj.common.simpleconfig.ConfigurationSection
import net.wdsj.common.simpleconfig.file.YamlConfiguration
import net.wdsj.mcserver.gui.common.gui.menu.ShopAmountPreChecker
import net.wdsj.servercore.config.invoke.ConfigInvoke
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author  Arthur
 * @date  2020/10/29 20:45
 * @version 1.0
 */
object Utils {

    fun <C> invokeConfig(clazz: Class<C>, config: ConfigurationSection): C = ConfigInvoke.invoke(clazz, config)

    fun createSectionByMap(map: Map<String, Any>): ConfigurationSection = YamlConfiguration().createSection(
        "temp",
        map
    );

    fun createSectionByList(list: List<Any>, root: String): ConfigurationSection = YamlConfiguration().createSection(
        "temp", mapOf(
            root to list
        )
    )


    fun getInventoryRange(minY: Int, minX: Int, maxY: Int, maxX: Int): List<Int> {
        val minX2 = minX - 1
        val maxX2 = maxX - 1
        var maxY2 = maxY
        var minY2: Int
        val list: MutableList<Int> = ArrayList()
        maxY2 = Math.max(maxY2, minY)
        minY2 = Math.min(maxY2, minY)


        for (i in minY2..maxY2) {
            val minx = if (i == 1) minX2 else minX2 + 9 * (i - 1)
            val maxx = if (i == 1) maxX2 else maxX2 + 9 * (i - 1)
            list.addAll(getRange(minx, maxx))
        }
        return list
    }

    fun getRange(start: Int, end: Int): List<Int> {
        val list: MutableList<Int> = ArrayList()
        for (i in start..end) {
            list.add(i)
        }
        return list
    }


}

open class ShopItemStackAmountPreCheck<T >(val getter: (T) -> Collection<ItemStack>) :
    ShopAmountPreChecker<Player, T> {

    override fun preCheck(handler: Player, commodity: T, amount: Int): Boolean {
        val itemstack = getter(commodity)
        val result = PlayerUtils.isIterableItemStackFree(
            handler.inventory.getCompatibleStorageContents().toList(), itemstack.associateBy({ it },
                { it.amount * amount }
            )) >= 0
        if (!result) {
            WdsjLib.getInstance().titleAPI.sendTitles(handler, "§c§l背包空间不足", "§7请清理背包后再试!")
        }
        return result
    }


}