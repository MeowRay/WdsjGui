package net.wdsj.mcserver.gui.common.utils

import com.meowray.common.KOVColumnEntity
import com.meowray.common.KOVHandlerCache
import mc233.cn.wdsjlib.bukkit.utils.extensions.getLangMessage
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.common.executor.GuiMenuItemExecutor
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu
import net.wdsj.mcserver.gui.common.item.GuiItemBase
import net.wdsj.mcserver.gui.common.item.GuiItemCommon
import net.wdsj.mcserver.langutils.lang.convert.EnumLang
import net.wdsj.servercore.compatible.XMaterial
import net.wdsj.servercore.eunm.inventory.InventoryAction
import org.bukkit.entity.Player
import kotlin.reflect.KClass

/**
 * @author  MeowRay
 * @date  2021/4/27 13:02
 * @version 1.0
 */
class KOVStatusUtils {

    companion object{
        val INSTANCE = KOVStatusUtils()

        init {
            INSTANCE.registerTypeControl(Boolean::class,  object : ItemControl<Any, Boolean> {
                override fun exec(
                    guiitem: GuiItemCommon<*, *>,
                    builder: ItemCommonBuilder,
                    kov: KOVHandlerCache<*>,
                    kovEntity: KOVColumnEntity<Boolean>,
                    handler: Any,
                    value: Boolean
                ) {
                    if (handler is Player) {
                        builder.addLore(handler.getLangMessage("global.type.boolean.${value.toString()}"))
                    }else{
                        builder.addLore(EnumLang.ZH_CN.getLocal("global.type.boolean.${value.toString()}", emptyMap()) ?: "$value")
                    }

                    val executor: GuiMenuItemExecutor<Any , Any>
                    executor = object : GuiMenuItemExecutor<Any , Any>{
                        override fun execute(menu: GuiMenu<Any, Any>, handler: Any?) {
                            kov.set(kovEntity, !kov.get(kovEntity, false))
                        }
                    }
                    (guiitem as GuiItemCommon<Any, Any>).addActionExecutor(InventoryAction.LEFT , executor)
                }
            }
            )
        }


    }

    private val TYPE_MAP = mutableMapOf<KClass<*>, ItemControl<*, *>>()
    private val ITEM_MAP = mutableMapOf<KOVColumnEntity<*>, ItemCommonBuilder>()


    @Synchronized
    fun  registerTypeControl(kClass: KClass<*> , control: ItemControl<*,*>){
        TYPE_MAP[kClass] = control
    }

    @Synchronized
    fun unregisterTypeControl(kClass: KClass<*>) = TYPE_MAP.remove(kClass)

    @Synchronized
    fun registerItem(entity: KOVColumnEntity<out Any> , builder: ItemCommonBuilder) { ITEM_MAP[entity] = builder}

    @Synchronized
    fun unregisterItem(entity: KOVColumnEntity<out Any>) { ITEM_MAP.remove(entity)}



fun <H : Any , I:Any> getItemRender(handler : H , cache : KOVHandlerCache<*> ,entity : KOVColumnEntity<out Any>) : GuiItemBase<H, I> {
    val value = cache.get(entity, false)
    val builder: ItemCommonBuilder =
        (ITEM_MAP[  entity] ?: ItemCommonBuilder(XMaterial.STONE).apply { display = "NO DEFINE" }).clone()


    val itemCommon = GuiItemCommon<H, I>(builder)
    value.run {
        if (this != null) {
            builder.run {
                if (handler is Player) {
                    addLore("§7" + handler.getLangMessage("global.status"))
                }
            }
            val itemControl: ItemControl<Any, Any>? = TYPE_MAP[this::class] as ItemControl<Any, Any>?
            itemControl?.exec(
                itemCommon,
                builder,
                cache,
                kovEntity = entity as KOVColumnEntity<Any>,
                handler = handler,
                value = this
            )
        }
    }
    return itemCommon
}

}

interface ItemControl<H, V> {
    fun exec(
        guiitem: GuiItemCommon<*, *>,
        builder: ItemCommonBuilder,
        kov: KOVHandlerCache<*>,
        kovEntity: KOVColumnEntity<V>,
        handler: H,
        value: V
    )

}