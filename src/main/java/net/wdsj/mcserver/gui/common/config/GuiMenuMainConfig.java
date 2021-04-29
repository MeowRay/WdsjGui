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
 * @date 2018/8/27 21:02
 */
@Getter
public class GuiMenuMainConfig {

    private String title = "menu";
    private String parent = null ;
    private String openCommand = null;
    private String construct = "";
    private String type = "CHEST";
    private List<String> layout = new ArrayList<>();
    private int size = -1;


    private String requirement = "true";


    private List<String> openAction = new ArrayList<>();
    private List<String> closeAction = new ArrayList<>();

    private Map<String, Container> container = new HashMap<>();

    private Map<String, GuiItemRenderConfig> items = new HashMap<>();



    public boolean isInherit() {
        return "INHERIT".equals(type);
    }

    @Getter
    public static class Container{

        private String range;
        @ListInvoke
        private List<GuiItemRenderConfig> items = new ArrayList<>();

    }

}
