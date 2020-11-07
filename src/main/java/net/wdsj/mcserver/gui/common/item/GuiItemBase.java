package net.wdsj.mcserver.gui.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import gnu.trove.set.hash.THashSet;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.servercore.common.placeholder.PlaceholderManager;
import net.wdsj.servercore.interfaces.Executor;
import net.wdsj.servercore.interfaces.Replacement;

import java.util.Set;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/12 16:29
 */
@Accessors(chain = true)
public abstract class GuiItemBase<Handler, Item> implements GuiItem<Handler, Item> {

    @Getter
    private Set<Replacement<Handler>> replacements = new THashSet<>();

    private Multimap<InventoryAction, Executor<Handler>> executorMultimap = HashMultimap.create();


    public GuiItemBase<Handler, Item> addReplacement(Replacement<Handler> replacement) {
        replacements.add(replacement);
        return this;
    }

    public GuiItemBase<Handler, Item>  removeReplacement(Replacement<Handler> replacement) {
        replacements.remove(replacement);
        return  this;
    }

    public GuiItemBase<Handler, Item>   addActionExecutor(InventoryAction inventoryAction, Executor<Handler> executor) {
        executorMultimap.put(inventoryAction, executor);
        return this;
    }

    public GuiItemBase<Handler, Item> removeActionExecutor(InventoryAction inventoryAction, Executor<Handler> executor) {
        executorMultimap.remove(inventoryAction, executor);
        return this;
    }

    public GuiItemBase<Handler, Item> removeActionExecutorAll(InventoryAction inventoryAction) {
        executorMultimap.removeAll(inventoryAction);
        return this;
    }

    public GuiItemBase<Handler, Item> removeAllExecutor() {
        executorMultimap.clear();
        return this;
    }

    public String replace(Handler handler, String str) {
        if (str == null) return null;
        for (Replacement<Handler> replacement : replacements) {
            str = replacement.replaceMessage(handler, str);
        }
        return PlaceholderManager.replace(handler, str);
    }

    public void execute(Handler handler, InventoryAction inventoryAction) {
        for (Executor<Handler> handlerExecutor : executorMultimap.get(inventoryAction)) {
            handlerExecutor.execute(handler);
        }
        if (inventoryAction != null) {
            for (Executor<Handler> handlerExecutor : executorMultimap.get(InventoryAction.ALL)) {
                handlerExecutor.execute(handler);
            }
        }
    }

    @SneakyThrows
    @Override
    public GuiItemBase<Handler, Item> clone() {
        GuiItemBase<Handler, Item> clone = (GuiItemBase<Handler, Item>) super.clone();
        clone.executorMultimap = HashMultimap.create(executorMultimap);
        clone.replacements = new THashSet<>(replacements);
        return clone;
    }
}
