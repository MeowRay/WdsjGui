package net.wdsj.mcserver.gui.common.generator

import mc233.cn.wdsjlib.bukkit.gui.CustomGuiManager
import mc233.cn.wdsjlib.global.api.eco.EcoAPI
import mc233.cn.wdsjlib.global.api.eco.EcoPointsHandler
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.common.GuiManager
import net.wdsj.mcserver.gui.common.executor.GuiSignExecutor
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuShopAmount
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic
import net.wdsj.mcserver.gui.common.gui.sign.GuiSign
import net.wdsj.mcserver.gui.common.item.GuiItemBase
import net.wdsj.mcserver.gui.common.item.GuiItemCommon
import net.wdsj.servercore.compatible.XMaterial
import net.wdsj.servercore.eunm.inventory.InventoryAction
import net.wdsj.servercore.eunm.inventory.InventoryType
import net.wdsj.servercore.message.ServerMessages
import net.wdsj.servercore.utils.MinecraftUtils
import org.apache.commons.lang3.math.NumberUtils

/**
 * @author  MeowRay
 * @date  2021/7/1 16:24
 * @version 1.0
 */

open class GuiItemCommodityGenerator<C : GuiMenuShopAmount.Commodity<*>, H, I>(
    val commodityView: (C) -> ItemCommonBuilder,
    var successExecutor: CommodityExecutor<H, C>,
    var failureExecutor: CommodityExecutor<H, C> = { h, c, i ->
        if (c.ecoHandler is EcoPointsHandler) {
            MinecraftUtils.sendTextComponentMessage(h, ServerMessages.Component.TAKE_POINT_FAIL)
        } else {
            MinecraftUtils.sendMessage(h, "§c" + c.ecoHandler.displayName + "不足!")
        }
    },
    var preCheckExecutor: CommodityChecker<H, C> = { _, _, _ -> true },
    var doMaxLimit: CommodityExecutor<H, C> = { h, c, i ->
        MinecraftUtils.sendMessage(
            h,
            String.format("§c你一次性最多只能买 %s %s!", c.maxAmount, c.unit)
        )
    },
    var format : CommodityItemBuilderFormat<C> ={ i, c->
         i.addLore(
            "",
            "§e" + c.price.priceDisplay + "§6" + c.ecoHandler
                .displayName + " §b/" + c.unit
        )
    }
) {

    fun build(commodity: C): GuiItemBase<H, I> {
        return buildCommodity(format(commodityView(commodity), commodity), commodity)
    }

    private fun buildCommodity(builder: ItemCommonBuilder, commodity: C): GuiItemBase<H, I> {
        return GuiItemCommon<H, I>(builder).addActionExecutor(
            InventoryAction.LEFT
        ) { handler: H ->
            if (commodity.amounts.isEmpty()) {
                val guiSign: GuiSign<H> = GuiSign(handler,
                    arrayOf(
                        "在下方输入购买",
                        commodity.name + "的数量",
                        "" + commodity.defaultAmount,
                        commodity.unit
                    ),
                    GuiSignExecutor { handler1: H, lines: Array<String> ->
                        val amountStr = lines[2]
                        var success = false
                        if (amountStr.isNotEmpty()) {
                            if (NumberUtils.isNumber(amountStr)) {
                                val amount =
                                    NumberUtils.toInt(
                                        amountStr,
                                        commodity.defaultAmount
                                    )
                                if (amount > commodity.maxAmount) {
                                    doMaxLimit(handler1, commodity, amount)
                                } else if (amount <= 0) {
                                    MinecraftUtils.sendMessage(handler1, "§c购买数量不能小于1")
                                } else {
                                    if (preCheckExecutor(handler1, commodity, amount)) {
                                        success = buy(handler1, commodity, amount)
                                    }
                                }
                            }
                        }
                        false
                    })
                GuiManager.open(handler, guiSign)
            } else {
                val guiMenuStatic: GuiMenuStatic<H, I> = GuiMenuStatic<H, I>(
                    handler,
                    InventoryType.GENERIC_9X3,
                    CustomGuiManager.getTitleDomainPrefix() + "购买物品"
                )
                val freeSlot: List<Int> = guiMenuStatic.getFreeSlot(9, 17)
                val iterator = freeSlot.iterator()
                for (amount in commodity.amounts) {
                    if (iterator.hasNext()) {
                        guiMenuStatic.setItem(
                            iterator.next(),
                            GuiItemCommon<H, I>(
                                ItemCommonBuilder(XMaterial.PAPER).setDisplay(
                                    String.format(
                                        "§e购买%d%s%s",
                                        amount,
                                        commodity.unit,
                                        commodity.name
                                    )
                                ).setAmount(amount).addLore("", "§e左键确认购买")
                            )
                                .addActionExecutor(
                                    InventoryAction.LEFT
                                ) { h: H ->
                                    var success = false
                                    if (preCheckExecutor(h, commodity, amount)) {
                                        success = buy(handler, commodity, amount)
                                    }
                                }
                        )
                    } else {
                        break
                    }
                }
                GuiManager.open(handler, guiMenuStatic)
            }
        }
    }


    open fun buy(handler: H, commodity: C, amount: Int): Boolean {
        if (amount > 0) {
            if (EcoAPI.getInstance().take(
                    MinecraftUtils.getHandlerName(handler),
                    commodity.ecoHandler,
                    commodity.price,
                    amount,
                    commodity.servers,
                    String.format("buy %d amount: %s", amount, commodity.name)
                )
            ) {
                successExecutor(handler, commodity, amount)
                return true
            } else {
                failureExecutor(handler, commodity, amount)
            }
        }
        return false
    }




}

typealias CommodityItemBuilderFormat<C> = (ItemCommonBuilder, C) -> ItemCommonBuilder

typealias CommodityExecutor<H, C> = (H, C, Int) -> Unit

typealias CommodityChecker<H, C> = (H, C, Int) -> Boolean