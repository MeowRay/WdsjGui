package net.wdsj.mcserver.gui.common.extra;

import net.wdsj.common.simpleconfig.ConfigurationSection;
import net.wdsj.mcserver.gui.common.extra.viewexector.GuiItemViewExecutor;
import net.wdsj.mcserver.gui.common.extra.viewexector.GuiItemViewExecutorCaller;
import net.wdsj.mcserver.gui.common.extra.viewexector.GuiItemViewExecutorConfigCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/10 23:51
 */
public class GuiExtraManager {

    private static final Map<String, GuiItemViewExecutorCaller<?>> itemViewExecutorCallerMap = new HashMap<>();

    public static void registerItemCaller(String type, GuiItemViewExecutorCaller<?> caller) {
        itemViewExecutorCallerMap.put(type, caller);
    }


    public static void unregisterItemCreator(String type) {
        itemViewExecutorCallerMap.remove(type);
    }

    public static <T> GuiItemViewExecutor<T> createItem(String type, ConfigurationSection section) {
        GuiItemViewExecutorCaller<?> creator = itemViewExecutorCallerMap.get(type);
        if (creator instanceof GuiItemViewExecutorConfigCreator) {
            return ((GuiItemViewExecutorConfigCreator) creator).get(section);
        }
        return null;
    }

    public static <T> GuiItemViewExecutor<T> getItem(String type, String key) {
        GuiItemViewExecutorCaller guiItemViewExecutorCaller = itemViewExecutorCallerMap.get(type);
        if (guiItemViewExecutorCaller != null) {
            return guiItemViewExecutorCaller.get(key);
        }
        return null;
    }

}
