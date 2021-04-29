package net.wdsj.mcserver.gui.common.wrapper;

import lombok.Getter;
import net.wdsj.common.simpleconfig.ConfigurationSection;
import net.wdsj.mcserver.gui.common.GuiFactory;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.config.GuiMenuMainConfig;
import net.wdsj.mcserver.gui.common.model.GuiMenuConfigModel;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import net.wdsj.servercore.config.invoke.ConfigInvoke;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/29 12:00
 */
public class GuiMenuConfigWrapper implements GuiWrapper {

    @Getter
    private final GuiMenuConfigModel guiMenuConfigModel;
    @Getter
    private final GuiMenuMainConfig guiMenuMainConfig;



    public GuiMenuConfigWrapper(ConfigurationSection section) {
        guiMenuMainConfig = ConfigInvoke.invoke(GuiMenuMainConfig.class, section);
        guiMenuConfigModel = new GuiMenuConfigModel(guiMenuMainConfig);
    }



    @Override
    public boolean requirementCanOpen(Object handler) {
        return MenuUtils.scriptExecute(guiMenuMainConfig.getRequirement(), handler, false);
    }

    @Override
    public String getCommand() {
        return guiMenuMainConfig.getOpenCommand();
    }

    public <T> void open(GuiMenuRenderAdapter<T, ?> adapter, T handler) {
        GuiManager.open(handler, guiMenuConfigModel.create(adapter, handler));
    }


    @Override
    public <T> void open(T handler, Map<String, String> args) {
        GuiManager.open(handler, guiMenuConfigModel.create(GuiFactory.getDefaultMenuRenderAdapter(), handler));
    }


}
