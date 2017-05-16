package com.ecjtu.whack_a_mole.util;

import android.util.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator on 2017-04-20.
 * @version 1.0
 */
public class LoginUser {
    private String name;
    private Date loginTime;
    private LoginUser() {}
    private static final LoginUser single = new LoginUser();
    //静态工厂方法
    public static LoginUser getInstance() {
        return single;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
