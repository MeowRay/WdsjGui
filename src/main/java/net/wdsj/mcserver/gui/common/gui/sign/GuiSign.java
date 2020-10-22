package net.wdsj.mcserver.gui.common.gui.sign;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.wdsj.mcserver.gui.common.Gui;
import net.wdsj.mcserver.gui.common.GuiFactory;
import net.wdsj.mcserver.gui.common.GuiManager;
import net.wdsj.mcserver.gui.common.adapter.GuiSignRenderAdapter;
import net.wdsj.mcserver.gui.common.executor.GuiSignExecutor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/4 15:18
 */

@Getter
public class GuiSign<Handler> implements Gui<Handler> {


    private final Handler owner;
    private final String[] lines;
    private final List<GuiSignExecutor<Handler>> executors;

    private final GuiSignRenderAdapter<Handler> renderAdapter = (GuiSignRenderAdapter<Handler>) GuiFactory.getDefaultSignRenderAdapter();

    public GuiSign(Handler owner, String... lines) {
        this(owner, lines, new GuiSignExecutor[0]);
    }

    public GuiSign(Handler owner, String[] lines, GuiSignExecutor<Handler>... executors) {
        this.owner = owner;
        this.executors = Lists.newArrayList(executors);
        ArrayList<String> strings = Lists.newArrayList(lines);
        if (strings.size() > 4) {
            for (int i = 0; i < strings.size() - 4; i++) {
                strings.remove(strings.size() - 1);
            }
        } else if (strings.size() < 4) {
            for (int i = -1; i < 4 - strings.size(); i++) {
                strings.add("");
            }
        }

        this.lines = strings.toArray(new String[0]);
    }

    public void execute(Handler handler, String[] args) {
        for (GuiSignExecutor<Handler> executor : executors) {
            if (!executor.execute(handler, args)) {
                return;
            }
        }
    }

    public GuiSignRenderAdapter<Handler> renderAdapter() {
        return renderAdapter;
    }

    @Override
    public int open(Handler handler) {
        return renderAdapter().open(handler, this);
    }

    @Override
    public void close(Handler handler, int id) {
        renderAdapter().close(handler, id);
    }
}
