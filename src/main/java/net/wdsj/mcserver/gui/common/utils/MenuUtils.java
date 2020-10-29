package net.wdsj.mcserver.gui.common.utils;

import com.google.common.collect.Maps;
import gnu.trove.map.hash.TIntObjectHashMap;
import javafx.util.Pair;
import mc233.cn.wdsjlib.global.api.eco.EcoAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.wdsj.servercore.common.placeholder.PlaceholderManager;
import net.wdsj.servercore.utils.ScriptUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 16:51
 */
public class MenuUtils {

    private static final Map<String, Object> scriptObjMap = new HashMap<>();


    public static void putScriptObject(String key, Object obj) {
        scriptObjMap.put(key, obj);
    }

    public static String[] getFoo(String string) {
        List<String> list = new ArrayList<>();
        int start = 0;

        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '[') {
                start = i;
                continue;
            }
            if (start != 0) {
                if (c == ']') {
                    list.add(string.substring(start + 1, i));
                    start = 0;
                }
                continue;
            }
            list.add(String.valueOf(c));
        }
        return list.toArray(new String[0]);
    }

    public static TIntObjectHashMap<String> getLayoutMap(TIntObjectHashMap<String[]> boxMap, int length) {
        TIntObjectHashMap<String> map = new TIntObjectHashMap<>();
        for (int i = 0; i < length; i++) {
            String[] strings = boxMap.get(i);
            if (strings != null) {
                for (int ii = 0; ii < strings.length; ii++) {
                    String foo = strings[ii];
                    if (!StringUtils.isBlank(foo)) {
                        int slot;
                        if (i < 1) {
                            slot = ii;
                        } else {
                            slot = ii + (i * 9);
                        }
                        map.put(slot, foo);
                    }
                }
            }
        }
        return map;
    }

    public static String replace(String text, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            text = text.replace("$" + entry, entry.getValue());
        }
        return text;
    }

    public static <T, Handler> T scriptExecute(String text, Handler handler, T def) {
        return scriptExecute(text, new HashMap<>(), handler, def);
    }

    public static <T, Handler> T scriptExecute(String text, Map<String, Object> map, Handler handler, T def) {
        if (text.isEmpty()) return def;
        try {
            String replace = PlaceholderManager.replace(handler, text);
            HashMap<String, Object> oMap = new HashMap<>(map);
            oMap.putAll(scriptObjMap);
            oMap.put("handler", handler);
            Object value = ScriptUtils.getValue(replace, oMap);
            return (T) value;
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }


}
