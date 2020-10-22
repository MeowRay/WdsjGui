package net.wdsj.mcserver.gui.bukkit.executor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.executor.GuiItemCanAsyncExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutionException;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/21 13:06
 */
@NoArgsConstructor
@Getter
public abstract class GuiItemBukkitExecutor implements GuiItemCanAsyncExecutor<Player> {


    private boolean async = true;

    public GuiItemBukkitExecutor(boolean async) {
        this.async = async;
    }

    @Override
    public <T extends GuiItemCanAsyncExecutor<Player>> T setAsync(boolean async) {
        this.async = async;
        return (T) this;
    }

    @Override
    public abstract boolean allowAsyncExecute(Player player);

    @Override
    public boolean async(Player player) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), () -> {
                allowAsyncExecute(player);
            });
            return true;
        }
        return allowAsyncExecute(player);
    }

    @Override
    public boolean sync(Player player) {
        try {
            return Bukkit.getScheduler().callSyncMethod(GuiBukkit.getInstance(), () -> allowAsyncExecute(player)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return allowAsyncExecute(player);
    }


}
