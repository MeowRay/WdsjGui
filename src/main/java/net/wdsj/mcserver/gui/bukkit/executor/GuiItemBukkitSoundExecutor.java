package net.wdsj.mcserver.gui.bukkit.executor;

import net.wdsj.servercore.compatible.XSound;
import org.bukkit.entity.Player;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 14:25
 */
public class GuiItemBukkitSoundExecutor extends GuiItemBukkitExecutor {

    public XSound xSound;
    private final float volume;
    private final float pitch;

    public GuiItemBukkitSoundExecutor(XSound xSound, float volume, float pitch) {
        this.xSound = xSound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public boolean allowAsyncExecute(Player player) {
        xSound.play(player , volume, pitch);
        return true;
    }


}
