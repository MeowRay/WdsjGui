package net.wdsj.mcserver.gui.common.extra.viewexector;

import net.wdsj.common.simpleconfig.ConfigurationSection;

/**
 * @author Arthur
 * @version 1.0
 * @date 2020/10/10 23:50
 */
public interface GuiItemViewExecutorConfigCreator extends GuiItemViewExecutorCaller<ConfigurationSection> {

    <Handler> GuiItemViewExecutor<Handler> get(ConfigurationSection key);

    default Class<ConfigurationSection> getKeyClass() {
        return ConfigurationSection.class;
    }

}
