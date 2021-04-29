package net.wdsj.mcserver.gui.common.config;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/6 13:17
 */


@Getter
public class GuiSignMainConfig {

    private List<String> lines = new ArrayList<>();

    private String openCommand = null;

    private int line = 1;

    private boolean ignoreEmpty = false;

    private String requirement = "true";

    private String type = "command";

    private String arg = "";


}
