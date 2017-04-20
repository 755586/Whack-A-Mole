package com.ecjtu.whack_a_mole.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator on 2017-04-20.
 * @version 1.0
 */
public class GameWord {
    private List<String> allTypeName;
    private Map<String,Integer> allType;
    private GameWord() {}
    private static final GameWord single = new GameWord();
    //静态工厂方法
    public static GameWord getInstance() {
        return single;
    }

    public List<String> getAllTypeName() {
        return allTypeName;
    }

    public void setAllTypeName(List<String> allTypeName) {
        this.allTypeName = allTypeName;
    }

    public Map<String, Integer> getAllType() {
        return allType;
    }

    public void setAllType(Map<String, Integer> allType) {
        this.allType = allType;
    }
}
