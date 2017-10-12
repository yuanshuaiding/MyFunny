package com.eric.baselib.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/11
 * desc   : 视图操作工具类：
 * 1. 解析注解绑定控件对象
 * 。。。
 * version: 1.0
 */

public class ViewUtils {
    /**
     * 将activity里的控件绑定到activity里带注解的属性上，同时如有事件注解，则把事件绑定到指定控件上
     */
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }


    /**
     * 将指定view里的控件对象绑定到holder里带注解的属性上，同时如有事件注解，则把事件绑定到指定控件上
     */
    public static void inject(View view, Object holder) {
        inject(new ViewFinder(view), holder);
    }

    private static void inject(ViewFinder finder, Object holder) {
        //绑定控件
        injectFields(finder, holder);
        //绑定事件
        injectEvents(finder, holder);
    }

    private static void injectFields(ViewFinder finder, Object holder) {
        if (finder == null || holder == null) return;
        Class<?> clz = holder.getClass();
        Field[] fields = clz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                BindView bindView = field.getAnnotation(BindView.class);
                if (bindView != null) {
                    int viewId = bindView.value();
                    View view = finder.getView(viewId);
                    try {
                        field.set(holder, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectEvents(ViewFinder finder, Object holder) {
        // TODO: 2017/10/11
    }


}
