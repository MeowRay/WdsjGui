package net.wdsj.mcserver.gui.bukkit.model.menu

import mc233.cn.wdsjlib.bukkit.gui.CustomGuiManager
import mc233.cn.wdsjlib.bukkit.repository.HeadRepository
import mc233.cn.wdsjlib.global.common.itemstack.ItemCommonBuilder
import net.wdsj.mcserver.gui.bukkit.executor.GuiItemBukkitCloseExecutor
import net.wdsj.mcserver.gui.bukkit.executor.GuiItemBukkitCommandExecutor
import net.wdsj.mcserver.gui.common.builder.GuiItemRenderBuilder
import net.wdsj.mcserver.gui.common.container.GuiMenuItemContainer
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic
import net.wdsj.mcserver.gui.common.item.GuiItemCommon
import net.wdsj.mcserver.gui.common.utils.TemplateUtils.Companion.setLastBackButton
import net.wdsj.servercore.eunm.inventory.InventoryAction
import net.wdsj.servercore.eunm.inventory.InventoryType
import net.wdsj.servercore.utils.extensions.replacePlaceholder
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/11/7 14:25
 */
class PlayerInfoMenu(owner: Player, target: String) : GuiMenuStatic<Player, ItemStack>(
    owner,
    InventoryType.GENERIC_9X3,
    CustomGuiManager.getTitleDomainPrefix() + target
) {
    init {
        setItem(
            0,
            GuiItemCommon(
                ItemCommonBuilder.createHeadFromPlayerSkin(target)
                    .setDisplay("§e$target").apply {
                        addLore("§8玩家管理")
                        addLore("§fID: §7$target")
                        val prefix = "%object_o:$target;p:prefix_now%".replacePlaceholder(null)
                        if (prefix.isNotBlank()) {
                            addLore("§f称号: $prefix")
                        }
                        addLore("§f段位: %object_o:$target;p:scoreprefix_prefix%".replacePlaceholder(null))
                    }
            )
        )
        val container = GuiMenuItemContainer<Player, ItemStack>(getFreeSlot(2, 2, 8, 2))
        container.addItem(
            GuiItemRenderBuilder(
                GuiItemCommon<Player, ItemStack>(
                    ItemCommonBuilder.createHeadFromRepo(
                        HeadRepository.PARTY.key
                    ).setDisplay("§e请求组队").addLore("§7将发送组队请求给对方!")
                )
                    .addActionExecutor(
                        InventoryAction.LEFT,
                        GuiItemBukkitCommandExecutor("console:bungeeplayercommand ${owner.name} zd yq ${target}")
                    )
                    .addActionExecutor(InventoryAction.LEFT, GuiItemBukkitCloseExecutor())
            )
        )
        container.addItem(
            GuiItemRenderBuilder(
                GuiItemCommon<Player, ItemStack>(
                    ItemCommonBuilder.createHeadFromRepo(
                        HeadRepository.FRIEND.key
                    ).setDisplay("§e请求好友").addLore("§7将发送好友请求给对方!")
                )
                    .addActionExecutor(
                        InventoryAction.LEFT,
                        GuiItemBukkitCommandExecutor("console:bungeeplayercommand ${owner.name} hy tj ${target}")
                    )
                    .addActionExecutor(InventoryAction.LEFT, GuiItemBukkitCloseExecutor())
            )
        )
        container.addItem(
            GuiItemRenderBuilder(
                GuiItemCommon<Player, ItemStack>(
                    ItemCommonBuilder.createHeadFromRepo(
                        HeadRepository.CLAN.key
                    ).setDisplay("§e邀请入会").addLore("§7将发送入会请求给对方!", "", "§c你需要拥有公会并且拥有邀请权限才能使用该功能")
                )
                    .addActionExecutor(
                        InventoryAction.LEFT,
                        GuiItemBukkitCommandExecutor("console:bungeeplayercommand ${owner.name} ghgl yq ${target}")
                    )
                    .addActionExecutor(InventoryAction.LEFT, GuiItemBukkitCloseExecutor())
            )
        )
        container.addItem(
            GuiItemRenderBuilder(
                GuiItemCommon<Player, ItemStack>(
                    ItemCommonBuilder.createHeadFromRepo(
                        HeadRepository.MARRY.key
                    ).setDisplay("§e求婚").addLore("§7将发送求婚请求给对方!", "", "§c你需要拥有昵称才能使用该功能")
                )
                    .addActionExecutor(
                        InventoryAction.LEFT,
                        GuiItemBukkitCommandExecutor("console:bungeeplayercommand ${owner.name} jh qh ${target}")
                    )
                    .addActionExecutor(InventoryAction.LEFT, GuiItemBukkitCloseExecutor())
            )
        )
        container.addItem(
            GuiItemRenderBuilder(
                GuiItemCommon<Player, ItemStack>(
                    ItemCommonBuilder.createHeadFromRepo(
                        "Gray R"
                    ).setDisplay("§c举报").addLore("§7如对方存在使用作弊的行为，输入/jb指令举报！")
                )
                    .addActionExecutor(
                        InventoryAction.LEFT,
                        GuiItemBukkitCommandExecutor("console:bungeeplayercommand ${owner.name} jb")
                    )
                    .addActionExecutor(InventoryAction.LEFT, GuiItemBukkitCloseExecutor())
            )
        )
        container.build(this)
        setLastBackButton()
    }
}