package com.eric.baselib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.eric.baselib.views.snack.MySnackbar;

import java.lang.reflect.InvocationTargetException;

import static com.eric.baselib.views.snack.MySnackbar.APPEAR_FROM_TOP_TO_DOWN;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 常用方法(使用前需要)：
 * 1.App全局应用信息管理
 * 2.dip px转换
 * 3.快速toast提示
 * version: 1.0
 */

public class Util {
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private Util() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());
    }

    public static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        }
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

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
        toast(activity.getWindow().getDecorView(), msg);
    }

    public static void toast(View view, String msg) {
        MySnackbar.make(view, msg, MySnackbar.LENGTH_SHORT, APPEAR_FROM_TOP_TO_DOWN).show();
    }

    /**
     * 监测当前网络连接是否可用
     */
    public static boolean isNetAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                // 当前网络是连接的
                if (info != null && info.isConnected()) {
                    // 当前所连接的网络可用
                    return info.isAvailable();
                }
            }
        }
        return false;
    }

    static final AdaptScreenArgs ADAPT_SCREEN_ARGS = new AdaptScreenArgs();

    static class AdaptScreenArgs {
        int     sizeInPx;
        boolean isVerticalSlide;
    }

}
