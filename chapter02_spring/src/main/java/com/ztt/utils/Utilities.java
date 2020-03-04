package com.ztt.utils;

import java.util.Random;

/**
 * Created by zhoutaotao on 17/6/4.
 */
public class Utilities {

    // 生成随机数
    public static int[] createArray(int length) {

        Random random = new Random();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(100) - random.nextInt(100); // 生成两个随机数相减，保证生成的数中有负数
            System.out.print(array[i] + " ");
        }
        System.out.println();
        return array;
    }


}
