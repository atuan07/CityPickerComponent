package com.atuan.citypicker.utils;

import com.atuan.citypicker.R;


public class CityPickerMange {

    private static CityPickerMange singleton;
    public int statusBarColor = R.color.color_ffffff;
    public int toolbarBarColor = R.color.color_ffffff;
    public int toolbarBarTextColor = R.color.color_333333;
    public int toolbarBarBackDrawable = R.mipmap.cp_icon_back;

    private CityPickerMange() {
    }

    public static CityPickerMange getInstance() {
        if (singleton == null) {
            singleton = new CityPickerMange();
        }
        return singleton;
    }

    /**
     * 设置状态栏 背景颜色
     *
     * @param color
     */
    public void setStatusBarColor(int color) {
        statusBarColor = color;
        toolbarBarColor = color;
        if (color != R.color.color_ffffff) {
            toolbarBarTextColor = R.color.color_ffffff;
        }
    }

    /**
     * 设置标题栏 背景颜色
     *
     * @param color
     */
    public void setToolbarBarColor(int color) {
        toolbarBarColor = color;
    }

    /**
     * 设置标题栏 字体颜色
     *
     * @param color
     */
    public void setToolbarBarTextColor(int color) {
        toolbarBarTextColor = color;
    }

    /**
     * 设置标题栏 返回按钮图片
     *
     * @param backDrawable
     */
    public void setToolbarBarBackDrawable(int backDrawable) {
        toolbarBarBackDrawable = backDrawable;
    }

    public boolean isStatusBarColorFFF() {
        return statusBarColor == R.color.color_ffffff;
    }

    public boolean isStatusBarColorFFF(int color) {
        return color == R.color.color_ffffff;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getToolbarBarColor() {
        return toolbarBarColor;
    }

    public int getToolbarBarTextColor() {
        return toolbarBarTextColor;
    }

    public int getToolbarBarBackDrawable() {
        return toolbarBarBackDrawable;
    }
}
