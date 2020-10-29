package net.wdsj.mcserver.gui.common.utils

import net.wdsj.common.simpleconfig.ConfigurationSection
import net.wdsj.common.simpleconfig.file.YamlConfiguration
import net.wdsj.servercore.config.invoke.ConfigInvoke

/**
 * @author  Arthur
 * @date  2020/10/29 20:45
 * @version 1.0
 */
object Utils {

    fun <C> invokeConfig(clazz: Class<C>, config: ConfigurationSection): C = ConfigInvoke.invoke(clazz, config)

    fun createSectionByMap(map : Map<String, Any> ): ConfigurationSection  =  YamlConfiguration().createSection("temp" , map);

    fun createSectionByList(list : List<Any> , root : String ): ConfigurationSection = YamlConfiguration().createSection("temp" , mapOf(root to list))

}