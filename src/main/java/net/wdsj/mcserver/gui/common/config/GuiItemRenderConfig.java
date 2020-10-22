package net.wdsj.mcserver.gui.common.config;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.wdsj.servercore.config.invoke.annotation.ListInvoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 21:14
 */
@Getter
@ToString
public class GuiItemRenderConfig {

    private int update = -1;

    private String model = null;

    private String repo = null;

    private Map<String, Object> args = new HashMap<>();

    private List<Integer> slot = new ArrayList<>();

    @ListInvoke
    private List<GuiItemStackConfig> display = new ArrayList<>();

    @ListInvoke
    private List<GuiItemShowConfig> displayCondition = new ArrayList<>();
    @Setter
    private Map<String, List<String>> action = new HashMap<>();

    private Map<String, String> options = new HashMap<>();

    private transient Map<String,String> stringArgs = new HashMap<>();


    public Map<String, String> getStringArgs() {
        return stringArgs = Maps.transformEntries(Maps.filterValues(getArgs(), o -> o instanceof String), (s, o) -> (String) o);
    }

}
