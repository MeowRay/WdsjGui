package net.wdsj.mcserver.gui.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import net.wdsj.mcserver.gui.common.event.GuiCloseEvent;
import net.wdsj.mcserver.gui.common.event.GuiOpenEvent;
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenu;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.servercore.utils.ClassUtils;
import net.wdsj.servercore.utils.ReflectionUtils;
import org.checkerframework.checker.units.qual.C;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/11 16:53
 */
public class GuiData<Handler> {

    private final Handler handler;
    @Setter
    private Gui<Handler> nowOpen;

    private int windowId;

    private final Map<Class<?>, List<Gui<Handler>>> history = new HashMap<>();
    private boolean openOverride = false;

    public GuiData(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public Gui<Handler> getNowOpen() {
        return nowOpen;
    }

    public int getWindowId() {
        return windowId;
    }


    public void open(Gui<Handler> guiMenu, int windowId) {
        openOverride = !(guiMenu instanceof GuiMenu) ||this.nowOpen != null;
        this.nowOpen = guiMenu;
        this.windowId = windowId;
        addHistory(nowOpen);
        WdsjServerAPI.getEventBus().post(new GuiOpenEvent<>(nowOpen));
    }

    public boolean setClose() {
        if (nowOpen != null) {
            GuiCloseEvent<Handler> event = new GuiCloseEvent<>(nowOpen);
            nowOpen = null;
            windowId = -1;
            WdsjServerAPI.getEventBus().post(event);
            return true;
        }
        return false;
    }

    public Gui<Handler> getHistory(Class<? extends Gui<?>> clazz, int v) {
        List<Gui<Handler>> guis = history.get(clazz);
        if (guis.size() >= v) {
            return guis.get(guis.size() - v);
        }
        return null;
    }


    public Gui<Handler> removeHistory(Class<? extends Gui<?>> clazz, int v) {
        List<Gui<Handler>> guis = history.get(clazz);
        if (guis.size() >= v) {
            return guis.remove(guis.size() - v);
        }
        return null;
    }



    public void addHistory(Gui<Handler> guiMenu) {
        Optional<Class<? extends Gui>> superImplClass = ClassUtils.getSuperImplClass(guiMenu.getClass(), Gui.class);
        superImplClass.ifPresent(aClass -> {
            //     System.out.println("superImplClass: " + superImplClass.get().getName());
            List<Gui<Handler>> guis = history.getOrDefault(aClass, new ArrayList<>());
            if (guis.size() > 4) {
                guis.remove(0);
            }
            guis.add(guiMenu);
            history.put(aClass, guis);
        });
    }


    public boolean isOpenOverride() {
        return openOverride;
    }


}
