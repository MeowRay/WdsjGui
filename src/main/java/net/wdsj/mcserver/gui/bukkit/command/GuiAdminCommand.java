package net.wdsj.mcserver.gui.bukkit.command;

import mc233.cn.wdsjlib.bukkit.utils.BukkitUtils;
import mc233.cn.wdsjlib.bukkit.utils.ItemBuilder;
import mc233.cn.wdsjlib.global.api.eco.EcoAPI;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.bukkit.item.GuiItemBukkit;
import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.GuiFactory;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderDynamicItem;
import net.wdsj.mcserver.gui.common.render.GuiMenuRenderItem;
import net.wdsj.mcserver.gui.common.render.RenderItem;
import net.wdsj.mcserver.gui.common.wrapper.GuiMenuConfigWrapper;
import net.wdsj.mcserver.gui.common.wrapper.GuiSignConfigWrapper;
import net.wdsj.servercore.common.SimpleParamParse;
import net.wdsj.servercore.common.command.WdsjCommand;
import net.wdsj.servercore.common.command.anntations.Arg;
import net.wdsj.servercore.common.command.anntations.GlobalCommand;
import net.wdsj.servercore.common.command.anntations.SubCommand;
import net.wdsj.servercore.compatible.XMaterial;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.servercore.eunm.inventory.InventoryType;
import net.wdsj.servercore.interfaces.Executor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/20 22:15
 */
@GlobalCommand(permission = "guimenu.admin")
public class GuiAdminCommand implements WdsjCommand<CommandSender> {

    @SubCommand(help = "testv2")
    public void test(Player player) {
        final GuiMenu<Player, ItemStack> guiMenu = GuiFactory.createBukkitPacketGuiMenu(player, InventoryType.GENERIC_9X4, "test");
        guiMenu.addRenderItem(new GuiMenuRenderDynamicItem<>(2, new RenderItem<>(0, new GuiItemBukkit(new ItemBuilder(XMaterial.STONE).name("§c测试图标")).addActionExecutor(InventoryAction.ALL, new Executor<Player>() {
                    @Override
                    public void execute(Player player) {
                        player.sendMessage("clicked!");
                    }
                })), new RenderItem<>(1, new GuiItemBukkit(new ItemBuilder(XMaterial.STONE).name("§c测试图标")).addActionExecutor(InventoryAction.ALL, new Executor<Player>() {
                    @Override
                    public void execute(Player player) {
                        player.sendMessage("clicked!");
                    }
                })))
        );
        guiMenu.addRenderItem(new GuiMenuRenderItem<>(new RenderItem<>(3, new GuiItemBukkit(new ItemBuilder(XMaterial.STONE).name("§c购买物品")).addActionExecutor(InventoryAction.ALL, new Executor<Player>() {
            @Override
            public void execute(Player player) {
                player.sendMessage(String.valueOf(EcoAPI.getInstance().take(player.getName(), 10, "POINT", null, "")));
            }
        }))));

        GuiManager.open(player, guiMenu);
    }

    @SubCommand(async = true)
    public void openMenu(CommandSender sender, @Arg(name = "menu") String menu, @Arg(required = false, name = "player") String player) {
        GuiMenuConfigWrapper guiMenu = GuiConfigManager.getGuiMenu(menu, true);
        if (guiMenu != null) {
            Player playerExact = player == null && sender instanceof Player ? (Player) sender : Bukkit.getPlayerExact(player);
            if (BukkitUtils.isOnlinePlayer(playerExact)) {
               // Stopwatch stopwatch = Stopwatch.createStarted();
                guiMenu.open(GuiFactory.GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER, playerExact);
            //    playerExact.sendMessage(stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
            }
        } else {
            sendMessage(sender, menu + "不存在");
        }
    }

    @SubCommand(async = true)
    public void openSign(CommandSender sender, @Arg(name = "sign") String sign, String player, @Arg(required = false) String args) {
        GuiSignConfigWrapper guiSign = GuiConfigManager.getGuiSign(sign , true);
        if (guiSign != null) {
            Player playerExact = player == null && sender instanceof Player ? (Player) sender : Bukkit.getPlayerExact(player);
            if (BukkitUtils.isOnlinePlayer(playerExact)) {
               // Stopwatch stopwatch = Stopwatch.createStarted();
                if (args == null) args = "";
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, String> entry : SimpleParamParse.parse(args, ";", "=").getMap().entrySet()) {
                    map.put("$" + entry.getKey(), entry.getValue());
                }
                guiSign.open(playerExact, map);
           //   playerExact.sendMessage(stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
            }
        } else {
            sendMessage(sender, sign + "不存在");
        }
    }

    @SubCommand(async = true)
    public void reload(CommandSender sender) {
        GuiBukkit.getInstance().closeAll();
        GuiConfigManager.init();
        sender.sendMessage("reloaded!");
    }


}
