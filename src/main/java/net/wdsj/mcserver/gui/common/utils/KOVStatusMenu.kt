package net.wdsj.mcserver.gui.common.utils

import com.google.common.collect.ImmutableList
import com.meowray.common.KOVColumnEntity
import com.meowray.common.KOVHandlerCache
import mc233.cn.wdsjlib.bukkit.gui.CustomGuiManager
import mc233.cn.wdsjlib.bukkit.utils.extensions.getLangMessage
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.common.GuiManager
import net.wdsj.mcserver.gui.common.component.page.GuiMenuPage
import net.wdsj.mcserver.gui.common.executor.GuiMenuItemExecutor
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic
import net.wdsj.mcserver.gui.common.item.GuiItemBase
import net.wdsj.mcserver.gui.common.item.GuiItemCommon
import net.wdsj.mcserver.gui.common.utils.TemplateUtils.Companion.setLastBackButton
import net.wdsj.mcserver.gui.common.utils.TemplateUtils.Companion.setUnderEvenlyByPageButton
import net.wdsj.mcserver.langutils.lang.convert.EnumLang
import net.wdsj.servercore.WdsjServerAPI
import net.wdsj.servercore.compatible.XEnchantment
import net.wdsj.servercore.compatible.XItemFlag
import net.wdsj.servercore.compatible.XMaterial
import net.wdsj.servercore.eunm.inventory.InventoryAction
import net.wdsj.servercore.eunm.inventory.InventoryType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

/**
 * @author  MeowRay
 * @date  2021/4/27 13:02
 * @version 1.0
 */
open class StatusMenu<O : Any> @JvmOverloads constructor(
    owner: O,
    val kovC: KOVHandlerCache<Long>,
    val list: List<KOVColumnEntity<*>>,
    val manager: KOVStatusGuiManager = KOVStatusGuiManager.INSTANCE,
    title: String = CustomGuiManager.getTitleDomainPrefix() + "设定"
) :
    GuiMenuStatic<O, ItemStack>(
        owner,
        if (list.size > 12) InventoryType.GENERIC_9X6 else InventoryType.GENERIC_9X5,
        title
    ) {

    init {

        layoutInitialization()

    }

    open fun layoutInitialization() {
        val i = inventoryType.size / 9
        val page = Page(this, ImmutableList.copyOf(getFreeSlot(2, 2, 9, i - 1)))

        page.loadPage(1)
        page.setUnderEvenlyByPageButton()
        setLastBackButton()
    }

}

class Page<O : Any>(
    val menu: StatusMenu<O>,
    val container: ImmutableList<Int> = ImmutableList.copyOf(
        menu.getFreeSlot(
            0,
            menu.inventoryType.size - 9
        )
    )
) :
    GuiMenuPage<KOVColumnEntity<*>, O, ItemStack>(menu, 48, 50) {


    override fun getContent(p0: Int): KOVColumnEntity<*> {
        return menu.list[p0]
    }

    override fun getContentSize(): Int {
        return menu.list.size
    }

    override fun getItemRender(slot: Int, entity: KOVColumnEntity<*>): GuiItemBase<O, ItemStack> {
        val itemRender = menu.manager.getItemRender<O, ItemStack>(
            menu.owner, menu.kovC,
            entity as KOVColumnEntity<out Any>
        )
        return itemRender.apply {
            addActionExecutor(InventoryAction.LEFT) {
                menu.setItem(slot, getItemRender(slot, entity))
                GuiManager.refreshGuiMenuRender(menu.owner, menu)
            }
        }
    }

    override fun getFreeSlots(): ImmutableList<Int> {
        return container
    }

}

class KOVStatusChangeEvent<H : Any, T : Any>(
    val handler: H,
    val entity: KOVColumnEntity<T>,
    val oldValue: T,
    var newValue: T,
    var cancel: Boolean = false
) {

}

class KOVStatusGuiManager {

    companion object {
        @JvmField
        val INSTANCE = KOVStatusGuiManager()

        @JvmField
        val BOOLEAN_CONTROL = object : ItemControl<Any, Boolean> {
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
                } else {
                    builder.addLore(
                        EnumLang.ZH_CN.getLocal("global.type.boolean.${value.toString()}", emptyMap()) ?: "$value"
                    )
                }

                val executor: GuiMenuItemExecutor<Any, Any>
                executor = object : GuiMenuItemExecutor<Any, Any> {
                    override fun execute(menu: GuiMenu<Any, Any>, handler: Any) {
                        val old = kov.get(kovEntity, false)
                        val event = KOVStatusChangeEvent(handler, kovEntity, old, !old)
                        WdsjServerAPI.getEventBus().post(event)
                        if (event.cancel.not()){
                            kov.set(kovEntity, event.newValue)
                        }
                    }
                }
                (guiitem as GuiItemCommon<Any, Any>).addActionExecutor(InventoryAction.LEFT, executor)
            }
        }

        init {
            INSTANCE.registerTypeControl(
                Boolean::class,
                BOOLEAN_CONTROL
            )
        }


    }

    private val TYPE_MAP = mutableMapOf<KClass<*>, ItemControl<*, *>>()
    private val ITEM_MAP = mutableMapOf<KOVColumnEntity<*>, KOVGuiItemRender<Any, Any>>()


    @Synchronized
    fun registerTypeControl(kClass: KClass<*>, control: ItemControl<*, *>) {
        TYPE_MAP[kClass] = control
    }

    @Synchronized
    fun unregisterTypeControl(kClass: KClass<*>) = TYPE_MAP.remove(kClass)

    @Synchronized
    fun <T : Any, H : Any> registerItem(entity: KOVColumnEntity<out T>, builder: KOVGuiItemRender<T, H>) {
        ITEM_MAP[entity] = builder as KOVGuiItemRender<Any, Any>
    }

    @Synchronized
    fun unregisterItem(entity: KOVColumnEntity<out Any>) {
        ITEM_MAP.remove(entity)
    }


    fun <H : Any, I : Any> getItemRender(
        handler: H,
        cache: KOVHandlerCache<*>,
        entity: KOVColumnEntity<out Any>
    ): GuiItemBase<H, I> {
        val value: Any
        value = cache.get(entity, false)
        val function =
            ITEM_MAP[entity] ?: { a, b -> ItemCommonBuilder(XMaterial.STONE).apply { display = "NO DEFINE" } }
        val builder = function(value, handler)


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

typealias KOVGuiItemRender <T, H> = (T, H) -> ItemCommonBuilder

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

object KOVStatusMenuUtils {

    @JvmStatic
    fun createBaseBooleanItemRender(
        builder: ItemCommonBuilder,
        format: String
    ): KOVGuiItemRender<Boolean, Any> {
        return createBaseBooleanItemRender(builder, builder, format)
    }

    @JvmStatic
    fun createBaseBooleanItemRender(
        onBuilder: ItemCommonBuilder,
        offBuilder: ItemCommonBuilder,
        format: String
    ): KOVGuiItemRender<Boolean, Any> {
        return { s, _ ->
            (if (s) onBuilder else offBuilder).clone().apply {
                if (s) {
                    addLore(format.format("启用"))
                    addEnchant(XEnchantment.DURABILITY, 1)
                    addItemFlag(XItemFlag.HIDE_ENCHANTMENTS)
                    addItemFlag(XItemFlag.HIDE_ATTRIBUTES)
                } else {
                    addLore(format.format("禁用"))
                }
            }

        }
    }

}