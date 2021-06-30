package net.wdsj.mcserver.gui.common.wrapper;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/9/10 22:04
 */
public interface CanOpenItem {

     boolean requirementCanOpen(Object handler);

     String getCommand();

     <T> void open(T handler, Map<String, String> args);
}
