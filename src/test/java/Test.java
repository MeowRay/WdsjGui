import com.google.common.collect.Lists;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 16:57
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(ArrayUtils.toString(MenuUtils.getFoo("^^^^^^^^^")));
        String text = "close";
        System.out.println(ArrayUtils.toString(text.split(":", 3)));

        ArrayList<String> strings = Lists.newArrayList("", "11","" );
        if (strings.size() > 4) {
            for (int i = 0; i < strings.size() - 4; i++) {
                strings.remove(strings.size() - 1);
            }
        } else if (strings.size() < 4) {
            for (int i = -1; i < 4 - strings.size(); i++) {
                strings.add("");
            }
        }
        System.out.println(ArrayUtils.toString(strings));

    }
}
