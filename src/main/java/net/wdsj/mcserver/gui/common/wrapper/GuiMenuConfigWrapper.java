package net.wdsj.mcserver.gui.common.wrapper;

import lombok.Getter;
import net.wdsj.common.simpleconfig.ConfigurationSection;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.adapter.GuiMenuRenderAdapter;
import net.wdsj.mcserver.gui.common.config.GuiMenuMainConfig;
import net.wdsj.mcserver.gui.common.model.GuiMenuConfigModel;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import net.wdsj.servercore.config.invoke.ConfigInvoke;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/29 12:00
 */
public class GuiMenuConfigWrapper implements GuiRequirementOpen{
    @Getter
    private final GuiMenuConfigModel guiMenuConfigModel;
    private final GuiMenuMainConfig guiMenuMainConfig;


    public GuiMenuConfigWrapper(ConfigurationSection section) {
        guiMenuMainConfig = ConfigInvoke.invoke(GuiMenuMainConfig.class, section);
        guiMenuConfigModel = new GuiMenuConfigModel(guiMenuMainConfig);
    }



    @Override
    public boolean requirementCanOpen(Object handler) {
        return MenuUtils.scriptExecute(guiMenuMainConfig.getRequirement(), handler, false);
    }

    public <T> void open(GuiMenuRenderAdapter<T, ?> adapter, T handler) {
        GuiManager.open(handler, guiMenuConfigModel.create(adapter, handler));
    }


}
