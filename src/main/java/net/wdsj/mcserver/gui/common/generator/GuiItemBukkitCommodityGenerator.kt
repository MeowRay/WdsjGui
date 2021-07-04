package net.wdsj.mcserver.gui.common.generator

import mc233.cn.wdsjlib.bukkit.WdsjLib
import mc233.cn.wdsjlib.bukkit.utils.extensions.sendMessage
import mc233.cn.wdsjlib.global.api.eco.EcoPointsHandler
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuShopAmount
import net.wdsj.mcserver.gui.common.utils.ShopItemStackAmountPreCheck
import net.wdsj.servercore.message.ServerMessages
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author  MeowRay
 * @date  2021/7/1 16:49
 * @version 1.0
 */
object GuiItemItemStackCommodityGenerator :
    GuiItemCommodityGenerator<GuiMenuShopAmount.Commodity<ItemStack>, Player, ItemStack>(
        {
            ItemCommonBuilder.createFromBukkitItemStack(it.value)
        },
        successExecutor = { h, c, i ->
            val apply = c.value.clone().apply { amount *= i }
            h.inventory.addItem(apply)
        },
        preCheckExecutor = GuiItemBukkitCommodityGenerator.ITEM_PRE_CHECK_EXECUTOR,
        failureExecutor = GuiItemBukkitCommodityGenerator.FAILURE_EXECUTOR
    )


object GuiItemBukkitCommodityGenerator {

    @JvmStatic
    val FAILURE_EXECUTOR = { player: Player, c: GuiMenuShopAmount.Commodity<*>, _: Int ->
        if (c.ecoHandler is EcoPointsHandler) {
            WdsjLib.getInstance().titleAPI.sendTitle(player, "§c" + c.ecoHandler.displayName + "不足!")
            player.sendMessage(ServerMessages.Component.TAKE_POINT_FAIL)
        } else {
            player.sendMessage("§c" + c.ecoHandler.displayName + "不足!")
        }
    }
    val ITEM_PRE_CHECK_EXECUTOR = { player: Player, c: GuiMenuShopAmount.Commodity<ItemStack>, i: Int ->
        ShopItemStackAmountPreCheck<GuiMenuShopAmount.Commodity<ItemStack>> { listOf(it.value) }.preCheck(player, c, i)
    }

}