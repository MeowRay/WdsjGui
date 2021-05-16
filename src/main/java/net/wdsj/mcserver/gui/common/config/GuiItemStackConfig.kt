package net.wdsj.mcserver.gui.common.config

import mc233.cn.wdsjlib.global.config.ItemStackConfig
import net.wdsj.servercore.common.placeholder.PlaceholderManager
import net.wdsj.servercore.compatible.XMaterial
import net.wdsj.servercore.utils.extensions.isArmor
import net.wdsj.servercore.utils.extensions.isPotion
import net.wdsj.servercore.utils.extensions.isTool
import net.wdsj.servercore.utils.extensions.isWeapon
import java.util.stream.Collectors

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:54
 */
//TODO SHIT, AUTO CONVERT TO KOTLIN
class GuiItemStackConfig {
    public var id: String? = null
    public var material: String? = null
    public var durability: String? = null
    public var amount: String? = null
    public var name: String? = null
    public var lore: ArrayList<String> = ArrayList()
    public val format = true
    public var head: String? = null
    public var house: String? = null
    public var skin: String? = null
    public var enchant: ArrayList<String> = ArrayList()
    public var flag: ArrayList<String> = ArrayList()

    fun build(handler: Any, map: Map<String, String?>?): ItemStackConfig {
        val itemStackConfig = ItemStackConfig()
        map?.let {
            for ((key, value) in it) {
                val foo = "$$key"
                if (id != null) id = id!!.replace(foo, value!!)
                if (material != null) material = material!!.replace(foo, value!!)
                if (durability != null) durability = durability!!.replace(foo, value!!)
                if (amount != null) amount = amount!!.replace(foo, value!!)
                if (name != null) name = name!!.replace(foo, value!!)
                if (head != null) head = PlaceholderManager.replace(handler, head!!.replace(foo, value!!))
                if (house != null) house = house!!.replace(foo, value!!)
                if (skin != null) {
                    skin = PlaceholderManager.replace(handler, skin!!.replace(foo, value!!))
                }

                lore = lore.stream().map { s: String -> s.replace(foo, value!!) }
                    .collect(Collectors.toList()) as ArrayList<String>

                enchant = enchant.stream().map { s: String -> s.replace(foo, value!!) }
                    .collect(Collectors.toList()) as ArrayList<String>

                flag = flag.stream().map { s: String -> s.replace(foo, value!!) }
                    .collect(Collectors.toList()) as ArrayList<String>
            }

        }
        if (format) {
            material?.let {
                val xMaterial = XMaterial.matchXMaterial(it)
                if (xMaterial.isPresent) {
                    val v = xMaterial.get()
                    flag.add("HIDE_ENCHANTMENTS")
                    if (v.isWeapon() || v.isArmor() || v.isTool()) {
                        flag.add("HIDE_ATTRIBUTES")
                    }
                    if (v.isPotion()){
                        flag.add("HIDE_POTION_EFFECTS")
                    }
                }
            }
        }
        itemStackConfig.id = if (id == null) null else id!!.toInt()
        itemStackConfig.material = if (material == null) null else material
        itemStackConfig.durability = if (durability == null) null else durability!!.toInt()
        itemStackConfig.amount = if (amount == null) null else amount!!.toInt()
        itemStackConfig.name = if (name == null) null else name
        itemStackConfig.head = if (head == null) null else head
        itemStackConfig.house = if (house == null) null else house
        itemStackConfig.skin = if (skin == null) null else skin
        itemStackConfig.lore = lore
        itemStackConfig.enchant = enchant
        itemStackConfig.flag = flag
        return itemStackConfig
    }
}