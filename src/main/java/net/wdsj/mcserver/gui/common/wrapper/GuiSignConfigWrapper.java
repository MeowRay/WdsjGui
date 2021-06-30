package net.wdsj.mcserver.gui.common.wrapper;

import lombok.Getter;
import net.wdsj.common.simpleconfig.ConfigurationSection;
import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.config.GuiSignMainConfig;
import net.wdsj.mcserver.gui.common.creator.GuiSignExecutorCreator;
import net.wdsj.mcserver.gui.common.execption.GuiSignExecutorCreatorNotFoundException;
import net.wdsj.mcserver.gui.common.executor.GuiSignExecutor;
import net.wdsj.mcserver.gui.common.model.GuiSignConfigModel;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import net.wdsj.servercore.config.invoke.ConfigInvoke;
import net.wdsj.servercore.utils.PlaceholderUtils;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/6 14:52
 */
@Getter
public class GuiSignConfigWrapper implements CanOpenItem {

    private final GuiSignMainConfig config;
    private final GuiSignConfigModel guiSignConfigModel;
    private final boolean permissionBypass ;

    public GuiSignConfigWrapper(ConfigurationSection section) {
        config = ConfigInvoke.invoke(GuiSignMainConfig.class, section);
        permissionBypass = config.getRequirement().equals("true");
        guiSignConfigModel = new GuiSignConfigModel(config) {
            @Override
            public GuiSignExecutor getExecutor(Object o, Map replaceMap) {
                GuiSignExecutorCreator<?> signExecutorCreator = GuiConfigManager.getSignExecutorCreator(config.getType());
                if (signExecutorCreator != null) {
                    String replace = PlaceholderUtils.replace(config.getArg(), replaceMap);
                    GuiSignExecutor guiSignExecutor;
                    GuiSignExecutor guiSignExecutor1 = signExecutorCreator.create(replace);
                    if (config.isIgnoreEmpty()) {
                        guiSignExecutor = (o1, lines) -> {
                         //   System.out.println(ArrayUtils.toString(lines));
                            if (lines[config.getLine() - 1].isEmpty()) {
                                return false;
                            }
                            return guiSignExecutor1.execute(o1, lines);
                        };
                    } else {
                        guiSignExecutor = guiSignExecutor1;
                    }
                    return guiSignExecutor;
                }
                throw new GuiSignExecutorCreatorNotFoundException(config.getType());
            }
        };
    }

    @Override
    public boolean requirementCanOpen(Object handler) {
        return permissionBypass|| MenuUtils.scriptExecute(config.getRequirement(), handler, false);
    }


    @Override
    public String getCommand() {
        return config.getOpenCommand();
    }
    public <T> void open(T handler, Map<String, String> args) {
        GuiManager.open(handler, guiSignConfigModel.create(handler, args));
    }

}
