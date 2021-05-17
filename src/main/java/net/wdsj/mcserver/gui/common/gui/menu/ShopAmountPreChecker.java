package net.wdsj.mcserver.gui.common.gui.menu;

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/5/17 16:45
 */
public interface ShopAmountPreChecker<Handler, T> {

    boolean preCheck(Handler handler, T commodity, int amount);

}
