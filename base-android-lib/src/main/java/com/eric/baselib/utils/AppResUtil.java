package com.eric.baselib.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/7/21
 * desc   : 快速获取应用图片和颜色值资源工具类
 * version: 1.0
 */
public class AppResUtil {
    public static int getColor(Context context, int colorId) {
        int color = Color.WHITE;
        try {
            color = ContextCompat.getColor(context, colorId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return color;
    }

    public static Drawable getDrawable(Context context, int drawableId) {
        Drawable drawable = new ColorDrawable(Color.WHITE);
        try {
            drawable = ContextCompat.getDrawable(context, drawableId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return drawable;
    }
}