package com.skey.chainprogrammingdemo;

/**
 * Description:
 * <br/>
 * Date: 2019/12/5 0:18
 *
 * @author ALion
 */
public class TimeUtil {

    public static void timer(int times, Runnable runnable) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < times; i++) {
            runnable.run();
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void timer(Runnable runnable) {
        long start = System.currentTimeMillis();

        runnable.run();

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
