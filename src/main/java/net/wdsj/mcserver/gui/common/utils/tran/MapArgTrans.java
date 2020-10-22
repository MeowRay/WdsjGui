package net.wdsj.mcserver.gui.common.utils.tran;

import com.google.common.base.Function;

import java.util.Map;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/28 19:24
 */
public class MapArgTrans implements Function<String, String> {

    private final Map<String, String> args;

    public MapArgTrans(Map<String,String> args){
        this.args = args;
    }

    @Override
    public String apply(String s) {
        for (Map.Entry<String, String> entry : args.entrySet()) {
            s = s.replace("$" + entry.getKey(), entry.getValue());
        }
        return s;
    }
}
