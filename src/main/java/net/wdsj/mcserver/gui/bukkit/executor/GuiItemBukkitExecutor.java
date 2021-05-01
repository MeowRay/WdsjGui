package net.wdsj.mcserver.gui.bukkit.executor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import net.wdsj.mcserver.gui.bukkit.GuiBukkit;
import net.wdsj.mcserver.gui.common.execption.GuiItemExecuteException;
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


    private boolean async;

    public GuiItemBukkitExecutor(boolean async) {
        this.async = async;
    }

    @Override
    public <T extends GuiItemCanAsyncExecutor<Player>> T setAsync(boolean async) {
        this.async = async;
        return (T) this;
    }

    @Override
    public abstract boolean exec(Player player);

    @Override
    public boolean async(Player player) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(GuiBukkit.getInstance(), () -> {
                exec(player);
            });
            return true;
        }
        return exec(player);
    }

    @SneakyThrows
    @Override
    public boolean sync(Player player) {
        try {
            return Bukkit.getScheduler().callSyncMethod(GuiBukkit.getInstance(), () -> exec(player)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            //  throw new GuiItemExecuteException(e);
            return false;
        }
    }


}
