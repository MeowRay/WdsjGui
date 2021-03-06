package net.wdsj.mcserver.gui.bukkit;

import com.comphenix.packetwrapper.WrapperPlayServerCloseWindow;
import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import mc233.cn.wdsjlib.bukkit.WdsjLib;
import mc233.cn.wdsjlib.bukkit.api.wrapper.SoundWrapper;
import mc233.cn.wdsjlib.bukkit.utils.BukkitUtils;
import mc233.cn.wdsjlib.global.api.eco.EcoAPI;
import mc233.cn.wdsjlib.global.config.ItemStackConfig;
import net.wdsj.mcserver.gui.bukkit.command.GuiAdminCommand;
import net.wdsj.mcserver.gui.bukkit.command.GuiLabelBukkitController;
import net.wdsj.mcserver.gui.bukkit.command.GuiPlayerCommand;
import net.wdsj.mcserver.gui.bukkit.creator.GuiItemBukkitConfigCreator;
import net.wdsj.mcserver.gui.bukkit.executor.*;
import net.wdsj.mcserver.gui.bukkit.listener.ExecutorItemListener;
import net.wdsj.mcserver.gui.bukkit.listener.GuiMenuBukkitEntityListener;
import net.wdsj.mcserver.gui.bukkit.listener.GuiMenuBukkitPacketListener;
import net.wdsj.mcserver.gui.bukkit.listener.GuiSignBukkitPacketListener;
import net.wdsj.mcserver.gui.common.*;
import net.wdsj.mcserver.gui.common.creator.GuiItemExecutorCreator;
import net.wdsj.mcserver.gui.common.creator.GuiSignExecutorCreator;
import net.wdsj.mcserver.gui.common.executor.GuiItemCommonOpenExecutor;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.servercore.common.command.CommandProxyBuilder;
import net.wdsj.servercore.compatible.XSound;
import net.wdsj.servercore.protocol.ProtocolVersion;
import net.wdsj.servercore.utils.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class GuiBukkit extends JavaPlugin {

    public static GuiBukkit getInstance() {
        return instance;
    }

    private static GuiBukkit instance;

    @Getter
    private static boolean defaultAsync = true;

    private static GuiMenuBukkitPacketListener guiMenuBukkitPacketListener;
    private static GuiSignBukkitPacketListener guiSignBukkitPacketListener;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        defaultAsync = WdsjServerAPI.getNmsService().getProtocolVersion().getId() <= ProtocolVersion.v1_12_2.getId();
        WdsjServerAPI.getPluginManager().registerCommand(CommandProxyBuilder.newBuilder(this, new GuiAdminCommand()).setLabel("guiadmin"));
        WdsjServerAPI.getPluginManager().registerCommand(CommandProxyBuilder.newBuilder(this, new GuiPlayerCommand()).setLabel("gui"));
        Bukkit.getPluginManager().registerEvents(new GuiMenuBukkitEntityListener() , this);

        guiMenuBukkitPacketListener = new GuiMenuBukkitPacketListener(this);
        guiSignBukkitPacketListener = new GuiSignBukkitPacketListener(this);


        WdsjLib.getInstance().registerPlayerQuitCleaner(this, lastPlaySoundMap::remove);
        init(this);
    }

    public void init(Plugin plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(guiMenuBukkitPacketListener);
        ProtocolLibrary.getProtocolManager().addPacketListener(guiSignBukkitPacketListener);

        GuiConfigManager.setLabelController(new GuiLabelBukkitController(this));

        WdsjLib.getInstance().registerPlayerQuitCleaner(plugin, (GuiManager::removeGuiData));

        final GuiMenuTask guiMenuTask = new GuiMenuTask(2);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, guiMenuTask, 20, 2);
        Bukkit.getPluginManager().registerEvents(new ExecutorItemListener(), this);

        //
        GuiConfigManager.registerItemCreator("BUKKIT", new GuiItemBukkitConfigCreator());

        //ITEM CREATOR
        GuiConfigManager.registerMenuExecutorCreator("menu", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemCommonOpenExecutor<>(args, GuiFactory.GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER));
        GuiConfigManager.registerMenuExecutorCreator("open", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemCommonOpenExecutor<>(args, GuiFactory.GUIMENU_RENDER_BUKKIT_PACKET_ADAPTER));
        GuiConfigManager.registerMenuExecutorCreator("close", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemBukkitCloseExecutor());
        GuiConfigManager.registerMenuExecutorCreator("msg", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemMessageExecutor(args));
        GuiConfigManager.registerMenuExecutorCreator("lang-msg", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemLanguageMessageExecutor(args));
        GuiConfigManager.registerMenuExecutorCreator("js", (GuiItemExecutorCreator<Player>) (args, argsMap) -> new GuiItemBukkitJavaScriptExecutor(args, argsMap));
        GuiConfigManager.registerMenuExecutorCreator("cmd", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemBukkitCommandExecutor(false, args));
        GuiConfigManager.registerMenuExecutorCreator("cmd-async", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemBukkitCommandExecutor(true, args));
        GuiConfigManager.registerMenuExecutorCreator("title", (GuiItemExecutorCreator<Player>) (args, map) -> new GuiItemBukkitTitleExecutor(args));
        GuiConfigManager.registerMenuExecutorCreator("sound", (GuiItemExecutorCreator<Player>) (args, map) -> {
            String[] split = args.split("-", 3);
            String n = ArrayUtils.get(split, 0);
            String v = ArrayUtils.get(split, 1);
            String p = ArrayUtils.get(split, 2);
            return new GuiItemBukkitSoundExecutor(XSound.matchXSound(n).orElse(XSound.BLOCK_NOTE_BLOCK_BANJO), v != null ? Float.parseFloat(v) : 1F, p != null ? Float.parseFloat(p) : 1F);
        });

        //SIGN CREATOR
        GuiConfigManager.registerSignExecutorCreator("command", (GuiSignExecutorCreator<Player>) args -> new GuiSignBukkitCommandExecutor(args, false));

        MenuUtils.putScriptObject("eco", EcoAPI.getInstance());
        MenuUtils.putScriptObject("title", WdsjLib.getInstance().getTitleAPI());
        MenuUtils.putScriptObject("bossbar", WdsjLib.getInstance().getBossBarAPI());

        MenuUtils.putScriptObject("itemconfig", ItemStackConfig.class);

        GuiConfigManager.init();

        GuiManager.setGuiMenuFunction((GuiMenuFunction<Player>) this::close);


        //   Bukkit.getScheduler().runTaskLater(this , ()->{
        //   } , 20);

    }

    public void closeAll() {
        for (GuiData<?> allGuiMenuDatum : GuiManager.getAllGuiMenuData()) {
            if (allGuiMenuDatum.getWindowId() != -1) {
                Object handler = allGuiMenuDatum.getHandler();
                if (handler instanceof Player) {
                    close((Player) handler, allGuiMenuDatum.getWindowId());
                }
            }
        }
    }

    public void close(Player player, int windowId) {
        if (!BukkitUtils.isOnlinePlayer(player)) return;
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow();
        if (windowId != -1) {
            closeWindow.setWindowId(windowId);
            closeWindow.sendPacket(player);
        }
    }

    private final Map<Player, Long> lastPlaySoundMap = new HashMap<>();

    public void playSound(Player player, SoundWrapper wrapper, boolean bypass) {
        if (bypass || System.currentTimeMillis() - lastPlaySoundMap.getOrDefault(player, 0L) > 400) {
            wrapper.play(player);
            lastPlaySoundMap.put(player, System.currentTimeMillis());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        closeAll();
    }
}
