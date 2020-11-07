package net.wdsj.mcserver.gui.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import gnu.trove.map.hash.THashMap;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/11 16:48
 */
public class GuiManager {

    private final static Map<Object, GuiData<?>> guiMenuDataMap = new THashMap<>();

    private static GuiMenuFunction<?> GUI_MENU_FUNCTION;


    public static <Handler> void open(Handler handler, Gui<Handler> guiMenu) {
        GuiData<Handler> guiData = getGuiData(handler);
        guiData.open(guiMenu, guiMenu.open(handler));
    }

    public static <Handler> boolean close(Handler handler) {
        GuiData<Handler> guiData = getGuiData(handler);
        if (guiData.getNowOpen() != null) guiData.getNowOpen().close(handler, guiData.getWindowId());
        return guiData.close();
    }


    /**
     * @param handler
     * @param override 是否覆盖历史GUI，如是从其他GUI打开到这的则返回TRUE
     * @param i
     * @param <Handler>
     * @return
     */
    public static <Handler> boolean openHistory(Handler handler, boolean override, int i) {
        if (override && i < 1) i = 2;
        GuiData guiData = GuiManager.getGuiData(handler);
        if (!override || guiData.isOpenOverride()) {
            Gui history = guiData.removeHistory(GuiMenu.class, i);
            if (history != null) {
                if (override)
                    guiData.removeHistory(GuiMenu.class, i - 1);
                GuiManager.open(handler, history);
                return true;
            }
        }
        return false;
    }

    public static <Handler, T extends Gui<Handler>> List<GuiData<Handler>> getGuiActiveDataHandlers(T gui) {
        List<GuiData<Handler>> handlers = new ArrayList<>();
        for (GuiData<?> value : guiMenuDataMap.values()) {
            if (value.getNowOpen() == gui) {
                handlers.add((GuiData<Handler>) value);
            }
        }
        return handlers;
    }

    public static <Handler> void closeAll(Gui<Handler> gui) {
        for (GuiData<Handler> menuActiveHandler : getGuiActiveDataHandlers(gui)) {
            close(menuActiveHandler.getHandler());
        }
    }


    public static <Handler, Item> void refreshAllGuiMenuRender(long serverTick) {
        Multimap<GuiMenu<Handler, Item>, GuiData<Handler>> multimap = HashMultimap.create();
        for (GuiData<?> value : guiMenuDataMap.values()) {
            if (value.getNowOpen() instanceof GuiMenu) {
                multimap.put((GuiMenu<Handler, Item>) value.getNowOpen(), (GuiData<Handler>) value);
            }
        }
        for (GuiMenu<Handler, Item> guiMenu : multimap.keySet()) {
            if (guiMenu.refreshRenderContent(serverTick)) {
                boolean first = true;
                for (GuiData<Handler> handler : multimap.get(guiMenu)) {
                    if (first) {
                        guiMenu.renderAdapter().reBuild(handler.getHandler(), guiMenu);
                        first = false;
                    }
                    guiMenu.renderAdapter().render(handler.getHandler(), handler.getWindowId(), guiMenu);
                }
            }
        }
    }

    public static <Handler, Item> void refreshGuiMenuRender(Handler handler, GuiMenu<Handler, Item> guiMenu) {
        GuiData<Handler> guiData = getGuiData(handler);
        if (guiData.getNowOpen() == guiMenu) {
            guiMenu.refreshRenderContent(0, true);
            guiMenu.renderAdapter().reBuild(guiData.getHandler(), guiMenu);
            guiMenu.renderAdapter().render(guiData.getHandler(), guiData.getWindowId(), guiMenu);
        }
    }

    public static Collection<GuiData<?>> getAllGuiMenuData() {
        return guiMenuDataMap.values();
    }


    public static <Handler> GuiData<Handler> getGuiData(Handler handler) {
        return (GuiData<Handler>) guiMenuDataMap.computeIfAbsent(handler, e -> new GuiData<>(handler));
    }

    public static <Handler> void removeGuiData(Handler handler) {
        guiMenuDataMap.remove(handler);
    }

    public static int applicationWindowId() {
        windowIdCache++;
        return windowIdCache;
    }


    public static void setGuiMenuFunction(GuiMenuFunction<?> function) {
        GUI_MENU_FUNCTION = function;
    }

    public static GuiMenuFunction getGuiMenuFunction() {
        return GUI_MENU_FUNCTION;
    }

    private static int windowIdCache = 1000000000;

}
