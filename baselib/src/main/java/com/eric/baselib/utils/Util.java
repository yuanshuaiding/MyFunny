package com.eric.baselib.utils;

import android.app.Activity;
import android.content.Context;

import com.eric.baselib.views.snack.MySnackbar;

import static com.eric.baselib.views.snack.MySnackbar.APPEAR_FROM_TOP_TO_DOWN;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 常用方法：
 * 1.dip px转换
 * 2.快速toast提示
 * version: 1.0
 */

public class Util {
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void toast(Activity activity, String msg) {
//        Snackbar snackBar = Snackbar.make(activity.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(snackBar.getView().getLayoutParams().width, snackBar.getView().getLayoutParams().height);
//        params.gravity = Gravity.TOP;
//        params.topMargin = ScreenUtil.getActionBarHeight(activity);
//        snackBar.getView().setLayoutParams(params);
//        snackBar.show();
        MySnackbar.make(activity.getWindow().getDecorView(), msg, MySnackbar.LENGTH_SHORT, APPEAR_FROM_TOP_TO_DOWN).show();
    }

}
