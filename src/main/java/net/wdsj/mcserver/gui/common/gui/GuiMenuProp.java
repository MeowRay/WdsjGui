package net.wdsj.mcserver.gui.common.gui;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import mc233.cn.wdsjlib.bukkit.api.wrapper.SoundWrapper;
import net.wdsj.servercore.compatible.XSound;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/20 9:46
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class GuiMenuProp {

    private static final SoundWrapper UI_CLICK = new SoundWrapper(XSound.UI_BUTTON_CLICK);

    private SoundWrapper openSound = new SoundWrapper(XSound.UI_BUTTON_CLICK);

    private SoundWrapper closeSound;

    private SoundWrapper clickSound = UI_CLICK;



}
