package com.ecjtu.whack_a_mole.util;

import java.util.Random;

import static oracle.net.aso.C00.v;

/**
 * @author Administrator on 2017/1/9.
 * @version 1.0
 */
public class MyRandom {
    public static int getNumble(int num){

        Random random = new Random();
        int x = Math.abs(random.nextInt()) % num;
        return x;
    }
}
