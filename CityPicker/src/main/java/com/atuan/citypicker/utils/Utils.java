package com.atuan.citypicker.utils;

public class Utils {

    private static long lastClickTime;

    /**
     * 500毫秒才能再次点击 多次点击
     *
     * @return
     */
    public static boolean enableClick() {
        return enableClick(500);
    }

    /**
     * delayMs 间隔时间
     *
     * @return
     */
    public static boolean enableClick(long delayMs) {
        long time = Math.abs(System.currentTimeMillis() - lastClickTime);
        if (time > delayMs) {
            lastClickTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }


}
