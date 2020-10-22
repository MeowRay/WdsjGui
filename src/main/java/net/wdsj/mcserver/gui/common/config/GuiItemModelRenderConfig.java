package net.wdsj.mcserver.gui.common.config;

import lombok.Getter;
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
public class GuiItemModelRenderConfig {

    private int update = -1;

    private Map<String, Object> args = new HashMap<>();

    private List<String> requirementArgs = new ArrayList<>();

    @ListInvoke
    private List<GuiItemStackConfig> display = new ArrayList<>();

    private Map<String, ArrayList<String>> action = new HashMap<>();


}
