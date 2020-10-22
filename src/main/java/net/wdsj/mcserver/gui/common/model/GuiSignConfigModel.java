package net.wdsj.mcserver.gui.common.model;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import mc233.cn.wdsjlib.bukkit.utils.Utils;
import net.wdsj.mcserver.gui.common.GuiConfigManager;
import net.wdsj.mcserver.gui.common.config.GuiSignMainConfig;
import net.wdsj.mcserver.gui.common.creator.GuiSignExecutorCreator;
import net.wdsj.mcserver.gui.common.executor.GuiSignExecutor;
import net.wdsj.mcserver.gui.common.gui.sign.GuiSign;
import net.wdsj.servercore.guava.Collections2Trans;
import net.wdsj.servercore.utils.PlaceholderUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/6 13:17
 */
public abstract class GuiSignConfigModel<Handler> implements GuiSignModel<Handler> {

    private final GuiSignMainConfig config;

    public GuiSignConfigModel(GuiSignMainConfig config) {
        this.config = config;
    }


    @Override
    public GuiSign<Handler> create(Handler handler, Map<String, String> replaceMap) {
        List<String> transform = Lists.transform(config.getLines(), s -> {
            String apply = Collections2Trans.getPlaceholderReplacer(handler).apply(s);
            return PlaceholderUtils.replace(apply, replaceMap);
        });
        return new GuiSign<>(handler, transform.toArray(new String[]{}), getExecutor(handler, replaceMap));
    }

    public abstract GuiSignExecutor<Handler> getExecutor(Handler handler, Map<String, String> replaceMap);


}
