package net.wdsj.mcserver.gui.common.utils

import net.wdsj.common.simpleconfig.ConfigurationSection
import net.wdsj.common.simpleconfig.file.YamlConfiguration
import net.wdsj.servercore.config.invoke.ConfigInvoke
import java.util.ArrayList

/**
 * @author  Arthur
 * @date  2020/10/29 20:45
 * @version 1.0
 */
object Utils {

    fun <C> invokeConfig(clazz: Class<C>, config: ConfigurationSection): C = ConfigInvoke.invoke(clazz, config)

    fun createSectionByMap(map: Map<String, Any>): ConfigurationSection  =  YamlConfiguration().createSection(
        "temp",
        map
    );

    fun createSectionByList(list: List<Any>, root: String): ConfigurationSection = YamlConfiguration().createSection(
        "temp", mapOf(
            root to list
        )
    )


    fun getInventoryRange(minX: Int, minY: Int, maxX: Int, maxY: Int): List<Int> {
        var minX2 = minX
        var maxY2 = maxY
        var minY2: Int
        val list: MutableList<Int> = ArrayList()
        maxY2 = Math.max(maxY2, minY)
        minY2 = Math.min(maxY2, minY)

        minX2--
        for (i in minY2..maxY2) {
            val minx = if (i == 1) minX2 else minX2 + 9 * (i - 1)
            val maxx = if (i == 1) maxX else maxX + 9 * (i - 1)
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