package net.wdsj.mcserver.gui.common.config;

import lombok.Getter;
import net.wdsj.servercore.config.invoke.annotation.ListInvoke;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/29 15:02
 */
@Getter
public class GuiItemShowConfig {

    public String condition = "true";

    @ListInvoke
    public List<GuiItemStackConfig> item = new ArrayList<>();

}
